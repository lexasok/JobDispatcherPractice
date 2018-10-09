package net.ozero.jobdispatcherpractice.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import net.ozero.jobdispatcherpractice.R;

public class AlarmJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Title")
                        .setContentText("Notification text");

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(1, notification);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;


    }
}
