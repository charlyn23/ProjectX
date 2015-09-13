package charlyn23.c4q.nyc.projectx;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
    private Scene firstScene;
    private Scene secondScene;
    private Fade fadeTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connects to Geolocation API to make current location request & load map
        buildGoogleApiClient(this);
        preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean(Constants.LOGGED_IN, false);
        isLoggedIn_google = preferences.getBoolean(Constants.LOGGED_IN_GOOGLE, false);
        setUpActionBar();

        ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        fadeTransition = new Fade();

        firstScene = Scene.getSceneForLayout(sceneRoot, R.layout.map_fragment, this);
        secondScene = Scene.getSceneForLayout(sceneRoot, R.layout.activity_details, this);

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
        } else {
            Toast.makeText(this, getString(R.string.network_connection_problem), Toast.LENGTH_LONG).show();
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
}
