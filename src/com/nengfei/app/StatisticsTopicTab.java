package com.nengfei.app;

import com.nengfei.controller.StatisticsController;
import com.nengfei.widget.PieChartView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

public class StatisticsTopicTab extends Activity {
	private float data[];
	private StatisticsController statisticsController = new StatisticsController();
	private PieChartView pcv_statistics;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tap_statistics);
		
		pcv_statistics=(PieChartView) findViewById(R.id.pcv_statistics);
		pcv_statistics.setDataCount(5);
		pcv_statistics.setColor(new int[] { Color.YELLOW, Color.BLUE, Color.GRAY,
				Color.MAGENTA, Color.RED });
		data = new float[5];
		data[0] = statisticsController.getUndoQuestionNum(this);
		data[1] = statisticsController.getRightAlwaysQuestionNum(this);
		data[2] = statisticsController.getRightOftenQuestionNum(this);
		data[3] = statisticsController.getWrongAlwaysQuestionNum(this);
		data[4] = statisticsController.getWrongOftenQuestionNum(this);
		pcv_statistics.setData(data);
		pcv_statistics.setDataTitle(getResources().getStringArray(
				R.array.statistics_status));
	
		pcv_statistics.setPadding(15, 0, 15, 0);
	}
}
