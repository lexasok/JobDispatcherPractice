package net.ozero.jobdispatcherpractice.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import net.ozero.jobdispatcherpractice.services.activities.MainActivity;

public class AlarmService extends Service {
    public AlarmService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        setAlarm();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void setAlarm() {
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(
                new GooglePlayDriver(this));

        firebaseJobDispatcher.cancelAll();

        Job job =
                firebaseJobDispatcher.newJobBuilder()
                        .setService(AlarmJobService.class)
                        .setTag("123555553233")
                        .setLifetime(Lifetime.FOREVER)
                        .setTrigger(Trigger.executionWindow(MainActivity.TIMEOUT_IN_SECONDS, MainActivity.TIMEOUT_IN_SECONDS + 1))
                        .build();

        firebaseJobDispatcher.mustSchedule(job);

        Log.i(getClass().getName(), "alarm set");
    }
}
