package ba.genijalno.example.mpchart;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Handles the Line Chart GUI
 */
public class LineChartFragment extends Fragment{

    private static final String TAG = LineChartFragment.class.getSimpleName();

    // public Gson instance
    private final Gson GSON = new GsonBuilder().create();

    private static String SAMPLE_JSON = null;

    public static LineChartFragment newInstance(){
        LineChartFragment fragment = new LineChartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LineChartFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        SAMPLE_JSON = getString(R.string.sample_data_json);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // initialize mpchart
        LineChart mChart = (LineChart) view.findViewById(R.id.line_chart);

        mChart.setDescription("");
        mChart.setNoDataTextDescription("");
        mChart.setNoDataText("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
//        mChart.setMaxVisibleValueCount(60);

        mChart.setStartAtZero(false);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawVerticalGrid(false);
        mChart.setDrawGridBackground(false);

        float chartLabelSize = getLabelSize();
//        float chartLabelSize = R.integer.chart_label_text_size;

        XLabels xLabels = mChart.getXLabels();
        xLabels.setPosition(XLabels.XLabelPosition.BOTTOM);
//        xLabels.setCenterXLabelText(true);
//        xLabels.setSpaceBetweenLabels(4);
//        xLabels.setTextSize(chartLabelSize);
        xLabels.setAvoidFirstLastClipping(true);

        YLabels yLabels = mChart.getYLabels();
        yLabels.setLabelCount(8);
        yLabels.setDrawTopYLabelEntry(true);
        yLabels.setPosition(YLabels.YLabelPosition.LEFT);
        yLabels.setTextSize(chartLabelSize);
        yLabels.setFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("$%d", (long)value);
            }
        });

        mChart.setDrawYLabels(true);
        mChart.setDrawLegend(false);

        mChart.setNoDataTextDescription("");
        mChart.setNoDataText("");

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Entry> yValues = new ArrayList<>();


        Type typeOfT = new TypeToken<ArrayList<ArrayList<String>>>() {}.getType();
        List<List<String>> dataList = GSON.fromJson(SAMPLE_JSON, typeOfT);

        Date first = null;
        Date last = null;

        int i = 0;
        for(List<String> entry : dataList) {

            Log.d(TAG, "entry: " + entry.toString());

            Date date = new Date(Long.parseLong(entry.get(0))*1000L);

            if(i == 0){
                first = date;
            }

            if(i == dataList.size() - 1){
                last = date;
            }

            xValues.add(getChartDateFormat().format(date)); // converting from unix time
            yValues.add(new Entry(Float.parseFloat(entry.get(1)), i));

            i++;
        }

        final LineDataSet set1 = new LineDataSet(yValues, "Price");

        set1.setDrawCircles(false);
        set1.setDrawFilled(true);
        set1.setFillColor(Color.GRAY);
        set1.setFillAlpha(10);

        LineData data = new LineData(xValues, set1);
        mChart.setData(data);

        // set Y range and invalidate (redraw) chart
        mChart.setYRange(set1.getYMin() - 10, set1.getYMax() + 10, true);
//                            mChart.invalidate();

        TextView lastUpdated = (TextView) view.findViewById(R.id.last_updated);
        lastUpdated.setText(String.format("%s - %s", getDateFormat().format(first), getDateFormat().format(last)));

        return view;
    }

    private float getLabelSize() {
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        float labelSizePx;

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                labelSizePx = 17;
                break;
            default:
                labelSizePx = 9;
        }
        return labelSizePx;
    }

    // gui date/time display format
    public static DateFormat getDateFormat(){
        return  DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    }
    public static SimpleDateFormat getChartDateFormat(){
        return new SimpleDateFormat("MMM dd", Locale.getDefault());
    }

}
