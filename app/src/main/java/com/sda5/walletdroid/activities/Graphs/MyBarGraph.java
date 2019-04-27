package com.sda5.walletdroid.activities.Graphs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sda5.walletdroid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MyBarGraph extends DemoBase implements
        OnChartGestureListener, OnChartValueSelectedListener {

    private BarChart chart;


    private HashMap<Integer, Integer> expenseMaps = new HashMap<>();

    public static final String TAG = "My Bar Graph";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_bar_graph);

        setTitle("My Line Chart Activity");

        final ArrayList<String> xLabel = new ArrayList<>();
        xLabel.add("Food");
        xLabel.add("Transport");
        xLabel.add("Books");
        xLabel.add("Car");
        xLabel.add("Sports");
        xLabel.add("Subscriptions");
        xLabel.add("Eat out");
        xLabel.add("Aug");
        xLabel.add("Sep");
        xLabel.add("Oct");
        xLabel.add("Nov");

        chart = findViewById(R.id.chartMyBar);
        chart.setOnChartValueSelectedListener(this);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(true);
        // Look-up table
        final String[] weekdays = {"Food", "Transport", "Books", "Car", "Sports", "Subscriptions", "Eatout"};

        // Set the value formatter
        xAxis.setValueFormatter(new IndexAxisValueFormatter(weekdays));
        //xAxis.mAxisMinimum=0;
        //xAxis.mAxisMaximum=20;

        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setTypeface(mTf);
        //leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        //rightAxis.setTypeface(mTf);
        //rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        // Adding values to hashmap
        expenseMaps.put(32, 1);
        expenseMaps.put(55, 2);
        expenseMaps.put(77, 6);
        expenseMaps.put(120, 8);
        expenseMaps.put(200, 1);
        expenseMaps.put(10, 2);
        expenseMaps.put(77, 6);
        expenseMaps.put(150, 8);


        //generateDataLine();
        getRemoteData(expenseMaps);
    }

    private final int[] colors = new int[]{
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2]
    };


    /**
     * @param expenseRemote
     */
    private void getRemoteData(HashMap<Integer, Integer> expenseRemote) {

        //Getting Set of keys
        final Set<Integer> keySet = expenseRemote.keySet();
        // Transfer data to an array
        final Integer[] test = keySet.toArray(new Integer[0]);
        // Entry data for creating graph
        final ArrayList<BarEntry> keySetData = new ArrayList<>();

        int lengthTest = test.length;

        Log.d(TAG, "LENGTH OF INTEGER ARRAY " + lengthTest);
        System.out.println("LENGTH OF INTEGER ARRAY " + lengthTest);

        for (int i = 0; i < test.length; i++) {
            keySetData.add(new BarEntry(i,test[i],months[i]));

        }



        BarDataSet d4 = new BarDataSet(keySetData, "Expenses ");
        d4.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d4.setHighLightAlpha(255);



        BarData data = new BarData(d4);
        data.setBarWidth(0.9f);
        data.setValueTextSize(14f);
        chart.setData((BarData)data);
        chart.invalidate();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        menu.removeItem(R.id.actionToggleIcons);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

          /*
            case R.id.actionToggleIcons: { break; }
             */
            case R.id.actionTogglePinch: {
                if (chart.isPinchZoomEnabled())
                    chart.setPinchZoom(false);
                else
                    chart.setPinchZoom(true);

                chart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                chart.setAutoScaleMinMaxEnabled(!chart.isAutoScaleMinMaxEnabled());
                chart.notifyDataSetChanged();
                break;
            }
            case R.id.actionToggleHighlight: {
                if (chart.getData() != null) {
                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled());
                    chart.invalidate();
                }
                break;
            }

            case R.id.actionSave: {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    saveToGallery();
                } else {
                    requestStoragePermission(chart);
                }
                break;
            }
            case R.id.animateX: {
                chart.animateX(2000);
                break;
            }
            case R.id.animateY: {
                chart.animateY(2000);
                break;
            }
            case R.id.animateXY: {
                chart.animateXY(2000, 2000);
                break;
            }
        }
        return true;
    }

    @Override
    protected void saveToGallery() {
        saveToGallery(chart, "MultiLineChartActivity");
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            chart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart long pressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart fling. VelocityX: " + velocityX + ", VelocityY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
    }

}
