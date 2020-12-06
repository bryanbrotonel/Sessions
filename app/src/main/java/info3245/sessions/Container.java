package info3245.sessions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class Container extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

    }

    // Updates navbar state on start
    @Override
    protected void onStart()
    {
        super.onStart();
        updateNavigationBarState();
    }

    // Removes transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {

        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_usage) {
                startActivity(new Intent(this, Usage.class));
            } else if (itemId == R.id.nav_timer) {
                startActivity(new Intent(this, Timer.class));
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(this, Settings.class));
            }
            finish();
        }, 300);
        return true;
    }

    // Updates navigation bar state
    private void updateNavigationBarState() {
        int actionId = getBottomNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    // Returns which layout (activity) needs to display when clicked on tabs.
    abstract int getLayoutId();

    //Which menu item selected and change the state of that menu item
    abstract int getBottomNavigationMenuItemId();


}