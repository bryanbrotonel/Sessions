package info3245.sessions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Introduction2 extends AppCompatActivity {


    private Button skipButton1, contButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction2);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {

            startActivity(new Intent(Introduction2.this, Timer.class));


        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        skipButton1 = findViewById(R.id.skipButton1);
        contButton1 = findViewById(R.id.contButton1);


        skipButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Introduction2.this,Timer.class));

            }
        });

        contButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Introduction2.this,Timer.class));

            }
        });
    }
}