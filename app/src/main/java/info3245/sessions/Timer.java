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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Timer extends Container {

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

    private static final String timerPrefs = "Timer_Prefs" ;
    private static final String todayUsage = "Usage_Today" ;
    private static final String settingsPrefs = "Settings_Prefs" ;

    private static final String shortBreakKey = "shortBreakKey";
    private static final String longBreakKey = "longBreakKey";

    private final long snTimeDef = 20;
    private final long shrtBrkTimeDef = 2;
    private final long lngBrkTimeDef = 20;

    private  long startingTime;
    private  long focusTimeStart;
    private  long shortBreakTimeStart;
    private  long longBreakTimeStart;
    private TextView mTextViewCountDown, mTimerType;
    private ImageButton mPlay,mReset;
    private Button  mRestart;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private long timeRemaining;
    private long mEndTime;
    public boolean focus = true;

    // TIMER FUNCTION METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_timer);

        writeTodayUsage();

        //Timer Code
        loadData();

        timeRemaining = startingTime = focusTimeStart;

        mTextViewCountDown = findViewById(R.id.timer);
        mTimerType = findViewById(R.id.timerType);
        mPlay = findViewById(R.id.playButton);
        mReset =findViewById(R.id.resetButton);
        mRestart = findViewById(R.id.restartButton);

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
                if(!focus)
                {
                    mTimerType.setText("Focus");
                }

                focus = true;
                resetTimer();
            }
        });
        updateCountDownText();

    }

    //Timer Code Methods

    // Countdown timer ticking down
    private void startTimer()
    {

        mEndTime = System.currentTimeMillis() + timeRemaining;

        Toast.makeText(Timer.this, "Your session has begun!",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Timer.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Timer.this,
                0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, mEndTime-500, pendingIntent);

        countDownTimer = new CountDownTimer(timeRemaining,1000) {
            @Override
            public void onTick(long l) {

                timeRemaining = l;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                // Determine which timer type and setting the Starting time when finished

                if (focus) {
                    sessions_Usage_Today++;
                    updateUsageCount();

                    writeUsageData(Usage_Today, date_Usage_Today, usage_Usage_Today, sessions_Usage_Today, goal_Usage_Today);
                }

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

    // Pausing the Timer
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

    public void setTime() {
        startingTime = focus ? focusTimeStart :
                sessions_Usage_Today % 4 == 0 ? longBreakTimeStart : shortBreakTimeStart;

        timeRemaining = startingTime;
        updateCountDownText();

    }

    public void setText() {
        mTimerType.setText(focus ? "Focus" :
                (sessions_Usage_Today % 4 == 0) ? "Long Break" : "Short Break");
    }

    // converting time remaining in Times and seconds and displaying it
    private  void updateCountDownText()
    {

        int min = (int) (timeRemaining/1000)/60;
        int sec = (int) (timeRemaining/1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", min, sec);
        mTextViewCountDown.setText(timeLeftFormatted);

    }

    // TIMER DATA METHODS

    private void loadData()
    {
        focusTimeStart = startingTime = convertTime(getSettingsData(sessionsKey, snTimeDef));
        shortBreakTimeStart = convertTime(getSettingsData(shortBreakKey, shrtBrkTimeDef));
        longBreakTimeStart = convertTime(getSettingsData(longBreakKey, lngBrkTimeDef));
    }

    public long getSettingsData(String key, long defaultVal) {
        sharedPref = getSharedPreferences(settingsPrefs, Context.MODE_PRIVATE);

        return sharedPref.getLong(key, defaultVal);
    }

    public void writeTodayUsage() {
        // Get recorded usage date
        date_Usage_Today = getUsageData(Usage_Today, Date);

        int usage = Integer.parseInt(getUsageData(Usage_Today, Usage));
        int sessions = Integer.parseInt(getUsageData(Usage_Today, Sessions));
        int goal = Integer.parseInt(getUsageData(Usage_Today, Goal));

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

    private String getUsageData(String usageDay, String data) {

        SharedPreferences sharedPref = getSharedPreferences(usageDay, Context.MODE_PRIVATE);

        return data.equals(Date) ? sharedPref.getString(data, "-") : String.valueOf(sharedPref.getInt(data, defaultInt));
    }

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

    private void updateUsageCount() {
        usage_Usage_Today = (int) ((usage_Usage_Today + focusTimeStart) / 60000);

    }

    // ONSTART AND ONPAUSE

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences(timerPrefs, MODE_PRIVATE);

        startingTime = prefs.getLong("sessionTime", snTimeDef);
        sessions_Usage_Today = prefs.getInt("sessionCount", 0);
        usage_Usage_Today = prefs.getInt("usageCount", 0);
        timeRemaining = prefs.getLong("sessionTime", focusTimeStart);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        focus = prefs.getBoolean("focus", true);

        setText();
        updateCountDownText();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            timeRemaining = mEndTime - System.currentTimeMillis();

            if (timeRemaining < 0) {
                mTimerRunning = false;

                updateCountDownText();
                mPlay.setImageResource(R.drawable.ic_playbutton);

                if (focus)
                    updateUsageCount();

                focus = !focus;

                setText();
                setTime();
            }
            else {
                startTimer();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        writeTodayUsage();

        SharedPreferences prefs = getSharedPreferences(timerPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("sessionTime", timeRemaining);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.putBoolean("focus", focus);
        editor.putInt("usageCount", usage_Usage_Today);
        editor.putInt("sessionCount", sessions_Usage_Today);
        editor.apply();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // HELPER METHODS

    public long convertTime(long time)
    {
        return time * 60000;
    }

    private String getTodaysDate() {

        String dateFormatString = "EEEE MMM d";

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);

        return dateFormat.format(calendar.getTime());
    }


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