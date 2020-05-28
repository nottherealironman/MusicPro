package com.bignerdranch.android.musicpro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity
{
    Venue mVenue;

    //private static final String TAG = "PhotoGalleryFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DbProvider.get(this).loadTestData();
        updateItems();
    }

    private void updateItems() {
        new FetchItemTask().execute();
    }

    public void onVenueClick(View view)
    {
        Intent intent = new Intent(MainActivity.this, VenueListActivity.class);
        startActivity(intent);
    }

    public void onMapClick(View view)
    {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        intent.putExtra("Venue", mVenue);
        startActivity(intent);
    }

    // just for testing the map without retrieving top-rated venue
    void getTestVenue()
    {
        mVenue = new Venue();
        mVenue.setName("Reef Hotel");
        mVenue.setAddress("Wharf Street, Cairns");
        mVenue.setLat(-16.9238);
        mVenue.setLon(145.7797);
    }

    private class FetchItemTask extends AsyncTask<Void, Void, Venue>
    {

        @Override
        protected Venue doInBackground(Void... voids) {
            return new VenueFetchr().downloadTopRatedVenue();
        }

        @Override
        protected void onPostExecute(Venue item){
            mVenue = item;
            Log.d("Response received:","name::"+item.getName());
        }
    }
}


