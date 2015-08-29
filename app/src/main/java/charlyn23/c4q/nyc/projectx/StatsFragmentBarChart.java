package charlyn23.c4q.nyc.projectx;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StatsFragmentBarChart extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats_fragment_bar_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager innerViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        innerViewPager.setAdapter(adapter);
        innerViewPager.setAdapter(new PagerAdapter((getChildFragmentManager());
    }

    public static class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putInt(TextViewFragment.POSITION_KEY, position);
            return TextViewFragment.newInstance(args);
        }
    }
