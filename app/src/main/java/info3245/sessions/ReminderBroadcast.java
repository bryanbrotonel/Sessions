package info3245.sessions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //We build our notification prompt here and assign it a channel ID for future reference
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyPomodoro")
        .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
        .setContentTitle("Pomodoro Reminder")
        .setContentText("Your session has ended")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //A notification manager is used to to notify when called.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify( 200, builder.build());
    }
}
