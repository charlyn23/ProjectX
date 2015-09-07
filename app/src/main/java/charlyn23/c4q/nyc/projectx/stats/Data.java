package charlyn23.c4q.nyc.projectx.stats;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;

public class Data {
    private ArrayList<Entry> yValues;
    private ArrayList<BarEntry> yVals;
    private ArrayList<String> xValues;
    private String name;

    public Data(ArrayList<BarEntry> yVals, ArrayList<String> xValues) {
        this.yVals = yVals;
        this.xValues = xValues;
    }

    public Data(String name, ArrayList<Entry> yValues, ArrayList<String> xValues) {
        this.name = name;
        this.yValues = yValues;
        this.xValues = xValues;
    }

    public ArrayList<Entry> getyValues() {
        return yValues;
    }

    public void setyValues(ArrayList<Entry> yValues) {
        this.yValues = yValues;
    }

    public ArrayList<BarEntry> getyVals() {
        return yVals;
    }

    public void setyVals(ArrayList<BarEntry> yVals) {
        this.yVals = yVals;
    }

    public ArrayList<String> getxValues() {
        return xValues;
    }

    public void setxValues(ArrayList<String> xValues) {
        this.xValues = xValues;
    }
}
