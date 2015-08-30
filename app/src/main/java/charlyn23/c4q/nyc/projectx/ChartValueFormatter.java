package charlyn23.c4q.nyc.projectx;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

public class ChartValueFormatter  implements  ValueFormatter{
    private DecimalFormat mFormat;

    public ChartValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.00");
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value);
    }

}
