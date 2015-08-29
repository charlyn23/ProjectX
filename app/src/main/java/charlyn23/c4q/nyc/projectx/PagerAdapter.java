package charlyn23.c4q.nyc.projectx;

/**
 * Created by sufeizhao on 8/11/15.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    boolean isLoggedIn;
    Fragment[] pagerFragments;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, boolean isLoggedIn) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.isLoggedIn = isLoggedIn;

        pagerFragments = new Fragment[3];
        pagerFragments[0] = new ProjectXMapFragment();
        pagerFragments[1] = new StatsFragment();
        pagerFragments[2] = new SignUpFragment();
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
                    return pagerFragments[2];
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
