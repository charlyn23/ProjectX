package charlyn23.c4q.nyc.projectx;

/**
 * Created by sufeizhao on 8/11/15.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.android.gms.common.api.GoogleApiClient;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    boolean isLoggedIn;
    Fragment[] pagerFragments;
    GoogleApiClient client;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;

        pagerFragments = new Fragment[4];
        pagerFragments[0] = new ProjectXMapFragment();
        pagerFragments[1] = new StatsFragment();
        pagerFragments[2] = new ProfileFragment();
        pagerFragments[3] = new SignUpFragment();
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
}
