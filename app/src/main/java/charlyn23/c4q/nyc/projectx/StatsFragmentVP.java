package charlyn23.c4q.nyc.projectx;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class StatsFragmentVP extends android.support.v4.app.Fragment {
    private EditText zipcode;
    private Button update;
    public static ViewPager innerViewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment_vp, container, false);
        zipcode = (EditText) view.findViewById(R.id.zipcode);
        update = (Button) view.findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = zipcode.getText().toString();
                if (userInput.length() > 0) {

                }
            }
        });

        innerViewPager = (ViewPager) view.findViewById(R.id.inner_pager);
        innerViewPager.setAdapter(new InnerAdapter(getChildFragmentManager()));

        return view;
    }

    public static class InnerAdapter extends FragmentPagerAdapter {

        public InnerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new StatsFragmentPieChart();
                case 1:
                    return new StatsFragmentBarChart();
                default:
                    return null;
            }
        }

//        @Override
//        public Fragment getItem(int position) {
//            Bundle args = new Bundle();
//            args.putInt(TextViewFragment.POSITION_KEY, position);
//            return TextViewFragment.newInstance(args);
//        }
    }
}
