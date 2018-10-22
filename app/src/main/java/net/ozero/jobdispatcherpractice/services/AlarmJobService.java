package net.ozero.jobdispatcherpractice.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import net.ozero.jobdispatcherpractice.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.i(getClass().getName(), "onStartJob");

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Title")
                        .setContentText("Notification text");
        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(1, notification);
            jobFinished(job, false);
            return false;
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

    private String getMessage(int id, int seconds) {
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        String dateStr = dateFormat.format(date);

        return "ID: " + id + "; DELAY: " + seconds + " sec; " + "Set in: " + dateStr;
    }
}
