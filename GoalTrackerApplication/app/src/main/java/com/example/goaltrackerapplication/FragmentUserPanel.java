package com.example.goaltrackerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class FragmentUserPanel extends Fragment {
    View view;

    public FragmentUserPanel() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.userpanel_fragment,container,false);
        updateUserInfo();

        Button focus = view.findViewById(R.id.btnFocus);
        focus.setBackgroundResource(R.drawable.btn_back);
        Button signout = view.findViewById(R.id.btnSignOut);
        signout.setBackgroundResource(R.drawable.btn_back);
        //Button settings = view.findViewById(R.id.btnSettings);
        //settings.setBackgroundResource(R.drawable.btn_back);

        signout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((MainHome)getActivity()).signOut();
            }
        });
        /*settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });*/
        focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserGuideActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void updateUserInfo(){
        TextView uName = view.findViewById(R.id.tvUserName);
        TextView uEmail = view.findViewById(R.id.tvUserEmail);
        ImageView uProfile = view.findViewById(R.id.iv_profile);
        uName.setText(((MainHome)getActivity()).getName());
        uEmail.setText(((MainHome)getActivity()).getEmail());
        Glide.with(this).load(((MainHome) getActivity()).getProfileURL()).into(uProfile);

        // might end up having a way to display the profile picture of the user
    }
}