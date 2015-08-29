package charlyn23.c4q.nyc.projectx;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class StatsFragmentBarChart extends android.support.v4.app.Fragment {
    private int numWomen;
    private int numPOC;
    private int numLGBTQ;
    private int numMinor;
    private ArrayList<Integer> colors;
    protected HorizontalBarChart barChart;
    private Typeface tf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats_fragment_bar_chart, container, false);
        TextView next = (TextView) view.findViewById(R.id.back);
        barChart = (HorizontalBarChart) view.findViewById(R.id.bar_chart);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsFragmentVP.innerViewPager.setCurrentItem(0);

            }
        });

        configBarChart(barChart);
        getCountGroups();
        return view;
    }

    public void getCountGroups() {
        numWomen = 0;
        numPOC = 0;
        numLGBTQ = 0;
        numMinor = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shame");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        if (objects.get(i).get("Group") == null) {
                            continue;
                        } else if (objects.get(i).get("Group").equals("woman")) {
                            numWomen++;
                        } else if (objects.get(i).get("Group").equals("LGBTQ")) {
                            numLGBTQ++;
                        } else if (objects.get(i).get("Group").equals("POC")) {
                            numPOC++;
                        } else {
                            numMinor++;
                        }
                    }
                    Log.d("yuliya", numWomen + "w ");
                    Log.d("yuliya", numPOC + " poc");
                    Log.d("yuliya", numLGBTQ + "lgbtq ");

                    setDataBarChart(4, 80);
                }
            }
        });
    }

    public BarChart configBarChart(BarChart barChart) {
        barChart.setHighlightEnabled(true);

        barChart.setDrawValueAboveBar(false);

//        barChart.setDescription("Groups of people");

        // if more than 60 entries are displayed in the pieChart, no values will be
        // drawn
        //barChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(true);

        // draw shadows for each bar that show the maximum value
        barChart.setDrawBarShadow(true);

        // barChart.setDrawXLabels(false);

        barChart.setDrawGridBackground(false);

        // barChart.setDrawYLabels(false);

        //tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xl = barChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGridLineWidth(0.3f);

        YAxis yl = barChart.getAxisLeft();
//        yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(false);
        yl.setGridLineWidth(0.3f);
//        yl.setInverted(true);

        YAxis yr = barChart.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);

//        yr.setInverted(true);

        //setData(4, 100);
        barChart.animateY(3000);

        Legend l = barChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(20f);
        l.setXEntrySpace(4f);

        // barChart.setDrawLegend(false);
        return barChart;
    }

    private void setDataBarChart(int count, float range) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("woman");
        xVals.add("POC");
        xVals.add("LGBTQ");
        xVals.add("MINOR");

        yVals1.add(new BarEntry((float) numWomen, 0));
        yVals1.add(new BarEntry((float) numPOC, 1));
        yVals1.add(new BarEntry((float) numLGBTQ, 2));
        yVals1.add(new BarEntry((float) numMinor, 3));

        BarDataSet set = new BarDataSet(yVals1, "Groups of people");
        colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(android.R.color.holo_red_dark));
        //colors.add(getResources().getColor(android.R.color.holo_red_light));
        set.setColors(colors);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set);


        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(15f);
        data.setValueTypeface(tf);

        barChart.setData(data);
    }
}
