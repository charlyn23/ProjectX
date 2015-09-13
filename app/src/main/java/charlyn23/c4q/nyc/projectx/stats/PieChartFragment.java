package charlyn23.c4q.nyc.projectx.stats;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.AnimationEasing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;

import charlyn23.c4q.nyc.projectx.Constants;
import charlyn23.c4q.nyc.projectx.R;

public class PieChartFragment extends Fragment {
    public PieChart pieChart;
    private int numVerbalShame;
    private int numPhysicalShame;
    private int numOtherShame;
    private Typeface questrial;
    private TextView numInstances;
    private LinearLayout parent;
    private TextView header;
    private TextView noHarassmentMessage;
    private CardView card;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pie_chart_fragment, container, false);
        questrial = Typeface.createFromAsset(getActivity().getAssets(), "questrial.ttf");
        card = (CardView) view.findViewById(R.id.card_view);
        parent = (LinearLayout) view.findViewById(R.id.parent_layout);
        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        MaterialIconView next = (MaterialIconView) view.findViewById(R.id.next);
        header = (TextView) view.findViewById(R.id.chart_header);
        noHarassmentMessage = (TextView) view.findViewById(R.id.no_harassment_message);
        numInstances = (TextView) view.findViewById(R.id.instances);

        header.setTypeface(questrial);
        noHarassmentMessage.setTypeface(questrial);
        numInstances.setTypeface(questrial);
        configPieChart();

        //switches to the next stats fragment
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsFragment.innerViewPager.setCurrentItem(Constants.BAR_CHART);
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

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.SHAME);
        if (zipCode.length() > 0) {
            query.whereEqualTo(Constants.SHAME_ZIPCODE_COLUMN, zipCode);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects != null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        if (objects.get(i).get(Constants.SHAME_TYPE_COLUMN) != null) {
                            if (objects.get(i).get(Constants.SHAME_TYPE_COLUMN).equals(Constants.VERBAL)) {
                                numVerbalShame++;
                            } else if (objects.get(i).get(Constants.SHAME_TYPE_COLUMN).equals(Constants.PHYSICAL)) {
                                numPhysicalShame++;
                            } else {
                                numOtherShame++;
                            }
                        }
                    }

                    Data data = setBars(numVerbalShame, numPhysicalShame, numOtherShame);
                    setDataPieChart(pieChart, data.getyValues(), data.getxValues());
                    if (numVerbalShame == 0 && numPhysicalShame == 0 && numOtherShame == 0) {
                        pieChart.invalidate();
                        header.setVisibility(View.GONE);
                        numInstances.setVisibility(View.GONE);
                        noHarassmentMessage.setVisibility(View.VISIBLE);
                    } else {
                        int totalInstances = numVerbalShame + numPhysicalShame + numOtherShame;
                        numInstances.setVisibility(View.VISIBLE);
                        numInstances.setText(getString(R.string.total_instances) + " " + totalInstances);
                        header.setVisibility(View.VISIBLE);
                        noHarassmentMessage.setVisibility(View.GONE);
                        animateChart();
                    }
                }
            }
        });
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
    private PieChart setDataPieChart(PieChart chart, ArrayList<Entry> yVals, ArrayList<String> xVals) {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(android.R.color.holo_red_dark));

        PieDataSet pieChartSet = new PieDataSet(yVals, "");
        pieChartSet.setSliceSpace(2);
        pieChartSet.setColors(colors);

        PieData data = new PieData(xVals, pieChartSet);
        data.setValueTextSize(13);
        data.setValueTypeface(questrial);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);
        //chart.highlightValues(null);

        //disables the legend
        Legend l = chart.getLegend();
        l.setEnabled(false);
        return chart;
    }

    public void animateChart() {
        pieChart.animateY(2000);
    }

    //sets up the number of y values to display in the chart depending on the type of data available in db
    private Data setBars(int numVerbalShame, int numPhysicalShame, int numOtherShame) {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        if (numOtherShame == 0 && numPhysicalShame == 0) {
            yVals.add(new Entry(numVerbalShame, 0));
            xVals.add(Constants.VERBAL);
        } else if (numOtherShame == 0 && numVerbalShame == 0) {
            yVals.add(new Entry(numPhysicalShame, 0));
            xVals.add(Constants.PHYSICAL);
        } else if (numPhysicalShame == 0 && numVerbalShame == 0) {
            yVals.add(new Entry(numOtherShame, 0));
            xVals.add(Constants.OTHER);
        } else if (numVerbalShame == 0) {
            yVals.add(new Entry(numPhysicalShame, 0));
            yVals.add(new Entry(numOtherShame, 1));
            xVals.add(Constants.PHYSICAL);
            xVals.add(Constants.OTHER);
        } else if (numPhysicalShame == 0) {
            yVals.add(new Entry(numVerbalShame, 0));
            yVals.add(new Entry(numOtherShame, 1));
            xVals.add(Constants.VERBAL);
            xVals.add(Constants.OTHER);
        } else if (numOtherShame == 0) {
            yVals.add(new Entry(numVerbalShame, 0));
            yVals.add(new Entry(numPhysicalShame, 1));
            xVals.add(Constants.VERBAL);
            xVals.add(Constants.PHYSICAL);
        } else {
            yVals.add(new Entry(numVerbalShame, 0));
            yVals.add(new Entry(numPhysicalShame, 1));
            yVals.add(new Entry(numOtherShame, 2));
            xVals.add(Constants.VERBAL);
            xVals.add(Constants.PHYSICAL);
            xVals.add(Constants.OTHER);
        }
        return new Data(Constants.PIE_CHART_NAME, yVals, xVals);
    }
}
