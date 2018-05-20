package com.example.soura.autosecurehome;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePageActivity extends AppCompatActivity
{

    private Toolbar toolbar;
    private ViewPager mViewPager;
    private SectionPagerAdapter mSectionPager;

    private TabLayout mTabLayout;

    private CircleImageView ProfileImage;
    private TextView nName;

    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private DatabaseReference mUserRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mViewPager=(ViewPager)findViewById(R.id.tab_Pager);
        mSectionPager=new SectionPagerAdapter(getSupportFragmentManager());

        mTabLayout=(TabLayout)findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mSectionPager);
        setSupportActionBar(toolbar);


        ProfileImage=(CircleImageView)findViewById(R.id.custom_bar_image);
        nName=(TextView)findViewById(R.id.custom_bar_title);


        mUserRef.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String name = dataSnapshot.child("name").getValue().toString();
                nName.setText(name);

                final String image = dataSnapshot.child("image").getValue().toString();

                Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile).into(ProfileImage, new Callback() {
                    @Override
                    public void onSuccess()
                    {

                    }

                    @Override
                    public void onError(Exception e)
                    {
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout)
        {
            logout();
            Toast.makeText(getApplicationContext(), "Logged out!", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.account_Settings)
        {
            Intent settings=new Intent(HomePageActivity.this,SettingsActivity.class);
            startActivity(settings);
        }
        if (id == R.id.about)
        {
            Intent settings=new Intent(HomePageActivity.this,AboutActivity.class);
            startActivity(settings);
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout()
    {
        mAuth.signOut();
        updateUI();
    }


    private void updateUI()
    {
        Intent account = new Intent(HomePageActivity.this,LoginActivity.class);
        startActivity(account);
        finish();
    }

    private static final int TIME_INTERVAL = 1000;
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
}
