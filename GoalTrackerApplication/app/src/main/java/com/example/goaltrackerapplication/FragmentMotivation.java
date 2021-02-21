package com.example.goaltrackerapplication;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

public class FragmentMotivation extends Fragment{
    View view;
    Button newMotiv;
    TextView motiv;
    ImageView dailyremin;
    List<String> motivations = new ArrayList<>();
    Switch allow_notif;
    ImageButton ibshare;
    private ShareActionProvider mShareActionProvider;
    boolean mForecastStr;



    public FragmentMotivation() {
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.motivation_fragment,container,false);

        //YOU ONLY WANT THIS TO BE RUN ONCE WHEN CREATING THE APP FOR THE FIRST TIME FOR TESTING
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        if (dataBaseHelper.areThereMotivations()){
            // if there are motivations do nothing
        }else{
            // load them if there are none
            dataBaseHelper.loadMotivations();
            //Toast.makeText(getActivity(), "Motivations Loaded", Toast.LENGTH_LONG).show();
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        newMotiv = view.findViewById(R.id.btnMotiv);
        motiv = view.findViewById(R.id.tvMotivation);
        allow_notif = view.findViewById(R.id.switch_notif);
        ibshare = view.findViewById(R.id.ib_share);
        newMotiv.setBackgroundResource(R.drawable.btn_back);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        motivations = dataBaseHelper.getMotivations();
        newMotivation(motivations);

        newMotiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMotivation(motivations);
            }
        }); // showing a new motivational quote on the screen

        allow_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Toast.makeText(getActivity(), "Testing Switch 1", Toast.LENGTH_LONG).show();

                    NotificationEventReceiver.setupAlarm(getActivity().getApplicationContext());
                }
                else {
                    //Toast.makeText(getActivity(), "Testing Switch 2", Toast.LENGTH_LONG).show();

                    //Intent myService = new Intent(getActivity(), NotificationIntentService.class);
                    //getActivity().stopService(myService);
                    NotificationEventReceiver.cancelAlarm(getActivity().getApplicationContext());
                }
            }
        });// this is using share action provider to be able to the share the motivation to places

        ibshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareText = motiv.getText().toString();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText );
                startActivity(Intent.createChooser(sharingIntent,"test" ));
            }
        });
    }
    public Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr);

        return shareIntent;
    }

    public void newMotivation(List<String> motivations){
        Random r = new Random();
        motivations.size();
        int i1 = r.nextInt(4 - 0);

        motiv.setText("''" + motivations.get(i1 * 2) + "''" + "  - " + motivations.get((i1*2) + 1));
    }

}