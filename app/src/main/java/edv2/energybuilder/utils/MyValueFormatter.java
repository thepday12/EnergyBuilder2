package edv2.energybuilder.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class MyValueFormatter implements IValueFormatter {

    int decimals = 0;

    public MyValueFormatter(int decimals) {
        this.decimals = decimals;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return String.format("%."+decimals+"f", value);
    }
}