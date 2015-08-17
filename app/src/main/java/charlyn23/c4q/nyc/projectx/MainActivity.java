package charlyn23.c4q.nyc.projectx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.parse.ParseUser;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "c4q.nyc.projectx";
    private static final String SHARED_PREFERENCE = "sharedPreference";
    private static final String LOGGED_IN = "isLoggedIn";
    private PagerAdapter adapter;
    private NoSwipeViewPager viewPager;
    private ProjectXMapFragment projectXMapFragment;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Intent intent = getIntent();
        String position = intent.getStringExtra("position");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectXMapFragment = new ProjectXMapFragment();

        setUpActionBar();

    }




    public void setUpActionBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        viewPager = (NoSwipeViewPager) findViewById(R.id.view_pager);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.map));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profile));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.stats));

        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    // MENU RESOURCES
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        int id = item.getItemId();
        if (id == R.id.log_out) {

            ParseUser user = ParseUser.getCurrentUser();

            user.logOut();
            editor.putBoolean(LOGGED_IN, false).commit();
            Toast.makeText(this, getString(R.string.log_out_toast), Toast.LENGTH_LONG).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
