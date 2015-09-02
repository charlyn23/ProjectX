package charlyn23.c4q.nyc.projectx.stats;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import charlyn23.c4q.nyc.projectx.R;

public class BarChartFragment extends android.support.v4.app.Fragment {
    private static final String SHAME = "Shame";
    private static final String ZIPCODE = "zipCode";
    private static final String LGBTQ = "LGBTQ";
    private static final String POC = "POC";
    private static final String WOMEN = "women";
    private static final String WOMAN = "woman";
    private static final String MINOR = "minor";
    private static final String GROUP = "Group";
    private static final int PIE_CHART = 0;


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
                StatsFragment.innerViewPager.setCurrentItem(PIE_CHART);

            }
        });

        configBarChart(barChart);
        getCountGroups("");
        return view;
    }

    public void getCountGroups(String zipCode) {
        numWomen = 0;
        numPOC = 0;
        numLGBTQ = 0;
        numMinor = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(SHAME);
        if (zipCode.length() > 0) {
            query.whereEqualTo(ZIPCODE, zipCode);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects != null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        if (objects.get(i).get(GROUP) != null) {
                            if (objects.get(i).get(GROUP).equals(WOMAN)) {
                                numWomen++;
                            } else if (objects.get(i).get(GROUP).equals(LGBTQ)) {
                                numLGBTQ++;
                            } else if (objects.get(i).get(GROUP).equals(POC)) {
                                numPOC++;
                            } else {
                                numMinor++;
                            }
                        }
                    }
                    Log.d("yuliya", numWomen + "w ");
                    Log.d("yuliya", numPOC + " poc");
                    Log.d("yuliya", numLGBTQ + "lgbtq ");

                    if (numWomen == 0 && numMinor == 0 && numPOC == 0 && numLGBTQ == 0 ) {
                        barChart.setNoDataText("Cases of harassment have not been reported in your area!");
                    }
                    Data data = setBars(numWomen, numPOC, numLGBTQ, numMinor);
                    setDataBarChart(data.getyVals(), data.getxValues());
                }
            }
        });
    }

    //configures the characteristics of the chart
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

    //sets the data on the configured chart
    private void setDataBarChart(ArrayList<BarEntry> yVals, ArrayList<String> xVals) {
        BarDataSet set = new BarDataSet(yVals, "");
        ArrayList<Integer> colors = new ArrayList<Integer>();
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();

        colors.add(getResources().getColor(android.R.color.holo_red_dark));
        set.setColors(colors);
        dataSets.add(set);
        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(13);
        barChart.setData(data);
        barChart.animateY(2000);
    }

    //animates the cart
    public void animateChart() {
        if (barChart == null) {
            return;
        }
        barChart.animateY(2000);
    }

    //sets up the number of bars in the chart
    private Data setBars(int numWomen, int numPOC, int numLGBTQ, int numMinor) {
        //presents info in %
        float sum = numWomen + numPOC + numLGBTQ + numMinor;
        float womenPerCent = numWomen / sum * 100;
        float POCPerCent = numPOC / sum * 100;
        float LGBTQPerCent = numLGBTQ / sum * 100;
        float minorPerCent = numMinor / sum * 100;

        ArrayList<BarEntry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        //calculates how many bars to display and hides 0-value entries
        if (numWomen == 0) {
            if (numPOC == 0) {
                if (numLGBTQ == 0) {
                    yVals.add(new BarEntry(minorPerCent, 0));
                    xVals.add(MINOR);
                }
                else {
                    yVals.add(new BarEntry(LGBTQPerCent, 0));
                    yVals.add(new BarEntry(minorPerCent, 1));
                    xVals.add(LGBTQ);
                    xVals.add(MINOR);
                }
            }
            else if (numLGBTQ == 0) {
                if (numMinor == 0) {
                    yVals.add(new BarEntry(POCPerCent, 0));
                    xVals.add(POC);
//                    yVals.add(new BarEntry(null, 0));
//                    yVals.add(new BarEntry(POCPerCent, 1));
//                    yVals.add(new BarEntry(null, 2));
//                    yVals.add(new BarEntry(null, 3));
//                    xVals.add("WOMAN");
//                    xVals.add("POC");
//                    xVals.add("LGBTQ");
//                    xVals.add("MINOR");

                }
                else {
                    yVals.add(new BarEntry(POCPerCent, 0));
                    yVals.add(new BarEntry(minorPerCent, 1));
                    xVals.add(POC);
                    xVals.add(MINOR);
                }
            }
            else if (numMinor == 0) {
                yVals.add(new BarEntry(POCPerCent, 0));
                yVals.add(new BarEntry(LGBTQPerCent, 1));
                xVals.add(POC);
                xVals.add(LGBTQ);
            }
            else {
                yVals.add(new BarEntry(POCPerCent, 0));
                yVals.add(new BarEntry(LGBTQPerCent, 1));
                yVals.add(new BarEntry(minorPerCent, 2));
                xVals.add(POC);
                xVals.add(LGBTQ);
                xVals.add(MINOR);
            }
        }

        else if (numPOC == 0) {
            if (numLGBTQ == 0) {
                if (numMinor == 0) {
                    yVals.add(new BarEntry(womenPerCent, 0));
                    xVals.add(WOMEN);
                }
                else {
                    yVals.add(new BarEntry(womenPerCent, 0));
                    yVals.add(new BarEntry(minorPerCent, 1));
                    xVals.add(WOMEN);
                    xVals.add(MINOR);
                }
            }
            else if (numMinor == 0) {
                yVals.add(new BarEntry(womenPerCent, 0));
                yVals.add(new BarEntry(LGBTQPerCent, 1));
                xVals.add(WOMEN);
                xVals.add(LGBTQ);
            }
            else {
                yVals.add(new BarEntry(womenPerCent, 0));
                yVals.add(new BarEntry(LGBTQPerCent, 1));
                yVals.add(new BarEntry(minorPerCent, 2));
                xVals.add(WOMEN);
                xVals.add(LGBTQ);
                xVals.add(MINOR);
            }
        }

        else if (numLGBTQ == 0) {
            if (numMinor == 0) {
                yVals.add(new BarEntry(womenPerCent, 0));
                yVals.add(new BarEntry(POCPerCent, 1));
                xVals.add(WOMEN);
                xVals.add(POC);
            }
            else {
                yVals.add(new BarEntry(womenPerCent, 0));
                yVals.add(new BarEntry(POCPerCent, 1));
                yVals.add(new BarEntry(minorPerCent, 2));
                xVals.add(WOMEN);
                xVals.add(POC);
                xVals.add(MINOR);
            }
        }

        else if (numMinor == 0) {
            yVals.add(new BarEntry(womenPerCent, 0));
            yVals.add(new BarEntry(POCPerCent, 1));
            yVals.add(new BarEntry(LGBTQPerCent, 2));
            xVals.add(WOMEN);
            xVals.add(POC);
            xVals.add(LGBTQ);
        }

        else {
            yVals.add(new BarEntry(womenPerCent, 0));
            yVals.add(new BarEntry(POCPerCent, 1));
            yVals.add(new BarEntry(LGBTQPerCent, 2));
            yVals.add(new BarEntry(minorPerCent, 3));
            xVals.add(WOMEN);
            xVals.add(POC);
            xVals.add(LGBTQ);
            xVals.add(MINOR);
        }

        Data data = new Data(yVals, xVals);
        return data;
    }
}
