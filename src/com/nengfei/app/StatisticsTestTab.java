package com.nengfei.app;

import com.nengfei.controller.StatisticsController;
import com.nengfei.widget.PieChartView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatisticsTestTab extends Activity {
	private float[] data;
	private StatisticsController statisticsController = new StatisticsController();
	private PieChartView pcv_statistics;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tap_statistics);

		pcv_statistics = (PieChartView) findViewById(R.id.pcv_statistics);
		if(statisticsController.getTestTimes(this)>0){
			pcv_statistics.setDataCount(6);
			pcv_statistics.setColor(new int[] { Color.YELLOW, Color.BLUE,
					Color.GRAY, Color.MAGENTA, Color.RED, Color.CYAN });
			data = new float[6];
			data[0] = statisticsController.getBestScoreTimes(this);
			data[1] = statisticsController.getBetterScoreTimes(this);
			data[2] = statisticsController.getJustSoSoScoreTimes(this);
			data[3] = statisticsController.getBadScoreTimes(this);
			data[4] = statisticsController.getWorseScoreTimes(this);
			data[5] = statisticsController.getWorstScoreTimes(this);
			pcv_statistics.setData(data);
			pcv_statistics.setDataTitle(getResources().getStringArray(
					R.array.statistics_test_rate));
			
			pcv_statistics.setPadding(15, 0, 15, 0);
		}else{
			TextView tvZeroTest=new TextView(this);
			tvZeroTest.setText(R.string.statistics_zero_test);
			tvZeroTest.setTextSize(20);
			LinearLayout ll_statistics=(LinearLayout) findViewById(R.id.ll_statistics);
			ll_statistics.removeAllViews();
			ll_statistics.addView(tvZeroTest);
		}
		
	}
}
