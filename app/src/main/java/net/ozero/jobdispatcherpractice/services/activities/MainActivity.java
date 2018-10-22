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
import com.firebase.jobdispatcher.Trigger;

import net.ozero.jobdispatcherpractice.R;
import net.ozero.jobdispatcherpractice.services.AlarmJobService;
import net.ozero.jobdispatcherpractice.services.AlarmService;

public class MainActivity extends AppCompatActivity {

    public static final String JOB_TAG = "alarm_job";
    public static final int TIMEOUT_IN_SECONDS = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(getClass().getName(), "onCreate");
    }

    public void setAlarmButtonClicked(View view) {
        setJobHere();
    }

    private  void  setJobInService() {
        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
    }

    private void setJobHere() {
        FirebaseJobDispatcher firebaseJobDispatcher =
                new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job job =
                firebaseJobDispatcher.newJobBuilder()
                        .setService(AlarmJobService.class)
                        .setTag(JOB_TAG)
                        .setLifetime(Lifetime.FOREVER)
                        .setTrigger(Trigger.executionWindow(TIMEOUT_IN_SECONDS, TIMEOUT_IN_SECONDS + 1))
                        .build();
        firebaseJobDispatcher.mustSchedule(job);
    }
}
