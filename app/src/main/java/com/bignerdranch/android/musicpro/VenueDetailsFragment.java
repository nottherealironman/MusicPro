package com.bignerdranch.android.musicpro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class VenueDetailsFragment extends Fragment
{
    public static final String EXTRAS_VENUE_ID = "venue_id";

    private Venue mVenue;

    private EditText mVenueName;
    private EditText mVenueAddress;
    private EditText mOpeningTime;
    private String eSubject="Invitation";//title of email
    private String eMessage;// body of email

    private Button mEmailButton;
    private Button mDoneButton;
    private Button mDiscardButton;
    boolean newVenue;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null)
        {
            UUID venueId = (UUID) extras.getSerializable(EXTRAS_VENUE_ID);
            mVenue = DbProvider.get(getActivity()).getVenue(venueId);
            newVenue = false;

        }
        else
        {
            mVenue = new Venue();
            newVenue = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_venue_details, container, false);

        mVenueName = (EditText) v.findViewById(R.id.venue_name);
        mVenueName.setText(mVenue.getName());

        mVenueAddress = (EditText) v.findViewById(R.id.venue_address);
        mVenueAddress.setText(mVenue.getAddress());

        mOpeningTime = (EditText) v.findViewById(R.id.venue_opening_time);
        mOpeningTime.setText(mVenue.getOpeningTime());

        mDiscardButton = (Button) v.findViewById(R.id.discard_button);
        mDiscardButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                confirmDelete();
            }
        });

        mDoneButton = (Button) v.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                confirmDone();
            }
        });

        mEmailButton = (Button) v.findViewById(R.id.email_button);
        mEmailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = mVenueName.getText().toString();
                String address = mVenueAddress.getText().toString();

                if ((name.isEmpty() || address.isEmpty()) !=true ) // Email does not open if name or address are not present
                {
                    //creating new intent
                    Intent it = new Intent(Intent.ACTION_SEND);
                    it.setType("text/plain");
                    // putting title of email in intent
                    it.putExtra(Intent.EXTRA_SUBJECT,eSubject);
                    //checking venue's opening time given or not
                    if (mOpeningTime.getText().toString().isEmpty())
                    {
                        eMessage="You are invited to "+ mVenueName.getText().toString()+" tonight";
                    }
                    else
                    {
                        eMessage="You are invited to "+ mVenueName.getText().toString()+" tonight at "+ mOpeningTime.getText().toString();
                    }
                    it.putExtra(Intent.EXTRA_TEXT, eMessage);
                    startActivity(Intent.createChooser(it,"Send Invitation"));
                }
                }
        });
        return v;
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    public void errorMsg()
    {
        AlertDialog d = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.warning)
                .setMessage(R.string.incomplete)
                .setPositiveButton(R.string.ok, null)
                .create();

        d.show();
    }

    public void confirmDone()
    {
        String name = mVenueName.getText().toString();
        String address = mVenueAddress.getText().toString();
        String openingTime = mOpeningTime.getText().toString();

        if (name.isEmpty() && address.isEmpty() )
        {
            DbProvider.get(getActivity()).deleteVenue(mVenue.getId());
            getActivity().finish();
        }
        else if (name.isEmpty() || address.isEmpty() )
        {
            errorMsg();
        }
        else
        {
            mVenue.setName(name);
            mVenue.setAddress(address);
            mVenue.setOpeningTime(openingTime);

            if (newVenue)
            {
                DbProvider.get(getActivity()).addVenue(mVenue);
            }
            else
            {
                DbProvider.get(getActivity()).updateVenue(mVenue);
            }

            getActivity().finish();
        }
    }

    public void confirmDelete()
    {
        AlertDialog d = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.confirm)
                .setMessage(R.string.delete_venue)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DbProvider.get(getActivity()).deleteVenue(mVenue.getId());
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .create();

        d.show();
    }
}
