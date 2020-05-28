package com.bignerdranch.android.musicpro;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        Venue mVenue = (Venue) extras.getSerializable("Venue");
        Log.d("MapActivity:","---"+mVenue.getName());
        //setContentView(R.layout.activity_main);
    }
}
