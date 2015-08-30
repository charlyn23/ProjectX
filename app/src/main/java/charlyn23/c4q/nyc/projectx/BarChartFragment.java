package charlyn23.c4q.nyc.projectx;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BarChartFragment extends android.support.v4.app.Fragment {
    private int numWomen;
    private int numPOC;
    private int numLGBTQ;
    private int numMinor;
    protected BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bar_chart_fragment, container, false);
        TextView next = (TextView) view.findViewById(R.id.back);
        barChart = (BarChart) view.findViewById(R.id.bar_chart);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsFragment.innerViewPager.setCurrentItem(0);

            }
        });

        configBarChart(barChart);
        //setDataBarChart(4, 6, 7, 8);
        getCountGroups("");
        return view;
    }

    public void getCountGroups(String zipCode) {
        numWomen = 0;
        numPOC = 0;
        numLGBTQ = 0;
        numMinor = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shame");
        if (zipCode.length() > 0) {
            query.whereEqualTo("zipCode", zipCode);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects != null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        if (objects.get(i).get("Group").equals("woman")) {
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

                    if (numWomen == 0 && numMinor == 0 && numPOC == 0 && numLGBTQ == 0 ) {
                        barChart.setNoDataText("Cases of harassment have not been reported in your area!");
                    }
                    setDataBarChart(numWomen, numPOC, numLGBTQ, numMinor);
                }
            }
        });
    }

    public BarChart configBarChart(BarChart barChart) {
        barChart.setHighlightEnabled(true);
        barChart.setDrawValueAboveBar(false);
        barChart.setDescription("");
        barChart.setDrawBarShadow(false);

        XAxis xl = barChart.getXAxis();
        xl.setTextSize(13);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTextColor(Color.BLACK);
        xl.setAdjustXLabels(true);

        YAxis yl = barChart.getAxisLeft();
        yl.setEnabled(false);
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);

        YAxis yr = barChart.getAxisRight();
        yr.setAxisMaxValue(100);
        yr.setTextSize(13);
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
        yr.setEnabled(false);
        
        Legend l = barChart.getLegend();
        l.setEnabled(false);
        return barChart;
    }

    private void setDataBarChart(int women, int POC, int LGBTQ, int minor) {
        int sum = women + POC + LGBTQ + minor;
        double womenPerCent = women * 100 / sum;
        double POCPerCent = POC * 100 / sum;
        double LGBTQPerCent = LGBTQ * 100 / sum;
        double minorPerCent = minor * 100 / sum;

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("WOMAN");
        xVals.add("POC");
        xVals.add("LGBTQ");
        xVals.add("MINOR");

        yVals.add(new BarEntry((float) womenPerCent, 0));
        yVals.add(new BarEntry((float) POCPerCent, 1));
        yVals.add(new BarEntry((float) LGBTQPerCent, 2));
        yVals.add(new BarEntry((float) minorPerCent, 3));

        BarDataSet set = new BarDataSet(yVals, "");
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(android.R.color.holo_red_dark));
        set.setColors(colors);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(13);
        barChart.setData(data);

        barChart.animateY(2000);
    }

    public void animateChart() {
        if (barChart == null) {
            return;
        }
        barChart.animateY(2000);
    }
}
