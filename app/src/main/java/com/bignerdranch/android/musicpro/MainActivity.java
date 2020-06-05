package com.bignerdranch.android.musicpro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    Venue mVenue;

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
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra("Venue", mVenue);
        startActivity(intent);
    }

    // Multithreading using AsyncTask
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


