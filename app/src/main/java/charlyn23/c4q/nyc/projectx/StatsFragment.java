package charlyn23.c4q.nyc.projectx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {
    private PieChart chart;
    private int numVerbalShame;
    private int numPhysicalShame;
    private int numOtherShame;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment, container, false);

        chart = (PieChart) view.findViewById(R.id.pie_chart);
        configureChart(chart);
        getCountShameTypes();
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
                        if (objects.get(i).get("shameType") == 1) {
                            numVerbalShame++;
                        } else if (objects.get(i).get("shameType") == 2) {
                            numPhysicalShame++;
                        } else {
                            numOtherShame++;
                        }
                    }
                    Log.d("yuliya", numVerbalShame + "" );
                    setData(chart);
                }
            }
        });
   }

    public PieChart configureChart(PieChart chart) {
        chart.setHoleColor(getResources().getColor(android.R.color.background_dark));
        chart.setHoleRadius(60f);
        chart.setDescription("");
        chart.setTransparentCircleRadius(5f);
        //chart.setDrawYValues(true);
        chart.setDrawCenterText(true);
        chart.setDrawHoleEnabled(false);
        chart.setRotationAngle(0);
        //chart.setDrawXValues(false);
        chart.setRotationEnabled(true);
        chart.setUsePercentValues(true);
        chart.setCenterText("Groups of people harassed");
        return chart;
    }

    private PieChart setData(PieChart chart) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        yVals1.add(new Entry(numVerbalShame, 0));
        yVals1.add(new Entry(numPhysicalShame, 1));
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("verbal");
        xVals.add("physical");
        PieDataSet set1 = new PieDataSet(yVals1, "");
        set1.setSliceSpace(0f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(android.R.color.holo_green_light));
        colors.add(getResources().getColor(android.R.color.holo_red_light));
        set1.setColors(colors);
        PieData data = new PieData(xVals, set1);
        chart.setData(data);
        chart.highlightValues(null);
        chart.animateY(1000);
        chart.invalidate();
        return chart;
    }
}
