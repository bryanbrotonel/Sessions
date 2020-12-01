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

    private static final String Usage_Today = "Usage_Today" ;
    private static final String Usage_Yesterday = "Usage_Yesterday" ;
    private static final String Usage_1Day = "Usage_1Day" ;
    private static final String Usage_2Day = "Usage_2Day" ;
    private static final String Usage_3Day = "Usage_3Day" ;

    private static final String Date = "dateKey";
    private static final String Sessions = "sessionsKey";
    private static final String Usage = "usageKey";
    private static final String Goal = "goalKey";

    private final String defaultString = "-";
    private final int defaultInt = 0;

    private int usage_Usage_Today = defaultInt;
    private int sessions_Usage_Today = defaultInt;

    private String date_Usage_1Day = defaultString;
    private int usage_Usage_1Day = defaultInt;
    private int sessions_Usage_1Day = defaultInt;
    private int goal_Usage_1Day = defaultInt;

    private String date_Usage_2Day = defaultString;
    private int usage_Usage_2Day = defaultInt;
    private int sessions_Usage_2Day = defaultInt;
    private int goal_Usage_2Day = defaultInt;

    private String date_Usage_3Day = defaultString;
    private int usage_Usage_3Day = defaultInt;
    private int sessions_Usage_3Day = defaultInt;
    private int goal_Usage_3Day = defaultInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restoreUsageData();
        updateUsageData();

        displayUsageData();

        SharedPreferences sharedPref = getSharedPreferences(Usage_Yesterday, Context.MODE_PRIVATE);

        Log.d("Usage_Yesterday", String.valueOf(sharedPref.getAll()));

        sharedPref = getSharedPreferences(Usage_Today, Context.MODE_PRIVATE);

        Log.d("Usage_Today", String.valueOf(sharedPref.getAll()));

    }

    @Override
    public void onPause() {
        super.onPause();

        writeUsageData(Usage_1Day, date_Usage_1Day, usage_Usage_1Day, sessions_Usage_1Day, goal_Usage_1Day);
        writeUsageData(Usage_2Day, date_Usage_2Day, usage_Usage_2Day, sessions_Usage_2Day, goal_Usage_2Day);
        writeUsageData(Usage_3Day, date_Usage_3Day, usage_Usage_3Day, sessions_Usage_3Day, goal_Usage_3Day);

    }

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

    private void updateUsageData() {

        sharedPref = getSharedPreferences(Usage_1Day, Context.MODE_PRIVATE);
        String date = sharedPref.getString(Date, defaultString);

//        if (!date.equals(defaultString) && date.equals(getYesterdayDate())) {

            //TODO: Update new values

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

            // Get new values
            date_Usage_1Day = getUsageData(Usage_Yesterday, Date);
            usage_Usage_1Day = Integer.parseInt(getUsageData(Usage_Yesterday, Usage));
            sessions_Usage_1Day = Integer.parseInt(getUsageData(Usage_Yesterday, Sessions));
            goal_Usage_1Day = Integer.parseInt(getUsageData(Usage_Yesterday, Goal));

//        }

    }

    private void restoreUsageData() {

        usage_Usage_Today = Integer.parseInt(getUsageData(Usage_Today, Usage));
        sessions_Usage_Today = Integer.parseInt(getUsageData(Usage_Today, Sessions));

        // Display Usage_1Day data
        date_Usage_1Day = getUsageData(Usage_1Day, Date);
        usage_Usage_1Day = Integer.parseInt(getUsageData(Usage_1Day, Usage));
        sessions_Usage_1Day = Integer.parseInt(getUsageData(Usage_1Day, Sessions));
        goal_Usage_1Day = Integer.parseInt(getUsageData(Usage_1Day, Goal));

        // Display Usage_2Day data
        date_Usage_2Day = getUsageData(Usage_2Day, Date);
        usage_Usage_2Day = Integer.parseInt(getUsageData(Usage_2Day, Usage));
        sessions_Usage_2Day = Integer.parseInt(getUsageData(Usage_2Day, Sessions));
        goal_Usage_2Day = Integer.parseInt(getUsageData(Usage_2Day, Goal));

        // Display Usage_3Day data
        date_Usage_3Day = getUsageData(Usage_3Day, Date);
        usage_Usage_3Day = Integer.parseInt(getUsageData(Usage_3Day, Usage));
        sessions_Usage_3Day = Integer.parseInt(getUsageData(Usage_3Day, Sessions));
        goal_Usage_3Day = Integer.parseInt(getUsageData(Usage_3Day, Goal));

    }

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

    private String getUsageData(String usageDay, String data) {

        SharedPreferences sharedPref = getSharedPreferences(usageDay, Context.MODE_PRIVATE);

        return data.equals(Date) ? sharedPref.getString(data, "-") : String.valueOf(sharedPref.getInt(data, defaultInt));
    }

    private void displayData(int ID, String value) {
        TextView test = findViewById(ID);
        test.setText(value);
    }

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