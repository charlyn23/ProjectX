package charlyn23.c4q.nyc.projectx.map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import charlyn23.c4q.nyc.projectx.ProfileFragment;
import charlyn23.c4q.nyc.projectx.SignUpFragment;
import charlyn23.c4q.nyc.projectx.stats.StatsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    boolean isLoggedIn;
    Fragment[] pagerFragments;
    GoogleApiClient client;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, boolean isLoggedIn,GoogleApiClient client) {
        super(fm);
        this.isLoggedIn = isLoggedIn;
        this.mNumOfTabs = NumOfTabs;
        this.client = client;

        pagerFragments = new Fragment[4];
        pagerFragments[0] = new ProjectXMapFragment();
        pagerFragments[1] = new StatsFragment();
        pagerFragments[2] = new ProfileFragment(client);
        pagerFragments[3] = new SignUpFragment(client);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return pagerFragments[0];
            case 1:
                return pagerFragments[1];
            case 2:
                if (isLoggedIn)
                    return pagerFragments[2];
                else
                    return pagerFragments[3];
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
