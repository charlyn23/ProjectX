package charlyn23.c4q.nyc.projectx;

/**
 * Created by sufeizhao on 8/11/15.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MapFragment mapFragment = new MapFragment();
                return mapFragment;
            case 1:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 2:
                ProfileFragment extra = new ProfileFragment();
                return extra;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
