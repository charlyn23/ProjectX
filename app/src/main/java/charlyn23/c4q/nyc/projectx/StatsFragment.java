package charlyn23.c4q.nyc.projectx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.google.android.gms.maps.SupportMapFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {
    private PieChart pieChart;
    private TextView detailView;
    private int numVerbalShame;
    private int numPhysicalShame;
    private int numOtherShame;
    private int numWomen;
    private int numPOC;
    private int numLGBTQ;
    private int numMinor;
    private ArrayList<Integer> colors;
    PieData data;
    ArrayList<String> xVals;
    PieDataSet pieChartSet;
    boolean changeColorIndex = false;
    protected HorizontalBarChart barChart;
    private Typeface tf;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment, container, false);

        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        barChart = (HorizontalBarChart) view.findViewById(R.id.bar_chart);
        //detailView = (TextView) view.findViewById(R.id.detail_view);

        configPieChart(pieChart);
        configBarChart(barChart);
        getCountShameTypes();

//            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//                @Override
//                public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//                    colors.clear();
//                    if (!changeColorIndex) {
//                        if (h.getXIndex() == 0) {
//                            colors.add(getResources().getColor(android.R.color.holo_blue_dark));
//                            colors.add(getResources().getColor(android.R.color.holo_red_dark));
//                            colors.add(getResources().getColor(android.R.color.holo_red_dark));
//                            detailView.setText(numVerbalShame + " instances of verbal harassment");
//                            changeColorIndex = true;
//
//                        } else if (h.getXIndex() == 1) {
//                            colors.add(getResources().getColor(android.R.color.holo_red_dark));
//                            colors.add(getResources().getColor(android.R.color.holo_green_dark));
//                            colors.add(getResources().getColor(android.R.color.holo_red_dark));
//                            detailView.setText(numPhysicalShame + " instances of physical harassment");
//                            changeColorIndex = true;
//                        } else {
//                            colors.add(getResources().getColor(android.R.color.holo_red_dark));
//                            colors.add(getResources().getColor(android.R.color.holo_red_dark));
//                            colors.add(getResources().getColor(android.R.color.holo_orange_dark));
//                            detailView.setText(numOtherShame + " instances of other harassment");
//                            changeColorIndex = true;
//                        }
//                    } else {
//                        colors.add(getResources().getColor(android.R.color.holo_red_dark));
//                        colors.add(getResources().getColor(android.R.color.holo_red_dark));
//                        colors.add(getResources().getColor(android.R.color.holo_red_dark));
//                        changeColorIndex = false;
//                    }
//                    pieChartSet.setColors(colors);
//                    PieData data = new PieData(xVals, pieChartSet);
//                    pieChart.setData(data);
//                }

//                @Override
//                public void onNothingSelected() {
//
//                }
//            });

//        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//                if (e == null)
//                    return;
//
//                RectF bounds = barChart.getBarBounds((BarEntry) e);
//                PointF position = barChart.getPosition(e, barChart.getData().getDataSetByIndex(dataSetIndex)
//                        .getAxisDependency());
//
//                Log.i("bounds", bounds.toString());
//                Log.i("position", position.toString());
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        });
        getCountGroups();

        return view;
    }


    public void getCountShameTypes() {
        numVerbalShame = 0;
        numPhysicalShame = 0;
        numOtherShame = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shame");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        if (objects.get(i).get("shameType") == null) {
                            continue;
                        }
                        else if (objects.get(i).get("shameType").equals("verbal")) {
                            numVerbalShame++;
                        } else if (objects.get(i).get("shameType").equals("physical")) {
                            numPhysicalShame++;
                        } else{
                            numOtherShame++;
                        }
                    }
                    Log.d("yuliya", numVerbalShame + "" );
                    Log.d("yuliya", numPhysicalShame + "" );
                    Log.d("yuliya", numOtherShame + "" );

                    setDataPieChart(pieChart);
                }
            }
        });
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

    public PieChart configPieChart(PieChart chart) {
        chart.setHoleColorTransparent(true);
        chart.setHoleRadius(60f);
        chart.setDrawCenterText(true);
        chart.setDrawHoleEnabled(true);
        chart.setDescription("");
        chart.setTransparentCircleRadius(5f);
        //pieChart.setDrawYValues(true);
        chart.setDrawCenterText(true);
        chart.setDrawHoleEnabled(true);
        chart.setRotationAngle(0);
        //pieChart.setDrawXValues(false);
        chart.setRotationEnabled(true);
        chart.setUsePercentValues(true);
        chart.setCenterText(getString(R.string.types_of_harassment));
        return chart;
    }

    public BarChart configBarChart(BarChart barChart) {
        barChart.setHighlightEnabled(true);

        barChart.setDrawValueAboveBar(true);

//        barChart.setDescription("Groups of people");

        // if more than 60 entries are displayed in the pieChart, no values will be
        // drawn
        //barChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        barChart.setDrawBarShadow(true);

        // barChart.setDrawXLabels(false);

        barChart.setDrawGridBackground(false);

        // barChart.setDrawYLabels(false);

        //tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xl = barChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGridLineWidth(0.3f);

        YAxis yl = barChart.getAxisLeft();
        yl.setTypeface(tf);
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

    private PieChart setDataPieChart(PieChart chart) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        yVals1.add(new Entry(numVerbalShame, 0));
        yVals1.add(new Entry(numPhysicalShame, 1));
        yVals1.add(new Entry(numOtherShame, 2));

        xVals = new ArrayList<String>();
        xVals.add("verbal");
        xVals.add("physical");
        xVals.add("other");

        pieChartSet = new PieDataSet(yVals1, "");
        pieChartSet.setSliceSpace(3f);
        colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(android.R.color.holo_red_dark));
        //colors.add(getResources().getColor(android.R.color.holo_red_light));
        pieChartSet.setColors(colors);
        PieData data = new PieData(xVals, pieChartSet);
        data.setValueTextSize(15f);
        chart.setData(data);
        //pieChart.highlightValues(null);
        chart.animateY(2000);
        Legend l = chart.getLegend();
        l.setEnabled(false);
        chart.invalidate();
        return chart;
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


//    @SuppressLint("NewApi")

//    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//
//        if (e == null)
//            return;
//
//        RectF bounds = barChart.getBarBounds((BarEntry) e);
//        PointF position = barChart.getPosition(e, barChart.getData().getDataSetByIndex(dataSetIndex)
//                .getAxisDependency());
//
//        Log.i("bounds", bounds.toString());
//        Log.i("position", position.toString());
//    }
//
//    public void onNothingSelected() {
//    };
}
