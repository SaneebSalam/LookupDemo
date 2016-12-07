package san.lookup.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import san.lookup.AppController;
import san.lookup.BaseActivity;
import san.lookup.R;
import san.lookup.addapter.ChatContentAdapter;
import san.lookup.addapter.POJO_item;
import san.lookup.addapter.RecyclerViewDataAdapter;
import san.lookup.utils.Lookup_Keys;
import san.lookup.utils.SaveLocation;
import su.j2e.rvjoiner.JoinableAdapter;
import su.j2e.rvjoiner.JoinableLayout;
import su.j2e.rvjoiner.RvJoiner;

/**
 * Created by Saneeb Salam
 * on 12/7/2016.
 */

public class MainActivity extends BaseActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    RecyclerView recyclerView;
    String Imagepath;
    POJO_item dm;
    RecyclerViewDataAdapter adapterhead;
    ChatContentAdapter adapter;
    ArrayList<POJO_item> allSampleData, feedItems_head, feedItems;
    TextView currentLocation;
    ProgressDialog dialog;
    private static final int ACCESS_FINE_LOCATION = 0;
    GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        feedItems_head = new ArrayList<>();
        feedItems = new ArrayList<>();
        allSampleData = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        dialog = ProgressDialog.show(MainActivity.this, "",
                "Loading. Please wait...", true);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        getCategories();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        if (requestCode == ACCESS_FINE_LOCATION) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            new SaveLocation(mLastLocation, context, application).execute();
            getCategories();
        }

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void GetChatlist() {

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get("http://ec2-54-169-238-70.ap-southeast-1.compute.amazonaws.com:5000/businesses");
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                parseJsonfeed_chat(new JSONObject(data));
            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
            }
        }
        // making fresh volley request and getting json
        JsonObjectRequest jsonReq = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                "http://ec2-54-169-238-70.ap-southeast-1.compute.amazonaws.com:5000/businesses", null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                parseJsonfeed_chat(response);
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error: " + error.toString());
                showtoast("Please check your connection");
                dialog.dismiss();
            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);


    }

    private void parseJsonfeed_chat(JSONObject jsonObject) {

        try {
            JSONObject json_data = jsonObject.getJSONObject("data");
            JSONArray json_business = json_data.getJSONArray("businesses");
            feedItems.clear();
            for (int i = 0; i < json_business.length(); i++) {

                POJO_item item = new POJO_item();
                item.setId(json_business.getJSONObject(i).getString("id"));
                item.setName(json_business.getJSONObject(i).getString("name"));
                item.setImage(json_business.getJSONObject(i).getString("profileImage"));
                if (!application.getsharedprefString(Lookup_Keys.LATTITUDE).isEmpty())
                    item.setLatlng(application.Getdistance(json_business.getJSONObject(i).getString("location")));
                else
                    item.setLatlng("");

                JSONObject json_address = (JSONObject) json_business.getJSONObject(i).get("address");
                item.setAddress(json_address.getString("addressLine"));

                feedItems.add(item);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter = new ChatContentAdapter(recyclerView.getContext(), feedItems);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RvJoiner rvJoiner = new RvJoiner();
        rvJoiner.add(new JoinableLayout(R.layout.layout_header, new JoinableLayout.Callback() {
            @Override
            public void onInflateComplete(View view, ViewGroup parent) {

                currentLocation = (TextView) view.findViewById(R.id.currentlocation);
                currentLocation.setText(application.getsharedprefString(Lookup_Keys.ADDRESS));

            }
        }));
        rvJoiner.add(new JoinableAdapter(adapterhead));
        rvJoiner.add(new JoinableLayout(R.layout.item_title));
        rvJoiner.add(new JoinableAdapter(adapter));

        //set join adapter to your RecyclerView
        recyclerView.setAdapter(rvJoiner.getAdapter());
        dialog.dismiss();

    }

    private void getCategories() {
        try {
            dialog.show();

            Resources resources = this.getResources();
            String[] category_names = resources.getStringArray(R.array.Category_names);
            String[] cat_image = resources.getStringArray(R.array.cat_img);

            if (category_names.length != 0) {
                dm = new POJO_item();
                dm.setHeaderTitle(getResources().getString(R.string.categories_handpicked_for_you));
                feedItems_head = new ArrayList<>();
                allSampleData.clear();
                for (int i = 0; i < category_names.length; i++) {

                    Imagepath = cat_image[i];

                    feedItems_head.add(new POJO_item(String.valueOf(i), category_names[i],
                            Imagepath));
                }
                dm.setAllItemsInSection(feedItems_head);
                allSampleData.add(dm);
            }

            adapterhead = new RecyclerViewDataAdapter(getApplicationContext(), allSampleData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        GetChatlist();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            MainActivity.super.requestAppPermissions(new
                            String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION);
        } else {

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            new SaveLocation(mLastLocation, context, application).execute();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}