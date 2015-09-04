package charlyn23.c4q.nyc.projectx.stats;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

import charlyn23.c4q.nyc.projectx.R;

public class PieChartFragment extends Fragment {
    private static final String SHAME = "Shame";
    private static final String ZIPCODE = "zipCode";
    private static final String VERBAL = "verbal";
    private static final String PHYSICAL = "physical";
    private static final String OTHER = "other";
    private static final String SHAME_TYPE = "shameType";
    private static final String PIE_CHART = "pieChart";
    private static final int BAR_CHART = 1;

    private PieChart pieChart;
    private int numVerbalShame;
    private int numPhysicalShame;
    private int numOtherShame;
    private Typeface questrial;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pie_chart_fragment, container, false);
        questrial = Typeface.createFromAsset(getActivity().getAssets(), "questrial.ttf");
        TextView next = (TextView) view.findViewById(R.id.next);
        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        configPieChart(pieChart);

        //switches to the next stats fragment
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsFragment.innerViewPager.setCurrentItem(BAR_CHART);
            }
        });

        //displays the general info about instances of harassment
        getCountShameTypes("");
        return view;
    }

    public void getCountShameTypes(String zipCode) {
        numVerbalShame = 0;
        numPhysicalShame = 0;
        numOtherShame = 0;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(SHAME);
        if (zipCode.length() > 0) {
            query.whereEqualTo(ZIPCODE, zipCode);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects != null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        if (objects.get(i).get(SHAME_TYPE) != null) {
                            if (objects.get(i).get(SHAME_TYPE).equals(VERBAL)) {
                                numVerbalShame++;
                            } else if (objects.get(i).get(SHAME_TYPE).equals(PHYSICAL)) {
                                numPhysicalShame++;
                            } else {
                                numOtherShame++;
                            }
                        }
                    }
                    Log.d("yuliya", numVerbalShame + "");
                    Log.d("yuliya", numPhysicalShame + "");
                    Log.d("yuliya", numOtherShame + "");

                    //displays a toast when there are no cases reported in the area
                    if (numVerbalShame == 0 && numPhysicalShame == 0 && numOtherShame == 0) {
                        Toast.makeText(getActivity(), "Cases of harassment have not been reported in your area!", Toast.LENGTH_LONG).show();
                    }
                    Data data = setBars(numVerbalShame, numPhysicalShame, numOtherShame);
                    setDataPieChart(pieChart, data.getyValues(), data.getxValues());
                }
            }
        });
    }

    //configures the characteristics of the chart
    private PieChart configPieChart(PieChart chart) {
        chart.setHoleColorTransparent(true);
        chart.setHoleRadius(60);
        chart.setDrawCenterText(true);
        chart.setDrawHoleEnabled(true);
        chart.setDescription("");
        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setUsePercentValues(true);
        chart.setCenterText(getString(R.string.types_of_harassment));
        chart.setCenterTextTypeface(questrial);
        chart.setCenterTextSize(17);
        return chart;
    }

    //sets the data on the configured chart
    private PieChart setDataPieChart(PieChart chart, ArrayList<Entry> yVals, ArrayList<String> xVals) {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(android.R.color.holo_red_dark));
        //colors.add(getResources().getColor(android.R.color.holo_red_light));

        PieDataSet pieChartSet = new PieDataSet(yVals, "");
        pieChartSet.setSliceSpace(2);
        pieChartSet.setColors(colors);

        PieData data = new PieData(xVals, pieChartSet);
        data.setValueTextSize(13);
        data.setValueTypeface(questrial);
        chart.setData(data);
        chart.highlightValues(null);
        chart.animateY(2000);

        //disables the legend
        Legend l = chart.getLegend();
        l.setEnabled(false);
        return chart;
    }

    public void animateChart() {
        if (pieChart == null) {
            return;
        }
        pieChart.animateY(2000);
    }

    //sets up the number of y values to display in the chart depending on the type of data available in db
    private Data setBars(int numVerbalShame, int numPhysicalShame, int numOtherShame) {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        if (numOtherShame == 0 && numPhysicalShame == 0) {
            yVals.add(new Entry(numVerbalShame, 0));
            xVals.add(VERBAL);
        }

        else if (numOtherShame == 0 && numVerbalShame == 0) {
            yVals.add(new Entry(numPhysicalShame, 0));
            xVals.add(PHYSICAL);
        }

        else if (numPhysicalShame == 0 && numVerbalShame == 0) {
            yVals.add(new Entry(numOtherShame, 0));
            xVals.add(OTHER);
        }

        else if (numVerbalShame == 0) {
            yVals.add(new Entry(numPhysicalShame, 0));
            yVals.add(new Entry(numOtherShame, 1));
            xVals.add(PHYSICAL);
            xVals.add(OTHER);
        }

        else if (numPhysicalShame == 0) {
            yVals.add(new Entry(numVerbalShame, 0));
            yVals.add(new Entry(numOtherShame, 1));
            xVals.add(VERBAL);
            xVals.add(OTHER);
        }
        else if (numOtherShame == 0) {
            yVals.add(new Entry(numVerbalShame, 0));
            yVals.add(new Entry(numPhysicalShame, 1));
            xVals.add(VERBAL);
            xVals.add(PHYSICAL);
        }

        else {
            yVals.add(new Entry(numVerbalShame, 0));
            yVals.add(new Entry(numPhysicalShame, 1));
            yVals.add(new Entry(numOtherShame, 2));
            xVals.add(VERBAL);
            xVals.add(PHYSICAL);
            xVals.add(OTHER);
        }
        Data data = new Data(PIE_CHART, yVals, xVals);
        return data;
    }
}
