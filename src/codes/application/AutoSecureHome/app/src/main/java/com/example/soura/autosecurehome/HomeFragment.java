package com.example.soura.autosecurehome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment
{
    private DatabaseReference mUserDatabase;
    private DatabaseReference mDevicesDatabase;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDeviceId;


    //HallRoom Switches
    Switch HLights, HFan, HAirconditioner;

    //Kitchen Switches
    Switch kitchen, KLights, KExFan;

    //Bedroom Switches
    Switch BLights, BFan, BAirconditioner;

    //Bathroom Switches
    Switch Balights,BaExFan,BaWaHeater;

    private View mMainView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        mMainView = inflater.inflate(R.layout.fragment_home, container, false);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mDevicesDatabase = FirebaseDatabase.getInstance().getReference().child("Devices");
        mUserDatabase.keepSynced(true);
        mDevicesDatabase.keepSynced(true);

        //HallRoom
        HLights = (Switch)mMainView.findViewById(R.id.lightswitch);
        HFan = (Switch)mMainView.findViewById(R.id.fanswitch);
        HAirconditioner = (Switch)mMainView.findViewById(R.id.ac_switch);

        //Kitchen

        KLights = (Switch)mMainView.findViewById(R.id.kitchenlightswitch);
        KExFan = (Switch)mMainView.findViewById(R.id.kitchenfanswitch);

        //Bedroom

        BLights = (Switch)mMainView.findViewById(R.id.bedroomlightswitch);
        BFan = (Switch)mMainView.findViewById(R.id.bedroomfanswitch);
        BAirconditioner = (Switch)mMainView.findViewById(R.id.bedroom_acswitch);

        //Bathroom

        Balights = (Switch)mMainView.findViewById(R.id.bathroomlightswitch);
        BaExFan = (Switch)mMainView.findViewById(R.id.bathroomfanswitch);
        BaWaHeater = (Switch)mMainView.findViewById(R.id.bathroomheaterswitch);



        //hallroom

        HLights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDeviceId.child("Switches").child("hall").child("halllightswitch").setValue(true);
                }
                else
                {
                    mDeviceId.child("Switches").child("hall").child("halllightswitch").setValue(false);
                }
            }
        });

        HFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDeviceId.child("Switches").child("hall").child("hallfanswitch").setValue(true);
                }
                else
                {
                    mDeviceId.child("Switches").child("hall").child("hallfanswitch").setValue(false);
                }
            }
        });

        HAirconditioner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDeviceId.child("Switches").child("hall").child("hallacswitch").setValue(true);
                }
                else
                {
                    mDeviceId.child("Switches").child("hall").child("hallacswitch").setValue(false);
                }
            }
        });


        //kitchen

        KLights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDeviceId.child("Switches").child("kitchen").child("kitchenlightswitch").setValue(true);
                }
                else
                {
                    mDeviceId.child("Switches").child("kitchen").child("kitchenlightswitch").setValue(false);
                }
            }
        });

        KExFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDeviceId.child("Switches").child("kitchen").child("kitchenfanswitch").setValue(true);
                }
                else
                {
                    mDeviceId.child("Switches").child("kitchen").child("kitchenfanswitch").setValue(false);
                }
            }
        });

        //Bedroom

        BLights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDeviceId.child("Switches").child("Bedroom").child("Bedroomlightswitch").setValue(true);
                }
                else
                {
                    mDeviceId.child("Switches").child("Bedroom").child("Bedroomlightswitch").setValue(false);
                }
            }
        });

        BFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDeviceId.child("Switches").child("Bedroom").child("Bedroomfanswitch").setValue(true);
                }
                else
                {
                    mDeviceId.child("Switches").child("Bedroom").child("Bedroomfanswitch").setValue(false);
                }
            }
        });

        BAirconditioner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDeviceId.child("Switches").child("Bedroom").child("Bedroomacswitch").setValue(true);
                }
                else
                {
                    mDeviceId.child("Switches").child("Bedroom").child("Bedroomacswitch").setValue(false);
                }
            }
        });

        //Bathroom

        Balights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDeviceId.child("Switches").child("Bathroom").child("Bathroomlightswitch").setValue(true);
                }
                else
                {
                    mDeviceId.child("Switches").child("Bathroom").child("Bathroomlightswitch").setValue(false);
                }
            }
        });

        BaExFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDeviceId.child("Switches").child("Bathroom").child("BathroomExfanswitch").setValue(true);
                }
                else
                {
                    mDeviceId.child("Switches").child("Bathroom").child("BathroomExfanswitch").setValue(false);
                }
            }
        });

        BaWaHeater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDeviceId.child("Switches").child("Bathroom").child("BathroomHeaterswitch").setValue(true);
                }
                else
                {
                    mDeviceId.child("Switches").child("Bathroom").child("BathroomHeaterswitch").setValue(false);
                }
            }
        });


        mUserDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final String deviceid = dataSnapshot.child("deviceid").getValue().toString();
                mDeviceId = FirebaseDatabase.getInstance().getReference().child("Devices").child(deviceid);

                mDevicesDatabase.child(deviceid).child("Switches").addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        //Bathroom

                        String BathroomHeater = dataSnapshot.child("Bathroom").child("BathroomHeaterswitch").getValue().toString();
                        String Bathroomlight = dataSnapshot.child("Bathroom").child("Bathroomlightswitch").getValue().toString();
                        String BathroomExfan = dataSnapshot.child("Bathroom").child("BathroomExfanswitch").getValue().toString();

                        if(BathroomHeater.equals("true"))
                        {
                            BaWaHeater.setChecked(true);
                        }
                        else
                        {
                            BaWaHeater.setChecked(false);
                        }

                        if(Bathroomlight.equals("true"))
                        {
                            Balights.setChecked(true);
                        }
                        else
                        {
                            Balights.setChecked(false);
                        }

                        if(BathroomExfan.equals("true"))
                        {
                            BaExFan.setChecked(true);
                        }
                        else
                        {
                            BaExFan.setChecked(false);
                        }

                        //Bedroom

                        String Bedroomac = dataSnapshot.child("Bedroom").child("Bedroomacswitch").getValue().toString();
                        String Bedroomlight = dataSnapshot.child("Bedroom").child("Bedroomlightswitch").getValue().toString();
                        String Bedroomfan = dataSnapshot.child("Bedroom").child("Bedroomfanswitch").getValue().toString();

                        if(Bedroomac.equals("true"))
                        {
                            BAirconditioner.setChecked(true);
                        }
                        else
                        {
                            BAirconditioner.setChecked(false);
                        }

                        if(Bedroomlight.equals("true"))
                        {
                            BLights.setChecked(true);
                        }
                        else
                        {
                            BLights.setChecked(false);
                        }

                        if(Bedroomfan.equals("true"))
                        {
                            BFan.setChecked(true);
                        }
                        else
                        {
                            BFan.setChecked(false);
                        }


                        //Hallroom

                        String Hallacswitch = dataSnapshot.child("hall").child("hallacswitch").getValue().toString();
                        String Halllightswitch = dataSnapshot.child("hall").child("halllightswitch").getValue().toString();
                        String Hallfanswitch = dataSnapshot.child("hall").child("hallfanswitch").getValue().toString();

                        if(Hallacswitch.equals("true"))
                        {
                            HAirconditioner.setChecked(true);
                        }
                        else
                        {
                            HAirconditioner.setChecked(false);
                        }

                        if(Hallfanswitch.equals("true"))
                        {
                            HFan.setChecked(true);
                        }
                        else
                        {
                            HFan.setChecked(false);
                        }

                        if(Halllightswitch.equals("true"))
                        {
                            HLights.setChecked(true);
                        }
                        else
                        {
                            HLights.setChecked(false);
                        }


                        //Kitchen

                        String kitchenex = dataSnapshot.child("kitchen").child("kitchenfanswitch").getValue().toString();
                        String kitchenlight = dataSnapshot.child("kitchen").child("kitchenlightswitch").getValue().toString();

                        if(kitchenlight.equals("true"))
                        {
                            KLights.setChecked(true);
                        }
                        else
                        {
                            KLights.setChecked(false);
                        }

                        if(kitchenex.equals("true"))
                        {
                            KExFan.setChecked(true);
                        }
                        else
                        {
                            KExFan.setChecked(false);
                        }


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

        return mMainView;
    }

}
