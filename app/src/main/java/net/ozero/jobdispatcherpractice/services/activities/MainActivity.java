package net.ozero.jobdispatcherpractice.services.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import net.ozero.jobdispatcherpractice.R;
import net.ozero.jobdispatcherpractice.services.AlarmJobService;
import net.ozero.jobdispatcherpractice.services.AlarmService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //preferences
    private static final String PREFERENCES_SESSION = "session";
    private static final String KEY_ID = "id";
    //extras
    public static final String EXTRA_TIME = "seconds";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_MESSAGE = "message";
    //tags
    public static final String JOB_TAG = "alarm_job";
    //simple constants
    public static final int TIMEOUT_IN_SECONDS = 5;

    private EditText mDelayInput;
    private Button mSetAlarmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(getClass().getName(), "onCreate");
        initViews();
    }

    private void initViews() {
        mDelayInput = findViewById(R.id.delayInput);
        mSetAlarmButton = findViewById(R.id.buttonSetAlarm);
        String title = getResources().getString(R.string.title_button_set_alarm)+ " " + nextId();
        mSetAlarmButton.setText(title);
    }

    private int nextId() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_SESSION, MODE_PRIVATE);
        int id = preferences.getInt(KEY_ID, 1);
        preferences.edit().putInt(KEY_ID, id + 1).apply();
        return id;
    }

    public void setAlarmButtonClicked(View view) {
        int seconds = Integer.parseInt(mDelayInput.getText().toString());
        int id = nextId() - 1;
        String title = getResources().getString(R.string.title_button_set_alarm) + " " + (id + 1);
        mSetAlarmButton.setText(title);

        setJobInService(id, seconds);
    }

    private String getMessage(int id, int seconds) {
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        String dateStr = dateFormat.format(date);

        return "ID: " + id + "; DELAY: " + seconds + " sec; " + "Set in: " + dateStr;
    }

    private  void  setJobInService(int id, int seconds) {
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_TIME, seconds);
        intent.putExtra(EXTRA_MESSAGE, getMessage(id, seconds));
        startService(intent);

        Toast.makeText(this, "Alarm set, id: " + id, Toast.LENGTH_LONG).show();
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
