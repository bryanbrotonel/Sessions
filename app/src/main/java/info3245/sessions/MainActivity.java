package info3245.sessions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gets bottom navigation by ID
        bottomNavigationView = findViewById(R.id.bottomNav);

        // Creates new bottom navigation listener
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationMethod);

        // Sets default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new TimerFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationMethod=new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment fragment = null;

                    // Assgigns new fragment
                    switch (item.getItemId())
                    {
                        case R.id.timer:
                            fragment = new TimerFragment();
                            break;

                        case R.id.usage:
                            fragment = new UsageFragment();
                            break;

                        case R.id.settings:
                            fragment = new SettingsFramgent();
                            break;

                    }
                    // Replaces current fragment with new fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

                    return true;
                }
            };
}