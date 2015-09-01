package charlyn23.c4q.nyc.projectx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import charlyn23.c4q.nyc.projectx.shames.ShameDetailActivity;


public class MainActivity extends AppCompatActivity implements ProjectXMapFragment.OnDataPass, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "c4q.nyc.projectx";
    private static final String LAT_LONG = "latLong";
    private PagerAdapter adapter;
    private static final String LOGGED_IN = "isLoggedIn";
    private static final String SHARED_PREFERENCE = "sharedPreference";
    private boolean isLoggedIn;
    private NoSwipeViewPager viewPager;
    private PagerAdapter viewPagerAdapter;
    private GoogleApiClient client;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean(LOGGED_IN, false);

        // Connect to Geolocation API to make current location request & load map
        buildGoogleApiClient(this);
        setUpActionBar();
    }

    protected synchronized void buildGoogleApiClient(Context context) {
        client = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        Log.d("Map", "Connected to Google API Client");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(client);
        if (location == null)
            LocationServices.FusedLocationApi.requestLocationUpdates(client, createLocationRequest(), new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    preferences.edit().putString("Lat", String.valueOf(location.getLatitude()));
                    preferences.edit().putString("Long", String.valueOf(location.getLongitude())).apply();
                }
            });
        else {
            preferences.edit().putString("Lat", String.valueOf(location.getLatitude()));
            preferences.edit().putString("Long", String.valueOf(location.getLongitude())).apply();
        }
    }

    private LocationRequest createLocationRequest() {
        return new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    public void setUpActionBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        viewPager = (NoSwipeViewPager) findViewById(R.id.view_pager);
        setSupportActionBar(mToolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.map));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.stats));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profile));

        viewPagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    StatsFragment statsFragment = (StatsFragment) viewPagerAdapter.getItem(position);
                    statsFragment.pageSelected();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    //displays the first page on the Back Button pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            viewPager.setCurrentItem(0, true);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onDataPass(double latitude, double longitude, String when, String who, String type) {
        Log.d("onDataPass" , String.valueOf(latitude) + " " +  String.valueOf(longitude) +" " +  when +" " +   who +" " +  type);
        double shameLat = latitude;
        double shameLong = longitude;
        String shameDateTime = when;
        String shameGroup = who;
        String shameType =  type;

        Intent intent = new Intent(MainActivity.this, ShameDetailActivity.class);
        intent.putExtra("when", when);
        Log.i("date intent has ", String.valueOf(when));
        intent.putExtra("who", who);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("type", type);
        startActivity(intent);

//        TextView group = (TextView) shameDetailActivity.findViewById(R.id.group);
//        group.setText(who);

    }

    // Called when the Activity could not connect to Google Play services and the auto manager
    // could resolve the error automatically.
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
}
