package com.example.soura.autosecurehome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherFragment extends Fragment
{
    private RelativeLayout changebackground;
    private ImageView celestials;
    private View mMainView;
    private TextView temperature,humidity;

    private DatabaseReference mUserDatabase;
    private DatabaseReference mDevicesDatabase;
    private FirebaseUser mCurrentUser;

    public WeatherFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        mMainView = inflater.inflate(R.layout.fragment_weather, container, false);

        changebackground = (RelativeLayout) mMainView.findViewById(R.id.weatherbackground);
        celestials = (ImageView) mMainView.findViewById(R.id.celestial);
        temperature = (TextView) mMainView.findViewById(R.id.temperature);
        humidity = (TextView) mMainView.findViewById(R.id.humidity);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mDevicesDatabase = FirebaseDatabase.getInstance().getReference().child("Devices");
        mUserDatabase.keepSynced(true);
        mDevicesDatabase.keepSynced(true);


        mUserDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final String deviceid = dataSnapshot.child("deviceid").getValue().toString();

                mDevicesDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        String temperaturevalue = dataSnapshot.child(deviceid).child("TemperatureSensor").child("temperature").getValue().toString();
                        String humidityvalue = dataSnapshot.child(deviceid).child("TemperatureSensor").child("humidity").getValue().toString();

                        temperature.setText(temperaturevalue);
                        humidity.setText(humidityvalue);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                fun();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 100, 5000);


        return mMainView;
    }

    private void fun()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        cal.set(Calendar.HOUR_OF_DAY, 6);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 0);

        long morning_start = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);

        long morning_end = cal.getTimeInMillis();
        long now = System.currentTimeMillis();

        if (now > morning_start && now < morning_end) {
            changebackground.setBackgroundResource(R.drawable.day);
            celestials.setImageResource(R.drawable.sun);
        } else {
            changebackground.setBackgroundResource(R.drawable.night);
            celestials.setImageResource(R.drawable.moon);
        }
    }
}
