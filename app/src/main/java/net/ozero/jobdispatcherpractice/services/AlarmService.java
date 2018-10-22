package net.ozero.jobdispatcherpractice.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import net.ozero.jobdispatcherpractice.services.activities.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmService extends IntentService {
    public AlarmService() {
        super("alarmService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            int id = intent.getIntExtra(MainActivity.EXTRA_ID, -999);
            int seconds = intent.getIntExtra(MainActivity.EXTRA_TIME, -9999);
            setAlarm(id, seconds);
        }
    }

    private void setAlarm(int id, int seconds) {
        Bundle extras = new Bundle();
        extras.putInt(MainActivity.EXTRA_ID, id);
        extras.putInt(MainActivity.EXTRA_TIME, seconds);
        extras.putString(MainActivity.EXTRA_SET_IN_TIME, getSetInTime());

        FirebaseJobDispatcher firebaseJobDispatcher =
                new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job job =
                firebaseJobDispatcher.newJobBuilder()
                        .setService(AlarmJobService.class)
                        .setTag(MainActivity.JOB_TAG + id)
                        .setLifetime(Lifetime.FOREVER)
                        .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                        .setTrigger(Trigger.executionWindow(seconds, seconds + MainActivity.TIMEOUT_IN_SECONDS))
                        .build();
        firebaseJobDispatcher.mustSchedule(job);
        Log.i(getClass().getName(), "alarm set");
    }

    private String getSetInTime() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        return dateFormat.format(date);
    }
}
