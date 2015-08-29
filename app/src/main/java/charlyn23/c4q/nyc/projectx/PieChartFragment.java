package charlyn23.c4q.nyc.projectx;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class PieChartFragment extends Fragment {
    private PieChart pieChart;
    private int numVerbalShame;
    private int numPhysicalShame;
    private int numOtherShame;
    private ArrayList<Integer> colors;
    ArrayList<String> xVals;
    PieDataSet pieChartSet;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment_pie_chart, container, false);
        TextView next = (TextView) view.findViewById(R.id.next);
        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        configPieChart(pieChart);

        //switch to the next stats fragment
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsFragment.innerViewPager.setCurrentItem(1);

            }
        });

        getCountShameTypes("");

        return view;
    }


    public void getCountShameTypes(String zipcode) {
        numVerbalShame = 0;
        numPhysicalShame = 0;
        numOtherShame = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shame");
        if (zipcode.length() > 0) {
            query.whereEqualTo("zipCode", zipcode);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        if (objects.get(i).get("shameType").equals("verbal")) {
                            numVerbalShame++;
                        } else if (objects.get(i).get("shameType").equals("physical")) {
                            numPhysicalShame++;
                        } else {
                            numOtherShame++;
                        }
                    }
                    Log.d("yuliya", numVerbalShame + "");
                    Log.d("yuliya", numPhysicalShame + "");
                    Log.d("yuliya", numOtherShame + "");
                    
                    setDataPieChart(pieChart);
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
        chart.setRotationAngle(0);
        //pieChart.setDrawXValues(false);
        chart.setRotationEnabled(true);
        chart.setUsePercentValues(true);
        chart.setCenterText(getString(R.string.types_of_harassment));
        return chart;
    }


    private PieChart setDataPieChart(PieChart chart) {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(numVerbalShame, 0));
        yVals.add(new Entry(numPhysicalShame, 1));
        yVals.add(new Entry(numOtherShame, 2));

        xVals = new ArrayList<String>();
        xVals.add("verbal");
        xVals.add("physical");
        xVals.add("other");

        pieChartSet = new PieDataSet(yVals, "");
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

    public void animateChart() {
        if (pieChart == null) {
            return;
        }
        pieChart.animateY(2000);
    }
}
