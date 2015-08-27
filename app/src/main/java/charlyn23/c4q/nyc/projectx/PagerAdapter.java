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

    public PagerAdapter(FragmentManager fm, int NumOfTabs, boolean isLoggedIn) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.isLoggedIn = isLoggedIn;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ProjectXMapFragment();
            case 1:
                return new StatsFragment();
            case 2:
                if (isLoggedIn)
                    return new ProfileFragment();
                else
                    return new SignUpFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
