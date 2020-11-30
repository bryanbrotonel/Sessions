package info3245.sessions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class Timer extends Container {

    SharedPreferences sharedPref;

    private static final String timerPrefs = "Timer_Prefs" ;
    private static final String settingsPrefs = "Settings_Prefs" ;

    private static final String shortBreakKey = "shortBreakKey";
    private static final String longBreakKey = "longBreakKey";

    private long snTimeDef = 20;
    private long shrtBrkTimeDef = 2;
    private long lngBrkTimeDef = 20;

    //Timer Code
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
    public int sessionCount;

//Timer Code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_timer);

        sharedPref = getApplicationContext().getSharedPreferences(timerPrefs, Context.MODE_PRIVATE);

        //Timer Code
        loadData();

        timeRemaining = startingTime = focusTimeStart;

        sessionCount = 0;

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
                if(mTimerType.getText().toString().equals("Break") )
                {
                    mTimerType.setText("Focus");
                }

                startingTime = focusTimeStart;
                resetTimer();
                sessionCount = 0;

            }
        });
        updateCountDownText();
//Timer Code

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

    //Timer Code Methods

    // Countdown timer ticking down
    private void startTimer()
    {

        mEndTime = System.currentTimeMillis() + timeRemaining;

        countDownTimer = new CountDownTimer(timeRemaining,1000) {
            @Override
            public void onTick(long l) {

                timeRemaining = l;
                updateCountDownText();

            }

            @Override
            public void onFinish() {


                // Determine which timer type and setting the Starting time when finished
                if(mTimerType.getText().toString().equals("Focus"))
                {
                    mTimerType.setText("Break");
                    sessionCount++;
                    if (sessionCount == 4) {
                        startingTime = longBreakTimeStart;
                        sessionCount = 0;
                    }
                    else {
                        startingTime = shortBreakTimeStart;
                    }
                }

                else
                {
                    mTimerType.setText("Focus");
                    startingTime = focusTimeStart;
                }
                timeRemaining = startingTime;
                updateCountDownText();


                mTimerRunning = false;
                mPlay.setImageResource(R.drawable.ic_playbutton);
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

        timeRemaining = startingTime;
        updateCountDownText();

    }

    // converting time remaining in Times and seconds and displaying it
    private  void updateCountDownText()
    {

        int min = (int) (timeRemaining/1000)/60;
        int sec = (int) (timeRemaining/1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", min, sec);
        mTextViewCountDown.setText(timeLeftFormatted);

    }

    private void loadData()
    {
        focusTimeStart = startingTime = convertTime(getSettingsData(sessionsKey, snTimeDef));
        shortBreakTimeStart = convertTime(getSettingsData(shortBreakKey, shrtBrkTimeDef));
        longBreakTimeStart = convertTime(getSettingsData(longBreakKey, lngBrkTimeDef));
    }

    public long convertTime(long time)
    {
        return time * 60000;
    }

    public long getSettingsData(String key, long defaultVal) {
        sharedPref = getApplicationContext().getSharedPreferences(settingsPrefs, Context.MODE_PRIVATE);

        return sharedPref.getLong(key, defaultVal);
    }

//Timer Code*/

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences(timerPrefs, MODE_PRIVATE);

        startingTime = prefs.getLong("sessionTime", focusTimeStart);
        sessionCount = prefs.getInt("sessionCount", 0);

        timeRemaining = prefs.getLong("sessionTime", focusTimeStart);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            timeRemaining = mEndTime - System.currentTimeMillis();

            if (timeRemaining < 0) {
                timeRemaining = 0;
                mTimerRunning = false;
                updateCountDownText();
                mPlay.setImageResource(R.drawable.ic_playbutton);
            }
            else {
                startTimer();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences(timerPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("sessionTime", timeRemaining);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.putInt("sessionCount", sessionCount);
        editor.apply();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}