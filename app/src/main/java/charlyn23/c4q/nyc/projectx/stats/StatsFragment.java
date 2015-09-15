package charlyn23.c4q.nyc.projectx.stats;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import charlyn23.c4q.nyc.projectx.R;

public class StatsFragment extends android.support.v4.app.Fragment {
    private EditText zipCode;
    private int lastChildFragmentShown = 0;
    private static Fragment[] fragments;
    public static ViewPager innerViewPager;

    //remembers and animates the last viewed inner graph fragment
    public void pageSelected() {
        if (lastChildFragmentShown == 0) {
            ((PieChartFragment) fragments[0]).animateChart();
        }
        else if (lastChildFragmentShown == 1) {
            ((BarChartFragment) fragments[1]).animateChart();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment, container, false);
        Typeface questrial = Typeface.createFromAsset(getActivity().getAssets(), "questrial.ttf");
        Button overView = (Button) view.findViewById(R.id.overview);
        zipCode = (EditText) view.findViewById(R.id.zipcode);
        zipCode.setTypeface(questrial);
        overView.setTypeface(questrial);

        //child fragments
        fragments= new Fragment[2];
        fragments[0] = new PieChartFragment();
        fragments[1] = new BarChartFragment();

        //shows the general data collected
        overView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipCode.setText("");
                ((PieChartFragment) fragments[0]).countShameTypes("");
                ((BarChartFragment) fragments[1]).countGroups("");

            }
        });

        zipCode.addTextChangedListener(zipCodeWatcher);
        innerViewPager = (ViewPager) view.findViewById(R.id.inner_pager);
        InnerAdapter innerAdapter = new InnerAdapter(getChildFragmentManager());
        innerViewPager.setAdapter(innerAdapter);
        innerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ((PieChartFragment) fragments[0]).animateChart();
                }
                else {
                    ((BarChartFragment) fragments[1]).animateChart();
                }
                lastChildFragmentShown = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    private final TextWatcher zipCodeWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            String userInput = zipCode.getText().toString();
            if (userInput.length() == 5) {
                ((PieChartFragment) fragments[0]).countShameTypes(userInput);
                ((BarChartFragment) fragments[1]).countGroups(userInput);

                //hides the keyboard when the user finishes typing zipCode
                Activity context = getActivity();
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = context.getCurrentFocus();
                if(view == null) {
                    view = new View(context);
                }
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    };

    //inner view pager adapter
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
                    return fragments[0];
                case 1:
                    return  fragments[1];
                default:
                    return null;
            }
        }
    }
}
