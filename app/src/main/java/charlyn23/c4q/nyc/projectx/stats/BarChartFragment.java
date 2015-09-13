package charlyn23.c4q.nyc.projectx.stats;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
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

public class BarChartFragment extends android.support.v4.app.Fragment {
    private int numWomen;
    private int numPOC;
    private int numLGBTQ;
    private int numMinor;
    private BarChart barChart;
    private Typeface questrial;
    private TextView numInstances;
    private TextView header;
    private CardView card;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bar_chart_fragment, container, false);
        questrial = Typeface.createFromAsset(getActivity().getAssets(), "questrial.ttf");
        card = (CardView) view.findViewById(R.id.card_view);
        header = (TextView) view.findViewById(R.id.chart_header);
        ImageView next = (ImageView) view.findViewById(R.id.back);
        numInstances = (TextView) view.findViewById(R.id.instances);
        numInstances.setTypeface(questrial);
        header.setTypeface(questrial);
        numInstances.setTypeface(questrial);
        barChart = (BarChart) view.findViewById(R.id.bar_chart);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsFragment.innerViewPager.setCurrentItem(Constants.PIE_CHART);

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
                    int totalInstances = numWomen + numPOC + numLGBTQ + numMinor;
                    numInstances.setText(getResources().getString(R.string.total_instances) + " " + totalInstances);

                    Data data = setBars(numWomen, numPOC, numLGBTQ, numMinor);
                    setDataBarChart(data.getyVals(), data.getxValues());
                    if (numWomen == 0 && numMinor == 0 && numPOC == 0 && numLGBTQ == 0) {
                        setNoHarassmentStyleCard();
                    }
                    else {
                        configureDefaultCardStyle();
                        animateChart();
                    }
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
        barChart.setDrawValueAboveBar(true);

        XAxis xl = barChart.getXAxis();
        xl.setTextSize(13);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTextColor(Color.BLACK);
        xl.setAdjustXLabels(true);
        xl.setTypeface(questrial);

        YAxis yl = barChart.getAxisLeft();
        yl.setEnabled(false);
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setTypeface(questrial);
        yl.setTextColor(Color.WHITE);

        YAxis yr = barChart.getAxisRight();
        yr.setAxisMaxValue(100);
        yr.setTextSize(13);
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
        yr.setEnabled(false);
        yr.setTypeface(questrial);
        yr.setTextColor(Color.WHITE);

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
        data.setValueTypeface(questrial);
        data.setValueTextColor(Color.BLACK);
        barChart.setData(data);
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
                    if (numMinor != 0) {
                        yVals.add(new BarEntry(minorPerCent, 0));
                        xVals.add(Constants.MINOR);
                    }
                }
                else {
                    if (numMinor == 0) {
                        yVals.add(new BarEntry(LGBTQPerCent, 0));
                        xVals.add(Constants.LGBTQ);
                    }
                    else {
                        yVals.add(new BarEntry(LGBTQPerCent, 0));
                        yVals.add(new BarEntry(minorPerCent, 1));
                        xVals.add(Constants.LGBTQ);
                        xVals.add(Constants.MINOR);
                    }
                }
            }
            else if (numLGBTQ == 0) {
                if (numMinor == 0) {
                    yVals.add(new BarEntry(POCPerCent, 0));
                    xVals.add(Constants.POC);
                }
                else {
                    yVals.add(new BarEntry(POCPerCent, 0));
                    yVals.add(new BarEntry(minorPerCent, 1));
                    xVals.add(Constants.POC);
                    xVals.add(Constants.MINOR);
                }
            }
            else if (numMinor == 0) {
                yVals.add(new BarEntry(POCPerCent, 0));
                yVals.add(new BarEntry(LGBTQPerCent, 1));
                xVals.add(Constants.POC);
                xVals.add(Constants.LGBTQ);
            }
            else {
                yVals.add(new BarEntry(POCPerCent, 0));
                yVals.add(new BarEntry(LGBTQPerCent, 1));
                yVals.add(new BarEntry(minorPerCent, 2));
                xVals.add(Constants.POC);
                xVals.add(Constants.LGBTQ);
                xVals.add(Constants.MINOR);
            }
        }

        else if (numPOC == 0) {
            if (numLGBTQ == 0) {
                if (numMinor == 0) {
                    yVals.add(new BarEntry(womenPerCent, 0));
                    xVals.add(Constants.WOMEN);
                }
                else {
                    yVals.add(new BarEntry(womenPerCent, 0));
                    yVals.add(new BarEntry(minorPerCent, 1));
                    xVals.add(Constants.WOMEN);
                    xVals.add(Constants.MINOR);
                }
            }
            else if (numMinor == 0) {
                yVals.add(new BarEntry(womenPerCent, 0));
                yVals.add(new BarEntry(LGBTQPerCent, 1));
                xVals.add(Constants.WOMEN);
                xVals.add(Constants.LGBTQ);
            }
            else {
                yVals.add(new BarEntry(womenPerCent, 0));
                yVals.add(new BarEntry(LGBTQPerCent, 1));
                yVals.add(new BarEntry(minorPerCent, 2));
                xVals.add(Constants.WOMEN);
                xVals.add(Constants.LGBTQ);
                xVals.add(Constants.MINOR);
            }
        }

        else if (numLGBTQ == 0) {
            if (numMinor == 0) {
                yVals.add(new BarEntry(womenPerCent, 0));
                yVals.add(new BarEntry(POCPerCent, 1));
                xVals.add(Constants.WOMEN);
                xVals.add(Constants.POC);
            }
            else {
                yVals.add(new BarEntry(womenPerCent, 0));
                yVals.add(new BarEntry(POCPerCent, 1));
                yVals.add(new BarEntry(minorPerCent, 2));
                xVals.add(Constants.WOMEN);
                xVals.add(Constants.POC);
                xVals.add(Constants.MINOR);
            }
        }

        else if (numMinor == 0) {
            yVals.add(new BarEntry(womenPerCent, 0));
            yVals.add(new BarEntry(POCPerCent, 1));
            yVals.add(new BarEntry(LGBTQPerCent, 2));
            xVals.add(Constants.WOMEN);
            xVals.add(Constants.POC);
            xVals.add(Constants.LGBTQ);
        }

        else {
            yVals.add(new BarEntry(womenPerCent, 0));
            yVals.add(new BarEntry(POCPerCent, 1));
            yVals.add(new BarEntry(LGBTQPerCent, 2));
            yVals.add(new BarEntry(minorPerCent, 3));
            xVals.add(Constants.WOMEN);
            xVals.add(Constants.POC);
            xVals.add(Constants.LGBTQ);
            xVals.add(Constants.MINOR);
        }
        Data data = new Data(yVals, xVals);
        return data;
    }

    //sets the default card style
    public void configureDefaultCardStyle() {
        header.setText(getString(R.string.groups));
        header.setGravity(Gravity.TOP);
        header.setGravity(Gravity.CENTER);
        header.setPadding(0, 9, 0, 0);
        header.setTextColor(getResources().getColor(R.color.text_black));
        barChart.setVisibility(View.VISIBLE);
    }

    //changes the card style when harassment is not reported in the area
    private void setNoHarassmentStyleCard() {
        barChart.setVisibility(View.GONE);
        card.setCardBackgroundColor(Color.parseColor("#ffffff"));
        card.setRadius(10);
        header.setText(getResources().getString(R.string.no_harassment));
        header.setTextColor(getResources().getColor(R.color.primary_dark));
        header.setTextSize(17);
        header.setGravity(Gravity.CENTER);
        header.setPadding(55, 290, 55, 0);
        numInstances.setText("");
    }
}
