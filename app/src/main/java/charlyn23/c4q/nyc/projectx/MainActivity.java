package charlyn23.c4q.nyc.projectx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import charlyn23.c4q.nyc.projectx.shames.ShameDetailActivity;


public class MainActivity extends AppCompatActivity implements ProjectXMapFragment.OnDataPass {
    private static final String TAG = "c4q.nyc.projectx";

    private static final String LAT_LONG = "latLong";
    private PagerAdapter adapter;
    private static final String LOGGED_IN = "isLoggedIn";
    private static final String SHARED_PREFERENCE = "sharedPreference";
    private boolean isLoggedIn;
    private NoSwipeViewPager viewPager;

    ShameDetailActivity shameDetailActivity = new ShameDetailActivity();

    ProjectXMapFragment.OnDataPass dataPasser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ProjectXMapFragment projectXMapFragment = new ProjectXMapFragment();
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean(LOGGED_IN, false);

        setUpActionBar();
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

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), isLoggedIn);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
