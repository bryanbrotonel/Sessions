package info3245.sessions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Timer extends Container {

    //For Usage
    private static final String Usage_Today = "Usage_Today" ;
    private static final String Usage_Yesterday = "Usage_Yesterday" ;

    private static final String Date = "dateKey";
    private static final String Sessions = "sessionsKey";
    private static final String Usage = "usageKey";
    private static final String Goal = "goalKey";

    private final String defaultString = "-";
    private final int defaultInt = 0;

    private String date_Usage_Today = defaultString;
    private int usage_Usage_Today = defaultInt;
    private int sessions_Usage_Today = defaultInt;
    private int goal_Usage_Today = 4;

    SharedPreferences sharedPref;

    //For Setting
    private static final String timerPrefs = "Timer_Prefs" ;
    private static final String settingsPrefs = "Settings_Prefs" ;

    private static final String shortBreakKey = "shortBreakKey";
    private static final String longBreakKey = "longBreakKey";

    private final long snTimeDef = 20;
    private final long shrtBrkTimeDef = 2;
    private final long lngBrkTimeDef = 20;

    //For Timer
    private  long startingTime;
    private  long focusTimeStart;
    private  long shortBreakTimeStart;
    private  long longBreakTimeStart;
    private TextView mTextViewCountDown, mTimerType;
    private ImageButton mPlay,mReset, mRestart;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private long timeRemaining;
    private long mEndTime;
    public int sessionCount;
    public boolean focus = true;

    // TIMER FUNCTION METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_timer);

        writeTodayUsage();

        //Load data will load what user picked in settings
        loadData();


        sessionCount = 0;

        //Setting The Timer
        timeRemaining = startingTime = focusTimeStart;

        //Initializing Values
        mTextViewCountDown = findViewById(R.id.timer);
        mTimerType = findViewById(R.id.timerType);
        mPlay = findViewById(R.id.playButton);
        mReset =findViewById(R.id.resetButton);
        mRestart = findViewById(R.id.replayButton);

        // When play button is press, Timer clicks down or pause
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mTimerRunning){
                    pauseTimer();
                }
                else{
                    startTimer();
                }

            }
        });

        // When reset button is clicked, timer reset back to Starting Time
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }

        });

        // When Restart Button is clicked,  timer reset back to Starting Time and Timer Type is back to Focus
        mRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                focus = true;
                resetSessionsDisplay();
                resetTimer();
            }
        });
        updateCountDownText();

    }

    //Timer Code Methods

    // Countdown timer ticking down
    private void startTimer()
    {

        if (sessionCount == 4) {
            resetSessionsDisplay();
        }

        // This value is the projected endtime eg. 8:40 plus timer remaining of 10min -> endtime is 8:50
        // This will be used to calculate the time remaining, in the onStart() method
        mEndTime = System.currentTimeMillis() + timeRemaining;

        //A toast to show that your session has begun
        Toast.makeText(Timer.this, "Your session has begun!",
                Toast.LENGTH_SHORT).show();

        //We create a new eintent with our ReminderBroadcast class to build our new notification
        //We use a pending intent so we can call on it at a later time
        Intent intent = new Intent(Timer.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Timer.this,
                0, intent, 0);

        //Now create an alarm manager to to set off the reminder once the time reaches 0.5 seconds
        //from the end time
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, mEndTime-500, pendingIntent);

        //ticks down the timer and updates the textview
        countDownTimer = new CountDownTimer(timeRemaining,1000) {
            @Override
            public void onTick(long l) {

                timeRemaining = l;
                updateCountDownText();

            }

            @Override
            public void onFinish() {

                // Determine which timer type and setting the Starting time when finished
                // adds a session counter for usage
                if (focus) {
                    sessions_Usage_Today++;
                    sessionCount++;
                    updateSessionsDisplay();
                    updateUsageCount();

                    writeUsageData(Usage_Today, date_Usage_Today, usage_Usage_Today, sessions_Usage_Today, goal_Usage_Today);
                }

                // turn off timer and change the button icon
                mTimerRunning = false;
                mPlay.setImageResource(R.drawable.ic_playbutton);

                focus = !focus;

                setText();
                setTime();

            }
        }.start();
        mTimerRunning = true;
        mPlay.setImageResource(R.drawable.ic_pausebutton);

    }

    // Pausing the Timer setting canceling the timer
    private void pauseTimer()
    {
        countDownTimer.cancel();
        mTimerRunning = false;
        mPlay.setImageResource(R.drawable.ic_playbutton);

    }

    // Reset The Timer back to starting time
    private void resetTimer()
    {
        if (mTimerRunning) {
            pauseTimer();
        }

        setText();
        setTime();
        updateCountDownText();

    }

    //Determines the Starting time and update the TextView
    public void setTime() {
        startingTime = focus ? focusTimeStart :
                sessionCount == 4 ? longBreakTimeStart : shortBreakTimeStart;

        timeRemaining = startingTime;
        updateCountDownText();

    }

    // For notifying users when to focus/take a break
    public void setText() {
        mTimerType.setText(focus ? "Lets focus right now." :
                sessionCount == 4 ? "Take a long break. You deserve it." : "Take a quick break.");
    }

    // converting time remaining in Times and seconds and displaying it
    private  void updateCountDownText()
    {

        int min = (int) (timeRemaining/1000)/60;
        int sec = (int) (timeRemaining/1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", min, sec);
        mTextViewCountDown.setText(timeLeftFormatted);

        updateSessionsDisplay();
    }

    // Resets session count display
    private void resetSessionsDisplay() {

        int[] dots = { R.id.session1,R.id.session2,R.id.session3,R.id.session4};

        // Restore dots to default state
        for (int dot : dots) {
            ImageView img = findViewById(dot);
            img.setImageResource(R.drawable.ic_blue_circle);
        }

        sessionCount = 0;

    }

    // Updates session count display
    private void updateSessionsDisplay() {

        int[] dots = { R.id.session1,R.id.session2,R.id.session3,R.id.session4};

        ImageView dot;

        // Fill in dots for session count
        switch(sessionCount) {
            case 1:
                dot = findViewById(R.id.session1);
                dot.setImageResource(R.drawable.ic_yellow_circle);
                break;
            case 2:
                for (int i = 0; i < 2; i++) {
                    ImageView img = findViewById(dots[i]);
                    img.setImageResource(R.drawable.ic_yellow_circle);
                }
                break;
            case 3:
                for (int i = 0; i < 3; i++) {
                    ImageView img = findViewById(dots[i]);
                    img.setImageResource(R.drawable.ic_yellow_circle);
                }
                break;
            case 4:
                for (int i = 0; i < 4; i++) {
                    ImageView img = findViewById(dots[i]);
                    img.setImageResource(R.drawable.ic_yellow_circle);
                }
                break;
        }
    }

    // TIMER DATA METHODS

    // Initializing values chosen from setting
    private void loadData()
    {
        focusTimeStart = startingTime = convertTime(getSettingsData(sessionsKey, snTimeDef));
        shortBreakTimeStart = convertTime(getSettingsData(shortBreakKey, shrtBrkTimeDef));
        longBreakTimeStart = convertTime(getSettingsData(longBreakKey, lngBrkTimeDef));
    }

    //Getting values from Setting
    public long getSettingsData(String key, long defaultVal) {
        sharedPref = getSharedPreferences(settingsPrefs, Context.MODE_PRIVATE);

        return sharedPref.getLong(key, defaultVal);
    }


    // Update today usage data
    public void writeTodayUsage() {
        // Get recorded usage date
        date_Usage_Today = getUsageData(Usage_Today, Date);

        // Get today's recorded usage
        int usage = Integer.parseInt(getUsageData(Usage_Today, Usage));
        int sessions = Integer.parseInt(getUsageData(Usage_Today, Sessions));
        int goal = Integer.parseInt(getUsageData(Usage_Today, Goal));

        //Check if data's date is today
        if (!date_Usage_Today.equals(defaultString) && date_Usage_Today.equals(getTodaysDate())) {

            // Continue recording usage
            usage_Usage_Today = usage;
            sessions_Usage_Today = sessions;
            goal_Usage_Today = goal;
        }
        else {

            // Write usage data to Yesterday
            writeUsageData(Usage_Yesterday, date_Usage_Today, usage, sessions, goal);

            // Reset usage data
            date_Usage_Today = getTodaysDate();
            usage_Usage_Today = defaultInt;
            sessions_Usage_Today = defaultInt;
            goal_Usage_Today = 4;

        }
    }

    // Get usage data from Shared pref
    private String getUsageData(String usageDay, String data) {

        SharedPreferences sharedPref = getSharedPreferences(usageDay, Context.MODE_PRIVATE);

        return data.equals(Date) ? sharedPref.getString(data, "-") : String.valueOf(sharedPref.getInt(data, defaultInt));
    }

    // Write usage data to Shared pref file
    private void writeUsageData(String file, String date, int usage, int sessions, int goal) {

        // Write usageData data
        sharedPref = getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(Date, date);
        editor.putInt(Sessions, sessions);
        editor.putInt(Usage, usage);
        editor.putInt(Goal, goal);

        editor.commit();
    }

    // Update usage data count in minutes
    private void updateUsageCount() {
        usage_Usage_Today = (int) ((usage_Usage_Today + focusTimeStart) / 60000);

    }

    // ONSTART AND ONPAUSE

    // On Start, restores session's data if any, and manages current timer state
    @Override
    //When user switch back to timer, SharedPref will get all the values stored in OnStop();
    protected void onStart() {
        super.onStart();

        // Restore session data if any, else assign default values
        SharedPreferences prefs = getSharedPreferences(timerPrefs, MODE_PRIVATE);

        startingTime = prefs.getLong("sessionTime", snTimeDef);
        sessions_Usage_Today = prefs.getInt("sessionCount", 0);
        sessionCount = prefs.getInt("sessionDisplay", 0);
        usage_Usage_Today = prefs.getInt("usageCount", 0);
        timeRemaining = prefs.getLong("sessionTime", focusTimeStart);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        focus = prefs.getBoolean("focus", true);

        // Update text in view
        setText();
        updateCountDownText();

        // Manager timer state if running before
        if (mTimerRunning) {

            // Get time elapsed since activity on Pause
            mEndTime = prefs.getLong("endTime", 0);
            // Get the new Time remaining by subtracting the End time with the current time eg.8:50 - 8:45 = 0:05
            timeRemaining = mEndTime - System.currentTimeMillis();

            // the time remaining is negative, set the value to zero and update the timer
            if (timeRemaining < 0) {
                mTimerRunning = false;

                // Update view
                updateCountDownText();
                mPlay.setImageResource(R.drawable.ic_playbutton);

                // Update usage count if focus session
                if (focus)
                    updateUsageCount();

                focus = !focus;

                // Update view
                setText();
                setTime();
            }
            else {
                // Session still in continuing
                startTimer();
            }
        }
    }

    // On stop, updates today's usage, and saves current session's data
    @Override

    //When user switch to an different activity, this method will stop the counter and will store all the necessary values
    protected void onStop() {
        super.onStop();

        // Update today's usage
        writeTodayUsage();

        // Save current session's data
        SharedPreferences prefs = getSharedPreferences(timerPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("sessionTime", timeRemaining);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.putBoolean("focus", focus);
        editor.putInt("usageCount", usage_Usage_Today);
        editor.putInt("sessionCount", sessions_Usage_Today);
        editor.putInt("sessionDisplay", sessionCount);
        editor.apply();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // HELPER METHODS

    // Converts minutes to milliseconds
    public long convertTime(long time)
    {
        return time * 60000;
    }

    // Gets and formats today's date
    private String getTodaysDate() {

        String dateFormatString = "EEEE MMM d";

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);

        return dateFormat.format(calendar.getTime());
    }

    //Navigation Methods
    // Returns layout ID to Container Class
    @Override
    int getLayoutId() {
        return R.layout.activity_timer;
    }

    // Returns navigation ID to Container Class
    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.nav_timer;
    }
}