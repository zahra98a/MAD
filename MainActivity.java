package edu.zahra.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    // experimental values for hi and lo magnitude limits
    private final double HI_STEP = 9.9;  // upper mag limit
    private final double LO_STEP = 9.85; // lower mag limit
    boolean highLimit = false;  // detect high limit
    int counter = 0; // step counter
    private SensorManager mSensorManager;
    private Sensor mSensor;
    CountUpTimer timer;
    TextView StepsCounter, TimeCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimeCounter = findViewById(R.id.tvCountTime);
        StepsCounter = findViewById(R.id.tvCountSteps);

        timer = new CountUpTimer(300000) {  // should be high for the run (ms)
            public void onTick(int second) {
                TimeCounter.setText(String.valueOf(second));
            }
        };

        // for using the sensor service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    public void doStart(View view) {
        timer.start();
        Toast.makeText(this, "Started counting", Toast.LENGTH_LONG).show();
    }
    public void doStop(View view) {
        timer.cancel();
        Toast.makeText(this, "Stopped Run", Toast.LENGTH_LONG).show();
    }
    public void doReset(View view) {
        TimeCounter.setText("0");
        timer.start();
        Toast.makeText(this, "Reset", Toast.LENGTH_LONG).show();
    }
    protected void onResume() {//When the app is brought to the foreground - using app on screen
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause() { //App running but not on screen - in the background
        super.onPause();
        mSensorManager.unregisterListener(this);    // turn off listener to save power
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        // get a magnitude number using Pythagorus's Theorem
        double mag = round(Math.sqrt((x*x) + (y*y) + (z*z)), 2);

        // if msg > 9.9 and then drops below 9.85 create a step
        if ((mag > HI_STEP) && (highLimit == false)) {
            highLimit = true;
        }
        if ((mag < LO_STEP) && (highLimit == true)) {// we have a step
            counter++;
            StepsCounter.setText(String.valueOf(counter));
            highLimit = false;
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    public void doDetails(View view) {
        //switch to detail Activity
        Intent detailActivity = new Intent(view.getContext(), Detail.class);

        float NumberOfSteps = Float.parseFloat(StepsCounter.getText().toString());
        float NumberOfSeconds = Float.parseFloat(TimeCounter.getText().toString());

       detailActivity.putExtra("userStep",NumberOfSteps);
       detailActivity.putExtra("userTime",NumberOfSeconds);

       startActivity(detailActivity); // start the second activity
    }
}