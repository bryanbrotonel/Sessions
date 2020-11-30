package info3245.sessions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {

            startActivity(new Intent(Introduction.this, Timer.class));

        }


        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();


        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Introduction.this, Timer.class));

            }
        });

        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Introduction.this,Introduction2.class));

            }
        });


    }
}