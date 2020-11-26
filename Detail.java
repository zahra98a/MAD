package edu.zahra.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import java.util.Date;

public class Detail extends AppCompatActivity {

    TextView RunDate, step, time, metre, calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        RunDate = findViewById(R.id.tvDate);
        step = findViewById(R.id.tvStep);
        time = findViewById(R.id.tvTime);
        metre = findViewById(R.id.tvMetres);
        calories = findViewById(R.id.tvCalories);

        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
        RunDate.setText(String.valueOf(s));

        float uStep = getIntent().getFloatExtra("userStep", -1);
        float uTime = getIntent().getFloatExtra("userTime", -1);

        double metresRun =  uStep * 0.8;
        double caloriesBurned  =  uStep * 0.04;

        step.setText(String.valueOf(uStep));
        time.setText(String.valueOf(uTime));
        metre.setText(String.valueOf(metresRun));
        calories.setText(String.valueOf(caloriesBurned));
    }
    public void doReturn(View view) {
        //switch to First page
        Intent mainActivity = new Intent(view.getContext(), MainActivity.class);
        startActivity(mainActivity);
    }
}