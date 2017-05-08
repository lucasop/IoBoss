package net.eclissi.lucasop.ioboss;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.WindowManager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import net.eclissi.lucasop.ioboss.database.DatabaseHelperSync;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lucasoprana on 06/05/2017.
 */

public class PieChartActivity extends AppCompatActivity  {


    private PieChart mChart;

    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    protected Typeface mTfRegular;
    protected Typeface mTfLight;


    private DatabaseHelperSync dbsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_piechart);

        dbsync = new DatabaseHelperSync(this);

        String outDB = Environment.getExternalStorageDirectory() + "/DBfile.db3";
        File currentDB = this.getDatabasePath(DatabaseHelperSync.DB_NAME);
        Log.d("Blue", "export DB : " + outDB  );
        Log.d("Blue", "current DB : " + currentDB.toString()  );

        try {
            dbsync.exportDatabase(outDB);
        } catch (IOException e) {
            e.printStackTrace();
        }


        mChart = (PieChart) findViewById(R.id.chart);
        mChart.setBackgroundColor(Color.WHITE);

        //moveOffScreen();

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);

        mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);

        mChart.setMaxAngle(180f); // HALF CHART
        mChart.setRotationAngle(1800f);
        mChart.setCenterTextOffset(0, -20);

        setData(4, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling

        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);

    }

    private void setData(int count, float range) {

        ArrayList<PieEntry> values = new ArrayList<PieEntry>();

        //for (int i = 0; i < count; i++) {
        //    values.add(new PieEntry((float) ((Math.random() * range) + range / 5), mParties[i % mParties.length]));
        //}



        // Cursor cursor = dbsync.getDiffRowToDay();

        Cursor cursor = dbsync.getSumActivityToDay();



        int vStill = 0;
        int vWalking = 0;
        int vVehicle = 0;


        if (cursor.moveToFirst()) {
            do {
                //calling unsynced activity to sync remote Server

                //long vID = cursor.getLong(cursor.getColumnIndex(DatabaseHelperSync.COLUMN_ID));

                //String vOption = cursor.getString(cursor.getColumnIndex(DatabaseHelperSync.COLUMN_OPTION));
                //long vDate = cursor.getLong(cursor.getColumnIndex(DatabaseHelperSync.COLUMN_DATATIME));
                //long vDate2 = cursor.getLong(cursor.getColumnIndex(DatabaseHelperSync.COLUMN_DATATIME2));
                //long vDiff = vDate - vDate2 ;

                String vOption = cursor.getString(cursor.getColumnIndex(DatabaseHelperSync.COLUMN_OPTION));
                long vDiff = cursor.getLong(cursor.getColumnIndex(DatabaseHelperSync.COLUMN_SUMDIFF));

                // TODO: sistemare ciclo group by
                /*
                if  (vOption.equals("still")) {
                    //vStill = vStill +
                }
                */
                values.add(new PieEntry((float) (vDiff), vOption));

                    long vID = 0;
                    String vDateDiff = "220";
                //Log.d("Blue", "Chart - ID: " + vID  + " Option: " + vOption + " date : " + vDate + " date2: "+ vDate2 + " datediff: " + vDiff  );
                Log.d("Blue", "Chart - ID: " + vID  + " Option: " + vOption  + " datediff: " + vDiff  );

            } while (cursor.moveToNext());
        }


        PieDataSet dataSet = new PieDataSet(values, "LucaSOp Activity");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        //dataSet.setSelectionShift(0f);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }




}
