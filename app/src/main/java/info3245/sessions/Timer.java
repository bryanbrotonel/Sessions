package info3245.sessions;

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
    private TextView mTextViewCountDown, mTimerType;
    private Button mPlay,mReset, mRestart;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private long timeRemaining;


//Timer Code



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Timer Code
        startingTime = focusTimeStart;
        timeRemaining = startingTime;
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
                    startingTime = breakTimeStart;
                }

                else
                {
                    mTimerType.setText("Focus");
                    startingTime = focusTimeStart;
                }
                timeRemaining = startingTime;
                updateCountDownText();


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

//Timer Code*/

}