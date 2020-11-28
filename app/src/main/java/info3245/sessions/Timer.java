package info3245.sessions;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Timer extends Container {

    //Timer Code
    private  long startingTime;
    private  long focusTimeStart = 6000;
    private  long breakTimeStart = 5000;
    private TextView mTextViewCountDown;
    private Button mPlay,mReset, mFocus, mBreak;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private long timeRemaining;
    private long mCurrentTime;


//Timer Code



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Timer Code

        //initializing values
        startingTime = focusTimeStart;
        timeRemaining = startingTime;
        mTextViewCountDown = findViewById(R.id.timer);
        mPlay = findViewById(R.id.playButton);
        mReset =findViewById(R.id.resetButton);
        mFocus = findViewById(R.id.focusButton);
        mBreak = findViewById(R.id.breakButton);

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

        // When Focus Button is clicked,  timer reset and will change the Starting Time to what was set in Focus
        mFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startingTime = focusTimeStart;
                resetTimer();

            }
        });

        // When Focus Button is clicked,  timer reset and will change the Starting Time to what was set in Break
        mBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startingTime = breakTimeStart;
                resetTimer();

            }
        });
//Timer Code



    }

    // Navigation Methods

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
        //used for background calculation
        mCurrentTime = System.currentTimeMillis() + timeRemaining;

        //Countdown Class is used to do the timer countdown
        countDownTimer = new CountDownTimer(timeRemaining,1000) {
            @Override
            //Timer will tickdown and will update the textview
            public void onTick(long l) {

                timeRemaining = l;
                updateCountDownText();

            }

            @Override
            //When Timer reaches Zero
            public void onFinish() {

                mTimerRunning = false;
                mPlay.setText("Start");
            }
        }.start();
        mTimerRunning = true;
        mPlay.setText("Pause");

    }

    // Pausing the Timer
    private void pauseTimer()
    {
        countDownTimer.cancel();
        mTimerRunning = false;
        mPlay.setText("Start");

    }

    // Reset The Timer back to starting time
    private void resetTimer()
    {
        if(mTimerRunning==true)
            pauseTimer();

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



    // Background running Coding

    // Upon switching activities or exiting the app, the app will store these data and stop the timer
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", timeRemaining);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mCurrentTime);
        editor.putLong("startingTime", startingTime);
        editor.apply();

        // if countdown is still active, stop it
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    //Upon switching back to the timer activity, a calculation is made to the timer to get the current time.
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        timeRemaining = prefs.getLong("millisLeft", startingTime);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        startingTime = prefs.getLong("startingTime", startingTime);
        updateCountDownText();


        if (mTimerRunning) {
            mCurrentTime = prefs.getLong("endTime", 0);

            //calc to get the current time
            timeRemaining = mCurrentTime- System.currentTimeMillis();

            //When the calculation reaches < 0, timer will make itself 0
            if (timeRemaining < 0) {
                timeRemaining = 0;
                mTimerRunning = false;
                updateCountDownText();
                mPlay.setText("Start");

            } else {
                startTimer();
            }


        }
        // When reset button is clicked, timer reset back to Starting Time
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }

        });
    }
}
