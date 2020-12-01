package info3245.sessions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Introduction2 extends AppCompatActivity {


    private Button skipButton1, contButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction2);

        // This piece of code make sure that Intro and Intro 2 are only run once after installation
        // uses SharedPreferences to save data
        SharedPreferences prefs = getSharedPreferences("Preference", MODE_PRIVATE);
        if (!prefs.getBoolean("firstTime", false)) {

            startActivity(new Intent(Introduction2.this, Timer.class));

            // mark first time has ran.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        skipButton1 = findViewById(R.id.skipButton1);
        contButton1 = findViewById(R.id.contButton1);

        // Skip Button go straight to the timer
        skipButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Introduction2.this,Timer.class));

            }
        });

        // Cont Button used to go to the second introduction page
        contButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Introduction2.this,Timer.class));

            }
        });
    }
}