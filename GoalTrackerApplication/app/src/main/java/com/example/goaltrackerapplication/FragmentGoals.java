package com.example.goaltrackerapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentGoals extends Fragment implements View.OnClickListener {
    LinearLayout linear; // layout of the form
    SQLiteDatabase mydatabase;
    View view;
    Button addGoal; // add goal button
    Button[] buttons; // create a list of buttons

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.goals_fragment,container,false);

        loadGoals(view); // load the buttons for the goals from db

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        final SwipeRefreshLayout pullToRefresh = getView().findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadGoals(view);
                if (pullToRefresh.isRefreshing()) {
                    pullToRefresh.setRefreshing(false);
                }
            }
        });

    }


    public void loadGoals(View view){

        linear = view.findViewById(R.id.fragmentgoals);
        linear.removeAllViews(); // every time you call this ensuring its clear of all items

        List<String> goals = new ArrayList<>();
        // loading goals from database to the application screen

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        goals = dataBaseHelper.getGoalsTitleForUser(MainHome.getID());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); // size of buttons
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(300,300);
        params.setMargins(0, 40, 0, 0);
        params2.setMargins(0, 40, 0, 0);

        buttons = new Button[goals.size()];
        for (int i = 0; i < goals.size(); i++){
            buttons[i] = new Button(this.getContext());
            buttons[i].setLayoutParams(params);
            buttons[i].setPadding(0,150,0,150);
            buttons[i].setGravity(Gravity.CENTER);
            buttons[i].setText(goals.get(i));
            buttons[i].setBackgroundResource(R.drawable.btn_back);
            buttons[i].setTextSize(25);
            buttons[i].setTextColor(Color.WHITE);
            buttons[i].setTransformationMethod(null);
            linear.addView(buttons[i]);
        }


        addGoal = new Button(this.getContext());
        addGoal.setLayoutParams(params2);
        addGoal.setBackgroundResource(R.drawable.btn_back);
        addGoal.setGravity(Gravity.CENTER);
        addGoal.setTextSize(50);
        addGoal.setText("+");
        addGoal.setTextColor(Color.WHITE);
        linear.addView(addGoal);

        if (buttons == null){
        }else{
            for (final Button b : buttons){
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // clicking a button
                        // this will get the text of the button -> b.getText();
                        Intent intent = new Intent(getActivity(), ExpandedGoal.class);
                        intent.putExtra("GOAL_TITLE", b.getText());
                        startActivity(intent);
                    }
                });
            }
        }

        addGoal.setOnClickListener(new View.OnClickListener() { // CREATING GOAL IN NEW ACTIVITY
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNewGoal.class);
                intent.putExtra("SESSION_USER_ID", MainHome.getID());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
