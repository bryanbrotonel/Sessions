package info3245.sessions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Settings extends Container{

    SharedPreferences sharedPref;

    private static final String settingsPrefs = "Settings_Prefs" ;

    // Settings keys;
    public static final String sessionsKey = "sessionsKey";
    private static final String goalKey = "goalKey";
    private static final String shortBreakKey = "shortBreakKey";
    private static final String longBreakKey = "longBreakKey";

    private static final String sessionsBtnKey = "sessionsBtnKey";
    private static final String goalBtnKey = "goalBtnKey";
    private static final String shortBreakBtnKey = "shortBreakBtnKey";
    private static final String longBreakBtnKey = "longBreakBtnKey";

    // Default values for settings
    private long snTimeDef = 20;
    private long goalTimeDef = 4;
    private long shrtBrkTimeDef = 2;
    private long lngBrkTimeDef = 20;

    // Default values for settings radio buttons
    private int snTimeBtnDef = R.id.sns20M;
    private int goalTimeBtnDef = R.id.goal_4h;
    private int shrtBrkTimeBtnDef = R.id.short_Brk2M;
    private int lngBrkTimeBtnDef = R.id.long_Brk20M;

    // Values for settings
    private long snTime = snTimeDef;
    private long goalTime = goalTimeDef;
    private long shrtBrkTime = shrtBrkTimeDef;
    private long lngBrkTime = lngBrkTimeDef;

    // Values for settings radio buttons
    private int snTimeBtn = snTimeBtnDef;
    private int goalTimeBtn = goalTimeBtnDef;
    private int shrtBrkTimeBtn = shrtBrkTimeBtnDef;
    private int lngBrkTimeBtn = lngBrkTimeBtnDef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        sharedPref = getApplicationContext().getSharedPreferences(settingsPrefs, Context.MODE_PRIVATE);

        restoreSettingsData();
        displaySettingsData();
        Log.d("Settings", String.valueOf(sharedPref.getAll()));
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong(sessionsKey, snTime);
        editor.putLong(goalKey, goalTime);
        editor.putLong(shortBreakKey, shrtBrkTime);
        editor.putLong(longBreakKey, lngBrkTime);

        editor.putInt(sessionsBtnKey, snTimeBtn);
        editor.putInt(goalBtnKey, goalTimeBtn);
        editor.putInt(shortBreakBtnKey, shrtBrkTimeBtn);
        editor.putInt(longBreakBtnKey, lngBrkTimeBtn);

        editor.apply();
    }

    private void restoreSettingsData() {

        snTime = getSettingsData(sessionsKey, snTimeDef);
        goalTime = getSettingsData(goalKey, goalTimeDef);
        shrtBrkTime = getSettingsData(shortBreakKey, shrtBrkTimeDef);
        lngBrkTime = getSettingsData(longBreakKey, lngBrkTimeDef);

        snTimeBtn  = getSettingsBtn(sessionsBtnKey, snTimeBtnDef);
        goalTimeBtn  = getSettingsBtn(goalBtnKey, goalTimeBtnDef);
        shrtBrkTimeBtn  = getSettingsBtn(shortBreakBtnKey, shrtBrkTimeBtnDef);
        lngBrkTimeBtn  = getSettingsBtn(longBreakBtnKey, lngBrkTimeBtnDef);

    }

    private void displaySettingsData() {
        int[] settingsBtns = {snTimeBtn, goalTimeBtn, shrtBrkTimeBtn, lngBrkTimeBtn};

        for (int button : settingsBtns) {
            RadioButton sessionBtn = findViewById(button);
            sessionBtn.setChecked(true);
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {

            // Session radio buttons
            case R.id.sns20M:
                if (checked)
                    snTime = 20;
                    snTimeBtn = R.id.sns20M;
                    break;
            case R.id.sns30M:
                if (checked)
                    snTime = 30;
                    snTimeBtn = R.id.sns30M;
                    break;
            case R.id.sns40M:
                if (checked)
                    snTime = 40;
                    snTimeBtn = R.id.sns40M;
                    break;
            case R.id.sns50M:
                if (checked)
                    snTime = 50;
                    snTimeBtn = R.id.sns50M;
                    break;

            // Goal radio button
            case R.id.goal_4h:
                if (checked)
                    goalTime = 4;
                    goalTimeBtn = R.id.goal_4h;
                break;
            case R.id.goal_5h:
                if (checked)
                    goalTime = 5;
                    goalTimeBtn = R.id.goal_5h;
                break;
            case R.id.goal_6h:
                if (checked)
                    goalTime = 6;
                    goalTimeBtn = R.id.goal_6h;
                break;
            case R.id.goal_7h:
                if (checked)
                    goalTime = 7;
                    goalTimeBtn = R.id.goal_7h;
                break;

            // Short break radio button
            case R.id.short_Brk2M:
                if (checked)
                    shrtBrkTime = 2;
                    shrtBrkTimeBtn = R.id.short_Brk2M;
                break;
            case R.id.short_Brk3M:
                if (checked)
                    shrtBrkTime = 3;
                    shrtBrkTimeBtn = R.id.short_Brk3M;
                break;
            case R.id.short_Brk5M:
                if (checked)
                    shrtBrkTime = 5;
                    shrtBrkTimeBtn = R.id.short_Brk5M;
                break;
            case R.id.short_Brk7M:
                if (checked)
                    shrtBrkTime = 7;
                    shrtBrkTimeBtn = R.id.short_Brk7M;
                break;

            // Long break radio button
            case R.id.long_Brk20M:
                if (checked)
                    lngBrkTime = 20;
                    lngBrkTimeBtn = R.id.long_Brk20M;
                break;
            case R.id.long_Brk25M:
                if (checked)
                    lngBrkTime = 25;
                    lngBrkTimeBtn = R.id.long_Brk25M;
                break;
            case R.id.long_Brk30M:
                if (checked)
                    lngBrkTime = 30;
                    lngBrkTimeBtn = R.id.long_Brk30M;
                break;
            case R.id.long_Brk35M:
                if (checked)
                    lngBrkTime = 35;
                    lngBrkTimeBtn = R.id.long_Brk35M;
                break;
        }
    }

    public long getSettingsData(String key, long defaultVal) {

        return sharedPref.getLong(key, defaultVal);
    }

    private int getSettingsBtn(String key, int defaultVal) {

        return sharedPref.getInt(key, defaultVal);
    }

    // Returns layout ID to Container Class
    @Override
    int getLayoutId() {
        return R.layout.activity_settings;
    }

    // Returns navigation ID to Container Class
    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.nav_settings;
    }

}