package com.example.goaltrackerapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentBreakdown extends Fragment {
    View view;
    Button[] buttons;
    PieChart pieChart;
    int[]colorClassArray = new int[]{Color.rgb(35, 17, 20), Color.rgb(80,27,29), Color.rgb(100, 72, 92)};
    int[]colorClassArray1 = new int[]{Color.RED, Color.BLUE, Color.GREEN};

    public FragmentBreakdown() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.breakdown_fragment,container,false);

        loadPieChart(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        final SwipeRefreshLayout pullToRefresh = getView().findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPieChart(view);
                if (pullToRefresh.isRefreshing()) {
                    pullToRefresh.setRefreshing(false);
                }
            }
        });
    }

    private ArrayList<PieEntry>datavalues1(){
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());

        dataVals.add(new PieEntry(Float.parseFloat(dataBaseHelper.getCountOfInProgress(MainHome.getID())), "In Progress"));
        dataVals.add(new PieEntry(Float.parseFloat(dataBaseHelper.getCountOfCompleted(MainHome.getID())), "Completed"));
        dataVals.add(new PieEntry(Float.parseFloat(dataBaseHelper.getCountOfNotStarted(MainHome.getID())), "To Do"));

        return dataVals;

    }

    private void loadPieChart(final View Inview){

        pieChart = Inview.findViewById(R.id.piechart);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        String number = dataBaseHelper.totalGoals(MainHome.getID());

        PieDataSet pieDataSet = new PieDataSet(datavalues1(), "");
        pieDataSet.setColors(colorClassArray);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setUsePercentValues(false);
        pieChart.setHoleRadius(30);
        pieChart.setCenterText("TOTAL GOALS: " + number);
        pieChart.setTransparentCircleRadius(40);
        pieChart.setEntryLabelTextSize(20);
        pieData.setValueTextSize(15);
        pieChart.getDescription().setEnabled(false);

        pieChart.setData(pieData);
        pieChart.invalidate();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                switch (pe.getLabel()) {
                    case "Completed":
                        loadCompletedGoals(Inview);
                        break;

                    case "In Progress":
                        loadInProgressGoals(Inview);
                        break;

                    case "To Do":
                        loadToDoGoals(Inview);
                        break;
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        loadCompletedGoals(Inview);


    } // Loading pie chart on command

    private void loadCompletedGoals(View Inview){
        LinearLayout linear; // layout of the form
        linear = Inview.findViewById(R.id.bdgoals);
        linear.removeAllViews(); // every time you call this ensuring its clear of all items

        List<String> goals;
        // loading goals from database to the application screen

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        goals = dataBaseHelper.getCompletedGoals(MainHome.getID());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300); // size of buttons
        params.setMargins(0, 40, 0, 0);

        buttons = new Button[goals.size()];
        for (int i = 0; i < goals.size(); i++){
            buttons[i] = new Button(this.getContext());
            buttons[i].setLayoutParams(params);
            //buttons[i].setPadding(0,80,0,80);
            buttons[i].setGravity(Gravity.CENTER);
            buttons[i].setText(goals.get(i));
            buttons[i].setBackgroundResource(R.drawable.btn_back);
            buttons[i].setTextSize(25);
            buttons[i].setTextColor(Color.WHITE);
            buttons[i].setTransformationMethod(null);
            linear.addView(buttons[i]);
        }
        loadButtonListeners(buttons);
    }

    private void loadToDoGoals(View Inview){
        LinearLayout linear; // layout of the form
        linear = Inview.findViewById(R.id.bdgoals);
        linear.removeAllViews(); // every time you call this ensuring its clear of all items

        List<String> goals;
        // loading goals from database to the application screen

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        goals = dataBaseHelper.getToDoGoals(MainHome.getID());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        params.setMargins(0, 40, 0, 0);

        buttons = new Button[goals.size()];
        for (int i = 0; i < goals.size(); i++){
            buttons[i] = new Button(this.getContext());
            buttons[i].setLayoutParams(params);
            //buttons[i].setPadding(0,80,0,80);
            buttons[i].setGravity(Gravity.CENTER);
            buttons[i].setText(goals.get(i));
            buttons[i].setBackgroundResource(R.drawable.btn_back);
            buttons[i].setTextSize(25);
            buttons[i].setTextColor(Color.WHITE);
            buttons[i].setTransformationMethod(null);
            linear.addView(buttons[i]);
        }

        loadButtonListeners(buttons);
    }

    private void loadInProgressGoals(final View Inview){
        LinearLayout linear; // layout of the form
        linear = Inview.findViewById(R.id.bdgoals);
        linear.removeAllViews(); // every time you call this ensuring its clear of all items

        List<String> goals;
        // loading goals from database to the application screen

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        goals = dataBaseHelper.getInProgressGoals(MainHome.getID());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        params.setMargins(0, 40, 0, 0);

        buttons = new Button[goals.size()];
        for (int i = 0; i < goals.size(); i++){
            buttons[i] = new Button(this.getContext());
            buttons[i].setLayoutParams(params);
            //buttons[i].setPadding(0,80,0,80);
            buttons[i].setGravity(Gravity.CENTER);
            buttons[i].setText(goals.get(i));
            buttons[i].setBackgroundResource(R.drawable.btn_back);
            buttons[i].setTextSize(25);
            buttons[i].setTextColor(Color.WHITE);
            buttons[i].setTransformationMethod(null);
            linear.addView(buttons[i]);
        }

        loadButtonListeners(buttons);
    }

    private void loadButtonListeners(Button[] buttons){
        if (buttons == null){
        }else{
            for (final Button b : buttons){
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // clicking a button
                        Intent intent = new Intent(getActivity(), ExpandedGoal.class);
                        intent.putExtra("GOAL_TITLE", b.getText());
                        startActivity(intent);
                    }
                });
            }
        }
    }

}