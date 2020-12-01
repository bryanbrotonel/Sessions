package info3245.sessions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Usage extends Container {

    SharedPreferences sharedPref;

    // Usage Shared prefs files
    private static final String Usage_Today = "Usage_Today" ;
    private static final String Usage_Yesterday = "Usage_Yesterday" ;
    private static final String Usage_1Day = "Usage_1Day" ;
    private static final String Usage_2Day = "Usage_2Day" ;
    private static final String Usage_3Day = "Usage_3Day" ;

    //Usage value keys
    private static final String Date = "dateKey";
    private static final String Sessions = "sessionsKey";
    private static final String Usage = "usageKey";
    private static final String Goal = "goalKey";

    //Usage default values
    private final String defaultString = "-";
    private final int defaultInt = 0;

    // Today usage values
    private int usage_Usage_Today = defaultInt;
    private int sessions_Usage_Today = defaultInt;

    // 1Day usage values
    private String date_Usage_1Day = defaultString;
    private int usage_Usage_1Day = defaultInt;
    private int sessions_Usage_1Day = defaultInt;
    private int goal_Usage_1Day = defaultInt;

    // 2Day usage values
    private String date_Usage_2Day = defaultString;
    private int usage_Usage_2Day = defaultInt;
    private int sessions_Usage_2Day = defaultInt;
    private int goal_Usage_2Day = defaultInt;

    // 3Day usage values
    private String date_Usage_3Day = defaultString;
    private int usage_Usage_3Day = defaultInt;
    private int sessions_Usage_3Day = defaultInt;
    private int goal_Usage_3Day = defaultInt;


    // Initializes usage data on Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restoreUsageData();
        updateUsageData();
        displayUsageData();

    }

    // Writes all shared data to Shared prefs on pause
    @Override
    public void onPause() {
        super.onPause();

        writeUsageData(Usage_1Day, date_Usage_1Day, usage_Usage_1Day, sessions_Usage_1Day, goal_Usage_1Day);
        writeUsageData(Usage_2Day, date_Usage_2Day, usage_Usage_2Day, sessions_Usage_2Day, goal_Usage_2Day);
        writeUsageData(Usage_3Day, date_Usage_3Day, usage_Usage_3Day, sessions_Usage_3Day, goal_Usage_3Day);

    }

    // Writes usage data to Shared prefs file
    private void writeUsageData(String file, String date, int usage, int sessions, int goal) {

        // Write usageData data
        sharedPref = getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(Date, date);
        editor.putInt(Sessions, sessions);
        editor.putInt(Usage, usage);
        editor.putInt(Goal, goal);

        editor.apply();
    }

    // Updates old usage history data with new usage data when new data is available
    private void updateUsageData() {

        sharedPref = getSharedPreferences(Usage_Yesterday, Context.MODE_PRIVATE);
        String date = sharedPref.getString(Date, defaultString);

        // Verify yesterday's data's date
        if (!date.equals(defaultString) && date.equals(getYesterdayDate())) {

            // Replace old 3Day data with new 2Day data
            date_Usage_3Day = date_Usage_2Day;
            usage_Usage_3Day = usage_Usage_2Day;
            sessions_Usage_3Day = sessions_Usage_2Day;
            goal_Usage_3Day = goal_Usage_2Day;

            // Replace old 2Day data with new 1Day data
            date_Usage_2Day = date_Usage_1Day;
            usage_Usage_2Day = usage_Usage_1Day;
            sessions_Usage_2Day = sessions_Usage_1Day;
            goal_Usage_2Day = goal_Usage_1Day;

            // Add yesterday's data to history
            date_Usage_1Day = getUsageData(Usage_Yesterday, Date);
            usage_Usage_1Day = Integer.parseInt(getUsageData(Usage_Yesterday, Usage));
            sessions_Usage_1Day = Integer.parseInt(getUsageData(Usage_Yesterday, Sessions));
            goal_Usage_1Day = Integer.parseInt(getUsageData(Usage_Yesterday, Goal));

        }

    }

    // Restores usage data to Shared Prefs
    private void restoreUsageData() {

        // Fetch Today usage data
        usage_Usage_Today = Integer.parseInt(getUsageData(Usage_Today, Usage));
        sessions_Usage_Today = Integer.parseInt(getUsageData(Usage_Today, Sessions));

        // Fetch 1Day usage data
        date_Usage_1Day = getUsageData(Usage_1Day, Date);
        usage_Usage_1Day = Integer.parseInt(getUsageData(Usage_1Day, Usage));
        sessions_Usage_1Day = Integer.parseInt(getUsageData(Usage_1Day, Sessions));
        goal_Usage_1Day = Integer.parseInt(getUsageData(Usage_1Day, Goal));

        // Fetch 2Day usage data
        date_Usage_2Day = getUsageData(Usage_2Day, Date);
        usage_Usage_2Day = Integer.parseInt(getUsageData(Usage_2Day, Usage));
        sessions_Usage_2Day = Integer.parseInt(getUsageData(Usage_2Day, Sessions));
        goal_Usage_2Day = Integer.parseInt(getUsageData(Usage_2Day, Goal));

        // Fetch 3Day usage data
        date_Usage_3Day = getUsageData(Usage_3Day, Date);
        usage_Usage_3Day = Integer.parseInt(getUsageData(Usage_3Day, Usage));
        sessions_Usage_3Day = Integer.parseInt(getUsageData(Usage_3Day, Sessions));
        goal_Usage_3Day = Integer.parseInt(getUsageData(Usage_3Day, Goal));

    }

    // Display usage data in view
    private void displayUsageData() {

        //Display today's usage data
        displayData(R.id.data_todayUsage, String.valueOf(usage_Usage_Today));
        displayData(R.id.data_todaySessions, String.valueOf(sessions_Usage_Today));

        // Display Usage_1Day data
        displayData(R.id.usage_date1, date_Usage_1Day);
        displayData(R.id.usage_data1, String.valueOf(usage_Usage_1Day));
        displayData(R.id.sessions_data1, String.valueOf(sessions_Usage_1Day));
        displayData(R.id.goal_data1, String.valueOf(goal_Usage_1Day));

        // Display Usage_2Day data
        displayData(R.id.usage_date2, date_Usage_2Day);
        displayData(R.id.usage_data2, String.valueOf(usage_Usage_2Day));
        displayData(R.id.sessions_data2, String.valueOf(sessions_Usage_2Day));
        displayData(R.id.goal_data2, String.valueOf(goal_Usage_2Day));

        // Display Usage_3Day data
        displayData(R.id.usage_date3, date_Usage_3Day);
        displayData(R.id.usage_data3, String.valueOf(usage_Usage_3Day));
        displayData(R.id.sessions_data3, String.valueOf(sessions_Usage_3Day));
        displayData(R.id.goal_data3, String.valueOf(goal_Usage_3Day));
    }

    // Retrieves usage data from Shared pref
    private String getUsageData(String usageDay, String data) {

        SharedPreferences sharedPref = getSharedPreferences(usageDay, Context.MODE_PRIVATE);

        return data.equals(Date) ? sharedPref.getString(data, "-") : String.valueOf(sharedPref.getInt(data, defaultInt));
    }

    // Displays data to view
    private void displayData(int ID, String value) {
        TextView test = findViewById(ID);
        test.setText(value);
    }

    // Gets and formats yesterday's date
    private String getYesterdayDate() {

        String dateFormatString = "EEEE MMM d";

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, -1);

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);

        return dateFormat.format(calendar.getTime());
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