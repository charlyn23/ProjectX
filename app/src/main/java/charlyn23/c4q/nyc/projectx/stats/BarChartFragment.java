package charlyn23.c4q.nyc.projectx.stats;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
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

import charlyn23.c4q.nyc.projectx.Constants;
import charlyn23.c4q.nyc.projectx.R;
import charlyn23.c4q.nyc.projectx.map.ShameSQLiteHelper;

public class BarChartFragment extends android.support.v4.app.Fragment {
    private int numWomen;
    private int numPOC;
    private int numLGBTQ;
    private int numMinor;
    private int numOther;
    private Typeface questrial;
    private BarChart barChart;
    private TextView numInstances;
    private TextView noHarassmentMessage;
    private TextView header;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bar_chart_fragment, container, false);
        ImageView next = (ImageView) view.findViewById(R.id.back);
        barChart = (BarChart) view.findViewById(R.id.bar_chart);
        header = (TextView) view.findViewById(R.id.chart_header);
        noHarassmentMessage = (TextView) view.findViewById(R.id.no_harassment_message_bar);
        numInstances = (TextView) view.findViewById(R.id.instances);

        questrial = Typeface.createFromAsset(getActivity().getAssets(), "questrial.ttf");
        header.setTypeface(questrial);
        noHarassmentMessage.setTypeface(questrial);
        numInstances.setTypeface(questrial);
        numInstances.setTypeface(questrial);

        configBarChart();
        countGroups("");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsFragment.innerViewPager.setCurrentItem(Constants.PIE_CHART);

            }
        });

        return view;
    }

    public void countGroups (final String zipCode) {
        new AsyncTask<Void, Void, int[]>() {
            @Override
            protected int[] doInBackground(Void... params) {
                ShameSQLiteHelper helper = ShameSQLiteHelper.getInstance(getActivity());
                return helper.countGroups(zipCode);
            }
            @Override
            protected void onPostExecute(int[] countTypes) {
                numWomen = countTypes[0];
                numPOC = countTypes[1];
                numLGBTQ = countTypes[2];
                numMinor = countTypes[3];
                numOther = countTypes[4];
                Data data = setBars(numWomen, numPOC, numLGBTQ, numMinor, numOther);
                setDataBarChart(data.getyVals(), data.getxValues());
                //no harassment instances reported in the area
                if (numWomen == 0 && numMinor == 0 && numPOC == 0 && numLGBTQ == 0 && numOther == 0) {
                    barChart.setVisibility(View.GONE);
                    header.setVisibility(View.GONE);
                    numInstances.setVisibility(View.GONE);
                    noHarassmentMessage.setVisibility(View.VISIBLE);
                }
                //data available
                else {
                    animateChart();
                    noHarassmentMessage.setVisibility(View.GONE);
                    barChart.setVisibility(View.VISIBLE);
                    numInstances.setVisibility(View.VISIBLE);
                    header.setVisibility(View.VISIBLE);
                    int totalInstances = numWomen + numPOC + numLGBTQ + numMinor + numOther;
                    numInstances.setText(getString(R.string.total_instances) + " " + totalInstances);
                }

            }
        }.execute();
    }

    //displays info about groups of harassment
    public void getCountGroups(String zipCode) {
        numWomen = 0;
        numPOC = 0;
        numLGBTQ = 0;
        numMinor = 0;
        numOther = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.SHAME);
        if (zipCode.length() > 0) {
            query.whereEqualTo(Constants.SHAME_ZIPCODE_COLUMN, zipCode);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects != null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        if (objects.get(i).get(Constants.GROUP_COLUMN) != null) {
                            if (objects.get(i).get(Constants.GROUP_COLUMN).equals(Constants.WOMAN)) {
                                numWomen++;
                            } else if (objects.get(i).get(Constants.GROUP_COLUMN).equals(Constants.LGBTQ)) {
                                numLGBTQ++;
                            } else if (objects.get(i).get(Constants.GROUP_COLUMN).equals(Constants.POC)) {
                                numPOC++;
                            } else {
                                numMinor++;
                            }
                        }
                    }

                    Data data = setBars(numWomen, numPOC, numLGBTQ, numMinor, numOther);
                    setDataBarChart(data.getyVals(), data.getxValues());
                    //no harassment instances reported in the area
                    if (numWomen == 0 && numMinor == 0 && numPOC == 0 && numLGBTQ == 0) {
                        barChart.setVisibility(View.GONE);
                        header.setVisibility(View.GONE);
                        numInstances.setVisibility(View.GONE);
                        noHarassmentMessage.setVisibility(View.VISIBLE);
                    }
                    //data available
                    else {
                        animateChart();
                        noHarassmentMessage.setVisibility(View.GONE);
                        barChart.setVisibility(View.VISIBLE);
                        numInstances.setVisibility(View.VISIBLE);
                        header.setVisibility(View.VISIBLE);
                        int totalInstances = numWomen + numPOC + numLGBTQ + numMinor;
                        numInstances.setText(getString(R.string.total_instances) + " " + totalInstances);
                    }
                }
            }
        });
    }

    //configures the characteristics of the chart
    public void configBarChart() {
        barChart.setDescription("");
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setGridBackgroundColor(Color.WHITE);

        XAxis xl = barChart.getXAxis();
        xl.setTextSize(11);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTextColor(Color.BLACK);
        xl.setAdjustXLabels(true);
        xl.setTypeface(questrial);

        YAxis yl = barChart.getAxisLeft();
        yl.setEnabled(false);

        YAxis yr = barChart.getAxisRight();
        yr.setEnabled(false);

        Legend l = barChart.getLegend();
        l.setEnabled(false);
    }

    //sets the data on the configured chart
    private void setDataBarChart(ArrayList<BarEntry> yVals, ArrayList<String> xVals) {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(android.R.color.holo_red_dark));

        BarDataSet set = new BarDataSet(yVals, "");
        set.setColors(colors);
        BarData data = new BarData(xVals, set);
        data.setValueTextSize(14);
        data.setValueTypeface(questrial);
        data.setValueTextColor(Color.BLACK);
        barChart.setData(data);
    }

    //animates the cart
    public void animateChart() {
        barChart.animateY(2000);
    }

    //sets up the number of bars in the chart
    private Data setBars(int numWomen, int numPOC, int numLGBTQ, int numMinor, int numOther) {
        int count = 0;
        //presents info in %
        float sum = numWomen + numPOC + numLGBTQ + numMinor + numOther;
        float womenPerCent = numWomen / sum * 100;
        float POCPerCent = numPOC / sum * 100;
        float LGBTQPerCent = numLGBTQ / sum * 100;
        float minorPerCent = numMinor / sum * 100;
        float otherPerCent = numOther / sum * 100;

        ArrayList<Bar> bars = new ArrayList<>();
        ArrayList<BarEntry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        bars.add(new Bar(Constants.WOMEN, womenPerCent));
        bars.add(new Bar(Constants.POC, POCPerCent));
        bars.add(new Bar(Constants.LGBTQ, LGBTQPerCent));
        bars.add(new Bar(Constants.MINOR, minorPerCent));
        bars.add(new Bar(Constants.OTHER, otherPerCent));

        for (int i = 0; i < bars.size(); i++) {
            if (bars.get(i).getPerCent() != 0) {
                yVals.add(new BarEntry(bars.get(i).getPerCent(), count));
                xVals.add(bars.get(i).getName());
                count ++;
            }
        }
        return new Data(yVals, xVals);
    }
}
