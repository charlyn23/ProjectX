package charlyn23.c4q.nyc.projectx.stats;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.AnimationEasing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;

import charlyn23.c4q.nyc.projectx.Constants;
import charlyn23.c4q.nyc.projectx.MainActivity;
import charlyn23.c4q.nyc.projectx.R;
import charlyn23.c4q.nyc.projectx.map.ShameSQLiteHelper;
import charlyn23.c4q.nyc.projectx.shames.Shame;

public class PieChartFragment extends Fragment {
    public PieChart pieChart;
    private int numVerbalShame;
    private int numPhysicalShame;
    private int numOtherShame;
    private Typeface questrial;
    private TextView numInstances;
    private TextView header;
    private TextView noHarassmentMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pie_chart_fragment, container, false);
        MaterialIconView next = (MaterialIconView) view.findViewById(R.id.next);
        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        header = (TextView) view.findViewById(R.id.chart_header);
        noHarassmentMessage = (TextView) view.findViewById(R.id.no_harassment_message);
        numInstances = (TextView) view.findViewById(R.id.instances);

        questrial = Typeface.createFromAsset(getActivity().getAssets(), "questrial.ttf");
        header.setTypeface(questrial);
        noHarassmentMessage.setTypeface(questrial);
        numInstances.setTypeface(questrial);

        configPieChart();
        countShameTypes("");

        //switches to the next stats fragment
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsFragment.innerViewPager.setCurrentItem(Constants.BAR_CHART);
            }
        });


        return view;
    }

    public void countShameTypes(final String zipCode) {
        new AsyncTask<Void, Void, int[]>() {
            @Override
            protected int[] doInBackground(Void... params) {
                ShameSQLiteHelper helper = ShameSQLiteHelper.getInstance(getActivity());
                return helper.countTypes(zipCode);
            }
            @Override
            protected void onPostExecute(int[] countTypes) {
                numVerbalShame = countTypes[0];
                numPhysicalShame = countTypes[1];
                numOtherShame = countTypes[2];
                Data data = setBars(numVerbalShame, numPhysicalShame, numOtherShame);
                setDataPieChart(data.getyValues(), data.getxValues());
                //no harassment instances reported in the area
                if (numVerbalShame == 0 && numPhysicalShame == 0 && numOtherShame == 0) {
                    pieChart.setVisibility(View.GONE);
                    header.setVisibility(View.GONE);
                    numInstances.setVisibility(View.GONE);
                    noHarassmentMessage.setVisibility(View.VISIBLE);
                }
                //data available
                else {
                    animateChart();
                    pieChart.setVisibility(View.VISIBLE);
                    noHarassmentMessage.setVisibility(View.GONE);
                    numInstances.setVisibility(View.VISIBLE);
                    header.setVisibility(View.VISIBLE);
                    int totalInstances = numVerbalShame + numPhysicalShame + numOtherShame;
                    numInstances.setText(getString(R.string.total_instances) + " " + totalInstances);
                }

            }
        }.execute();
    }

    //configures the characteristics of the chart
    private void configPieChart() {
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(60);
        pieChart.setDrawCenterText(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setDescription("");
        pieChart.setRotationAngle(90);
        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);
    }

    //sets the data on the configured chart
    private void setDataPieChart(ArrayList<Entry> yVals, ArrayList<String> xVals) {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(android.R.color.holo_red_dark));

        PieDataSet pieChartSet = new PieDataSet(yVals, "");
        pieChartSet.setSliceSpace(2);
        pieChartSet.setColors(colors);

        PieData data = new PieData(xVals, pieChartSet);
        data.setValueTextSize(14);
        data.setValueTypeface(questrial);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        //disables the legend
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
    }

    public void animateChart() {
        pieChart.animateY(2000);
    }

    //sets up the number of bars in the chart
    private Data setBars(int numVerbalShame, int numPhysicalShame, int numOtherShame) {
        int count = 0;
        ArrayList<Bar> bars = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        bars.add(new Bar(Constants.VERBAL, numVerbalShame));
        bars.add(new Bar(Constants.PHYSICAL, numPhysicalShame));
        bars.add(new Bar(Constants.OTHER, numOtherShame));

        for (int i = 0; i < bars.size(); i++) {
            if (bars.get(i).getPerCent() != 0) {
                yVals.add(new BarEntry(bars.get(i).getPerCent(), count));
                xVals.add(bars.get(i).getName());
                count ++;
            }
        }
        return new Data(Constants.PIE_CHART_NAME, yVals, xVals);
    }
}
