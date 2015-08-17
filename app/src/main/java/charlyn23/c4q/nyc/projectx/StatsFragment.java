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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {
    private PieChart mChart;
    private int result;
    private int numVerbalShame;
    private int numPhysicalShame;
    private int numOtherShame;
    PieDataSet dataSet;
    ArrayList<Entry> yVals1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment, container, false);

//
//        BarChart chart = (BarChart) view.findViewById(R.id.chart);
//        PieChart mChart = (PieChart) view.findViewById(R.id.pie_chart);

//        BarData data = new BarData(getXAxisValues(), getDataSet());
//        chart.setData(data);
//        chart.setDescription("My Chart");
//        chart.animateXY(2000, 2000);
//        chart.invalidate();


//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getCountShameTypes(1);


        mChart = (PieChart) view.findViewById(R.id.chart1);

        mChart.setHoleColorTransparent(true);

        mChart.setHoleRadius(60f);

        mChart.setDrawCenterText(true);

        mChart.setDrawHoleEnabled(true);

        mChart.setRotationAngle(0);

        mChart.setRotationEnabled(true);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        mChart.setCenterText("Groups of people harassed");

        yVals1 = new ArrayList<Entry>();

        yVals1.add(new Entry((float) numVerbalShame, 1));
        yVals1.add(new Entry((float) numPhysicalShame, 2));
        yVals1.add(new Entry((float) numOtherShame, 3));

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("verbal");
        xVals.add("physical");
        xVals.add("other");


        dataSet = new PieDataSet(yVals1, "Idades Porlande");
        dataSet.setSliceSpace(1f);

        PieData data = new PieData(xVals, dataSet);
        mChart.setData(data);


        return view;
    }



    //    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        new AsyncTask<Void, Void, List<Integer>>(){
//            @Override
//            protected List<Integer> doInBackground(Void... params) {
//                List<Integer> counts = new ArrayList<Integer>();
//                numVerbalShame = getCountShameTypes(1);
//                numPhysicalShame = getCountShameTypes(2);
//                numOtherShame = getCountShameTypes(3);
//                counts.add(numVerbalShame);
//                counts.add(numPhysicalShame);
//                counts.add(numOtherShame);
//
//                Log.d("yuliya", "Async is done");
//                return counts;
//            }
//
//            @Override
//            protected void onPostExecute(List<Integer> counts) {
//                Log.d("yuliya", counts.get(0) + "");
//                mChart.setHoleColorTransparent(true);
//
//                mChart.setHoleRadius(60f);
//
//                mChart.setDrawCenterText(true);
//
//                mChart.setDrawHoleEnabled(true);
//
//                mChart.setRotationAngle(0);
//
//                mChart.setRotationEnabled(true);
//
//                mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//                    @Override
//                    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//
//                    }
//
//                    @Override
//                    public void onNothingSelected() {
//
//                    }
//                });
//
//                mChart.setCenterText("Groups of people harassed");
//
//                ArrayList<Entry> yVals1 = new ArrayList<Entry>();
//
//                yVals1.add(new Entry((float) counts.get(0), 0));
//                yVals1.add(new Entry((float) counts.get(1), 1));
//                yVals1.add(new Entry((float) counts.get(2), 2));
//
//
//
//                ArrayList<String> xVals = new ArrayList<String>();
//
//                xVals.add("verbal");
//                xVals.add("physical");
//                xVals.add("other");
//
//                PieDataSet dataSet = new PieDataSet(yVals1, "Idades Porlande");
//                dataSet.setSliceSpace(3f);
//
//                PieData data = new PieData(xVals, dataSet);
//                mChart.setData(data);
//            }
//        }.execute();




    //    public boolean onCreateOptionsMenu(Menu menu) {
