package info3245.sessions;

import android.os.Bundle;

public class Usage extends Container {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Returns layout ID to Container Class
    @Override
    int getLayoutId() {
        return R.layout.activity_usage;
    }

    // Returns navigation ID to Container Class
    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.nav_usage;
    }
}