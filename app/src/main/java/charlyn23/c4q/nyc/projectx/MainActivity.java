package charlyn23.c4q.nyc.projectx;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.parse.ParseFacebookUtils;

import charlyn23.c4q.nyc.projectx.shames.ShameDetailActivity;


public class MainActivity extends AppCompatActivity implements ProjectXMapFragment.OnDataPass, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "c4q.nyc.projectx";
    private static final String LAT_LONG = "latLong";
    private static final String LOGGED_IN = "isLoggedIn";
    private static final String LOGGED_IN_GOOGLE = "isLoggedInGoogle";
    private static final String SHOULD_RESOLVE = "should_resolve";
    private static final String IS_RESOLVING = "is_resolving";
    private static final int MAP_VIEW = 0;
    private static final int RC_SIGN_IN = 0;
    private static final String SHARED_PREFERENCE = "sharedPreference";
    private NoSwipeViewPager viewPager;
    private PagerAdapter viewPagerAdapter;
    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;
    private GoogleApiClient client;
    private boolean isLoggedIn, isLoggedIn_Google;
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
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.EMAIL))
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected: Google+");

        mShouldResolve = false;
        preferences.edit().putBoolean(SHOULD_RESOLVE, false).apply();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);

        mIsResolving = preferences.getBoolean(IS_RESOLVING, false);
        mShouldResolve = preferences.getBoolean(SHOULD_RESOLVE, false);
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                    preferences.edit().putBoolean(IS_RESOLVING, true).apply();
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    preferences.edit().putBoolean(IS_RESOLVING, false).apply();
                    client.connect();
                }
            } else {
                Toast.makeText(this, getString(R.string.network_connection_problem), Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("MAINACTIVITY", "OnConnectionFailed -- should not resolve");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
                preferences.edit().putBoolean(SHOULD_RESOLVE, false).apply();
            }

            mIsResolving = false;
            preferences.edit().putBoolean(IS_RESOLVING, false).apply();
            client.connect();
            viewPager.setCurrentItem(MAP_VIEW);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(LOGGED_IN, true);
            editor.putBoolean(LOGGED_IN_GOOGLE, true).apply();
            Toast.makeText(this, "Signing in", Toast.LENGTH_LONG).show();
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

        viewPagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), isLoggedIn, client);
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
}