//        getActivity().getMenuInflater().inflate(R.menu.pie, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.actionToggleValues: {
//                for (DataSet<?> set : mChart.getData().getDataSets())
//                    set.setDrawValues(!set.isDrawValuesEnabled());
//
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleHole: {
//                if (mChart.isDrawHoleEnabled())
//                    mChart.setDrawHoleEnabled(false);
//                else
//                    mChart.setDrawHoleEnabled(true);
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionDrawCenter: {
//                if (mChart.isDrawCenterTextEnabled())
//                    mChart.setDrawCenterText(false);
//                else
//                    mChart.setDrawCenterText(true);
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleXVals: {
//
//                mChart.setDrawSliceText(!mChart.isDrawSliceTextEnabled());
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionSave: {
//                // mChart.saveToGallery("title"+System.currentTimeMillis());
//                mChart.saveToPath("title" + System.currentTimeMillis(), "");
//                break;
//            }
//            case R.id.actionTogglePercent:
//                mChart.setUsePercentValues(!mChart.isUsePercentValuesEnabled());
//                mChart.invalidate();
//                break;
//            case R.id.animateX: {
//                mChart.animateX(1800);
//                break;
//            }
//            case R.id.animateY: {
//                mChart.animateY(1800);
//                break;
//            }
//            case R.id.animateXY: {
//                mChart.animateXY(1800, 1800);
//                break;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//        tvX.setText("" + (mSeekBarX.getProgress() + 1));
//        tvY.setText("" + (mSeekBarY.getProgress()));
//
//        setData(mSeekBarX.getProgress(), mSeekBarY.getProgress());
//    }
//
//    private void setData(int count, float range) {
//
//        float mult = range;
//
//        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
//
//        // IMPORTANT: In a PieChart, no values (Entry) should have the same
//        // xIndex (even if from different DataSets), since no values can be
//        // drawn above each other.
//        for (int i = 0; i < count + 1; i++) {
//            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
//        }
//
//        ArrayList<String> xVals = new ArrayList<String>();
//
//        for (int i = 0; i < count + 1; i++)
//            xVals.add(mParties[i % mParties.length]);
//
//        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
//        dataSet.setSliceSpace(3f);
//        dataSet.setSelectionShift(5f);
//
//        // add a lot of colors
//
//        ArrayList<Integer> colors = new ArrayList<Integer>();
//
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());
//
//        dataSet.setColors(colors);
//
//        PieData data = new PieData(xVals, dataSet);
//        data.setValueFormatter(new PercentFormatter());
//        data.setValueTextSize(11f);
//        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(tf);
//        mChart.setData(data);
//
//        // undo all highlights
//        mChart.highlightValues(null);
//
//        mChart.invalidate();
//    }
//
//    @Override
//    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//
//        if (e == null)
//            return;
//        Log.i("VAL SELECTED",
//                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
//                        + ", DataSet index: " + dataSetIndex);
//    }
//
//    @Override
//    public void onNothingSelected() {
//        Log.i("PieChart", "nothing selected");
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        // TODO Auto-generated method stub
//
//    }
//
//
//    private ArrayList<BarDataSet> getDataSet() {
//        ArrayList<BarDataSet> dataSets = null;
//
//        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
//        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
//        valueSet1.add(v1e1);
//        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
//        valueSet1.add(v1e2);
//        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
//        valueSet1.add(v1e3);
//        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
//        valueSet1.add(v1e4);
//        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
//        valueSet1.add(v1e5);
//        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
//        valueSet1.add(v1e6);
//
//        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
//        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
//        valueSet2.add(v2e1);
//        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
//        valueSet2.add(v2e2);
//        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
//        valueSet2.add(v2e3);
//        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
//        valueSet2.add(v2e4);
//        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
//        valueSet2.add(v2e5);
//        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
//        valueSet2.add(v2e6);
//
//        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
//        barDataSet1.setColor(Color.rgb(0, 155, 0));
//        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
//        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        dataSets = new ArrayList<>();
//        dataSets.add(barDataSet1);
//        dataSets.add(barDataSet2);
//        return dataSets;
//    }
//
//    private ArrayList<String> getXAxisValues() {
//        ArrayList<String> xAxis = new ArrayList<>();
//        xAxis.add("JAN");
//        xAxis.add("FEB");
//        xAxis.add("MAR");
//        xAxis.add("APR");
//        xAxis.add("MAY");
//        xAxis.add("JUN");
//        return xAxis;
//    }


//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shame");
//        query.whereEqualTo("shameType", 1);
//        query.countInBackground(new CountCallback() {
//            @Override
//            public void done(int i, com.parse.ParseException e) {
//                Log.d("yuliya", i + "");
//            }
//
//        });
//
//
//        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Shame");
//        query1.whereEqualTo("shameType", 3);
//        query1.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> scoreList, com.parse.ParseException e) {
//                if (e == null) {
//                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });
//    }


    public int getCountShameTypes(final int type) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shame");
        query.whereEqualTo("shameType", type);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, com.parse.ParseException e) {
                numVerbalShame = i;
                numPhysicalShame = 0;
                numOtherShame = 5;
                Log.d("yuliya", i + " type= " + type);
            }
        });
        return result;
   }
}
