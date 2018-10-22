package net.ozero.jobdispatcherpractice.services.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import net.ozero.jobdispatcherpractice.R;
import net.ozero.jobdispatcherpractice.services.AlarmJobService;
import net.ozero.jobdispatcherpractice.services.AlarmService;

public class MainActivity extends AppCompatActivity {

    public static final String JOB_TAG = "alarm_job";
    public static final int TIMEOUT_IN_SECONDS = 5;
    public static final String EXTRA_TIME = "seconds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(getClass().getName(), "onCreate");
    }

    public void setAlarmButtonClicked(View view) {
        int seconds = 0;

        switch (view.getId()) {
            case R.id.buttonSet10secAlarm:
                seconds = 10;
                break;
            case R.id.buttonSet1MinAlarm:
                seconds = 60;
                break;
        }
        setJobHere(seconds);
    }

    private  void  setJobInService(int seconds) {
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra(EXTRA_TIME, seconds);
        startService(intent);
    }

    private void setJobHere(int seconds) {
        FirebaseJobDispatcher firebaseJobDispatcher =
                new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job job =
                firebaseJobDispatcher.newJobBuilder()
                        .setService(AlarmJobService.class)
                        .setTag(JOB_TAG)
                        .setLifetime(Lifetime.FOREVER)
                        .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                        .setTrigger(Trigger.executionWindow(seconds, seconds + TIMEOUT_IN_SECONDS))
                        .build();
        firebaseJobDispatcher.mustSchedule(job);
        Log.i(getClass().getName(), "alarm set");
    }
}
