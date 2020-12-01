package info3245.sessions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Introduction extends AppCompatActivity {

    private Button skipButton, contButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        skipButton = findViewById(R.id.skipButton);
        contButton = findViewById(R.id.contButton);



        // This piece of code make sure that Intro and Intro 2 are only run once after installation
        // uses SharedPreferences to save data
        SharedPreferences prefs = getSharedPreferences("Preference", MODE_PRIVATE);
        if (!prefs.getBoolean("firstTime", false)) {

            startActivity(new Intent(Introduction.this, Timer.class));

            // mark first time has ran.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }


        // Skip Button go straight to the timer
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Introduction.this, Timer.class));

            }
        });

        // Cont Button used to go to the second introduction page
        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Introduction.this,Introduction2.class));

            }
        });


    }
}