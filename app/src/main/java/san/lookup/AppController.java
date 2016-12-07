package san.lookup;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;

import san.lookup.utils.Lookup_Keys;

/**
 * Created by Saneeb Salam
 * on 12/7/2016.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    public static SharedPreferences preferences;
    public static Editor editor;
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    Double lat1, lat2, lon1, lon2, theta, dist;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        preferences = getSharedPreferences("lookup", 0);

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    // Shared preferances
    public void setsharedprefString(String key, String value) {
        editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getsharedprefString(String key) {
        return preferences.getString(key, "");
    }

    public String Getdistance(String latlng) {
        lat1 = Double.valueOf(latlng.split(",")[0]);
        lon1 = Double.valueOf(latlng.split(",")[1]);
        lat2 = Double.valueOf(getsharedprefString(Lookup_Keys.LATTITUDE));
        lon2 = Double.valueOf(getsharedprefString(Lookup_Keys.LONGITUDE));

        if (latlng.split(",")[0].isEmpty() || (latlng.split(",")[0]).equalsIgnoreCase("0")
                || (latlng.split(",")[0]).equalsIgnoreCase("0.0")
                || getsharedprefString(Lookup_Keys.LATTITUDE).isEmpty()
                || getsharedprefString(Lookup_Keys.LATTITUDE).equalsIgnoreCase("0.0"))
            return "";

        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        DecimalFormat form = new DecimalFormat("0.00");
        return (form.format(dist)+" KM");
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}


