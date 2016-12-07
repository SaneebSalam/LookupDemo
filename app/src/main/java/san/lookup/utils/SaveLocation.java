package san.lookup.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import san.lookup.AppController;

/**
 * Created by Saneeb Salam
 * on 12/7/2016.
 */

public class SaveLocation extends AsyncTask<Void, Void, Void> {
    private Location mLastLocation;
    private Context context;
    private AppController application;


    public SaveLocation(Location mLastLocation, Context context, AppController application) {
        this.mLastLocation = mLastLocation;
        this.context = context;
        this.application = application;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Void doInBackground(Void... params) {

        if (mLastLocation != null) {
            application.setsharedprefString(Lookup_Keys.LATTITUDE, String.valueOf(mLastLocation.getLatitude()));
            application.setsharedprefString(Lookup_Keys.LONGITUDE, String.valueOf(mLastLocation.getLongitude()));

            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(context, Locale.getDefault());
                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                application.setsharedprefString(Lookup_Keys.ADDRESS, addresses.get(0).getAddressLine(0));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
