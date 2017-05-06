package net.eclissi.lucasop.ioboss;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by lucasoprana on 06/05/2017.
 */

public class PieChartActivity extends AppCompatActivity  {


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_piechart);

            PieChart pieChart = (PieChart) findViewById(R.id.chart);

            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(4f, 0));
            entries.add(new Entry(8f, 1));
            entries.add(new Entry(6f, 2));
            entries.add(new Entry(12f, 3));
            entries.add(new Entry(18f, 4));
            entries.add(new Entry(9f, 5));

            PieDataSet dataset = new PieDataSet(entries, "# of Calls");

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("January");
            labels.add("February");
            labels.add("March");
            labels.add("April");
            labels.add("May");
            labels.add("June");

            PieData data = new PieData(labels, dataset);
            dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
            pieChart.setDescription("Description");
            pieChart.setData(data);

            pieChart.animateY(5000);

            pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image



        }


}
