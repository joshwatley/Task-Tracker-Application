package com.example.goaltrackerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class AddNewGoal extends AppCompatActivity {
    private Button btnSaveGoal;
    private EditText etGoalTitle;
    private EditText etGoalDesc;
    private EditText etGoalDate;
    private EditText etGoalTime;
    private Switch sNotifcation;
    private GoalModel goalModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewgoal);

        // initialising all of this
        btnSaveGoal = findViewById(R.id.btnSaveGoal);
        etGoalTitle = findViewById(R.id.etGoalTitle);
        etGoalDesc = findViewById(R.id.etGoalDesc);
        etGoalDate = findViewById(R.id.etGoalEndDate);
        etGoalTime = findViewById(R.id.etGoalEndTime);
        sNotifcation = findViewById(R.id.switch_GoalNotification);

        btnSaveGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etGoalTime.getText().toString()) || TextUtils.isEmpty(etGoalDesc.getText().toString()) || TextUtils.isEmpty(etGoalDate.getText().toString()) || TextUtils.isEmpty(etGoalTime.getText().toString())) {
                    // if any of the fields are empty
                    Toast.makeText(AddNewGoal.this, "Please ensure all fields are completed", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        // evaluating the switch
                        int intnotif;
                        if (sNotifcation.isChecked()) {
                            intnotif = 1;
                        } else {
                            intnotif = 0;
                        }

                        goalModel = new GoalModel(-1, etGoalTitle.getText().toString(), etGoalDesc.getText().toString(), etGoalDate.getText().toString(), etGoalTime.getText().toString(), intnotif, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // add goal to the database and add a record for goal alloc between the two
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                    boolean success = dataBaseHelper.addGoal(goalModel, MainHome.getID());
                    if (success) {
                        Toast.makeText(AddNewGoal.this, "Goal Record Added Successfully - Pull Down to refresh!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(AddNewGoal.this, "Goal Record Fail", Toast.LENGTH_LONG).show();
                    }



                }
            }
        });


    }
}
