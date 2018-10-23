package net.ozero.jobdispatcherpractice.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import net.ozero.jobdispatcherpractice.R;
import net.ozero.jobdispatcherpractice.services.activities.MainActivity;

public class AlarmJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.i(getClass().getName(), "onStartJob");

        Bundle extras = job.getExtras();
        if (extras != null) {
            int id = extras.getInt(MainActivity.EXTRA_ID);
            int seconds = extras.getInt(MainActivity.EXTRA_TIME);
            String setInTime = extras.getString(MainActivity.EXTRA_SET_IN_TIME);

            NotificationCompat.Builder builder;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                builder = new NotificationCompat.Builder(
                        this,"com.ozero.jobdispatcher.notificationchannel");
            } else {
                builder = new NotificationCompat.Builder(this);
            }
            builder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getTitle(id, seconds))
                    .setContentText(getMessage(setInTime))
                    .setAutoCancel(false);
            Notification notification = builder.build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.notify(id, notification);
                jobFinished(job, false);
                return false;
            } else {
                jobFinished(job, true);
                return false;
            }
        } else {
            jobFinished(job, true);
            return false;
        }
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.i(getClass().getName(), "onStopJob");
        return true;
    }

    private String getMessage(String setInTime) {
        return "Set in: " + setInTime;
    }
    private String getTitle(int id, int delay) {
        return "ID: " + id + "; DELAY: " + delay + " sec";
    }
}
