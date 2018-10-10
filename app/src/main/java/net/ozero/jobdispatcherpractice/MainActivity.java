package net.ozero.jobdispatcherpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import net.ozero.jobdispatcherpractice.services.AlarmJobService;


public class MainActivity extends AppCompatActivity {

    public static final String JOB_TAG = "alarm_job";
    public static final int TIMEOUT_IN_SECONDS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(getClass().getName(), "onCreate");
    }


    public void setAlarmButtonClicked(View view) {

        setJob();

        Log.i(getClass().getName(), "job set");
    }

    private void setJob() {

        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(
                new GooglePlayDriver(this));



        Job job =
                firebaseJobDispatcher.newJobBuilder()
                .setService(AlarmJobService.class)
                .setTag(JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(TIMEOUT_IN_SECONDS, TIMEOUT_IN_SECONDS + 10))
                .build();

        firebaseJobDispatcher.mustSchedule(job);
    }
}
