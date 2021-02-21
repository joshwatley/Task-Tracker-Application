package com.example.goaltrackerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandedGoal extends AppCompatActivity {
    GoalModel CURRENT_GOAL;
    private TextView goalTitle;
    private TextView goalDesc;
    private TextView goalDate;
    private TextView goalTime;
    private TextView goalNotif;
    private TextView goalComplet;
    private Button goalComplete;
    private Button goalInprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_goal);

        goalTitle = findViewById(R.id.tvGoalTitle);
        goalDesc = findViewById(R.id.tvGoalDesc);
        goalDate = findViewById(R.id.tvGoalEndDate);
        goalTime = findViewById(R.id.tvGoalEndTime);
        goalNotif = findViewById(R.id.tvGoalNotif);
        goalComplet = findViewById(R.id.tvGoalCompl);
        goalComplete = findViewById(R.id.btnCompleteGoal);
        goalInprogress = findViewById(R.id.btnInProgressGoal);

        // get intent string from the goal text
        String goal_Title = getIntent().getStringExtra("GOAL_TITLE");

        // get data from the database
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        CURRENT_GOAL = dataBaseHelper.getGoalData(goal_Title);

        goalTitle.setText(CURRENT_GOAL.getTitle());
        goalDesc.setText(CURRENT_GOAL.getDesc());
        goalDate.setText(CURRENT_GOAL.getDateend());
        goalTime.setText(CURRENT_GOAL.getTimeend());
        if (String.valueOf(CURRENT_GOAL.getNotification()).equals("1")){
            goalNotif.setText("Notifications are ON");
        }else {
            goalNotif.setText("Notifications are OFF");
        }

        if (String.valueOf(CURRENT_GOAL.getCompleteness()).equals("0")){
            goalComplet.setText("Not Started");
        }else if (String.valueOf(CURRENT_GOAL.getCompleteness()).equals("1")){
            goalComplet.setText("Complete");
        }else if (String.valueOf(CURRENT_GOAL.getCompleteness()).equals("2")){
            goalComplet.setText("In Progress");

        }
        // display the data from the database onto the form to see the data


        // complete goal event, update the database of that goal using CURRENT_GOAL

        goalComplete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            DataBaseHelper dataBaseHelper1 = new DataBaseHelper(getApplicationContext());
            if (dataBaseHelper1.completeGoal(CURRENT_GOAL.getTitle())){
                Toast.makeText(ExpandedGoal.this, "Well Done for completing goal", Toast.LENGTH_LONG).show();
                finish();
            }
            }
        });

        goalInprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper1 = new DataBaseHelper(getApplicationContext());
                if (dataBaseHelper1.inProgressGoal(CURRENT_GOAL.getTitle())){
                    Toast.makeText(ExpandedGoal.this, "Well Done for starting a goal", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        


    }
}
