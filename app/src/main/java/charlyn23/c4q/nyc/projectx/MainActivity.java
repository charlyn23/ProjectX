package charlyn23.c4q.nyc.projectx;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Scene;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import java.util.List;

import charlyn23.c4q.nyc.projectx.map.NoSwipeViewPager;
import charlyn23.c4q.nyc.projectx.map.PagerAdapter;
import charlyn23.c4q.nyc.projectx.map.ProjectXMapFragment;
import charlyn23.c4q.nyc.projectx.shames.ShameDetailActivity;
import charlyn23.c4q.nyc.projectx.stats.StatsFragment;


public class MainActivity extends AppCompatActivity implements ProjectXMapFragment.OnDataPass, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private NoSwipeViewPager viewPager;
    private PagerAdapter viewPagerAdapter;
    public GoogleApiClient googleLogInClient;
    private boolean isLoggedIn, isLoggedIn_google;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Connects to Geolocation API to make current location request & load map
        buildGoogleApiClient(this);
        preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean(Constants.LOGGED_IN, false);
        isLoggedIn_google = preferences.getBoolean(Constants.LOGGED_IN_GOOGLE, false);
        checkLocationAccess();
        setUpActionBar();

        ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        Fade fadeTransition = new Fade();
        Scene firstScene = Scene.getSceneForLayout(sceneRoot, R.layout.map_fragment, this);
        Scene secondScene = Scene.getSceneForLayout(sceneRoot, R.layout.activity_details, this);

        getBundle();
    }

    protected synchronized void buildGoogleApiClient(Context context) {
        googleLogInClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.EMAIL))
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("MainActivity", "onConnected: Google+");
        if (Plus.PeopleApi.getCurrentPerson(googleLogInClient) != null && !isLoggedIn_google) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Constants.LOGGED_IN, true).apply();
            isLoggedIn_google = true;
            editor.putBoolean(Constants.LOGGED_IN_GOOGLE, true).apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("MainActivity", "Connection suspended in mainactivity");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("MainActivity", "onConnectionFailed: " + connectionResult);

        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, Constants.RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                Log.e(Constants.TAG, "Could not resolve ConnectionResult.", e);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                Log.d("MainActivity", "resultCode =! OKAY");
            } else {
                googleLogInClient.connect();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(Constants.LOGGED_IN, true).apply();
                editor.putBoolean(Constants.LOGGED_IN_GOOGLE, true).apply();

                Toast.makeText(this, getString(R.string.signing_in), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.SHOW_DIALOG, true);
                startActivity(intent);
            }
        }
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

        viewPagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), isLoggedIn, googleLogInClient);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
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

    //receives shame details from dropped pin, pulls down to map fragment's parent activity (main),
    //and passes data to shame details activity
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDataPass(double latitude, double longitude, String when, String who, String type) {
        Log.d("onDataPass", String.valueOf(latitude) + " " + String.valueOf(longitude) + " " + when + " " + who + " " + type);

        Intent intent = new Intent(MainActivity.this, ShameDetailActivity.class);
        intent.putExtra(Constants.WHEN, when);
        Log.i("date intent has ", String.valueOf(when));
        intent.putExtra(Constants.WHO, who);
        intent.putExtra(Constants.SHAME_LATITUDE_COLUMN, latitude);
        intent.putExtra(Constants.SHAME_LONGITUDE_COLUMN, longitude);
        intent.putExtra(Constants.SHAME_TYPE_COLUMN, type);

        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleLogInClient.disconnect();
        Log.d("MainActivity", "Client Disconnected onStop");
            preferences.edit().putBoolean(Constants.IS_DROPPED, false).commit();
    }

    private void getBundle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean isLoggedIn = extras.getBoolean(Constants.SHOW_DIALOG);
            if (isLoggedIn) {
                ProjectXMapFragment projectXMapFragment = (ProjectXMapFragment) viewPagerAdapter.getItem(0);
                Bundle fragmentBundle = new Bundle();
                fragmentBundle.putBoolean(Constants.SHOW_DIALOG, true);
                projectXMapFragment.setArguments(fragmentBundle);
            }
        }
    }

    public void checkLocationAccess() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getResources().getString(R.string.action_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });
            dialog.show();
        }
    }
}
