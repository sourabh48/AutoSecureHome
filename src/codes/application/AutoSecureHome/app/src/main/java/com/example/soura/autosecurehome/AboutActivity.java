package com.example.soura.autosecurehome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("Our app is the front end of our IOT project Auto Secure Home. " +
                        "Gives updates of room temperature and humidity. Gives u advantage to switch on or off the electronic appliances. " +
                        " Detects intruders and Alerts if dustbin is full." +
                        "\n \n Team::                                                          " +
                        "\n  Sourabh Sarkar(1515048)" +
                        "\n Srijita Gayen(1515049)" +
                        "\n Prerna Singh(1515031)")
                .setImage(R.drawable.smarthome)
                .addItem(new Element().setTitle("Version 1.1"))
                .addGroup("Connect with us")
                .addEmail("sourabh.654321@outlook.com")
                .addWebsite("http://medyo.github.io/")
                .addFacebook("sourabh.sarkar.750")
                .create();
        setContentView(aboutPage);

    }
}
