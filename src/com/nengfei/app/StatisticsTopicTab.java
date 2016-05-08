package com.nengfei.app;

import com.nengfei.controller.StatisticsController;
import com.nengfei.widget.PieChartView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StatisticsTopicTab extends Fragment {
	private float data[];
	private StatisticsController statisticsController = new StatisticsController();
	private PieChartView pcv_statistics;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 设置activity的布局
		View view = inflater.inflate(R.layout.tap_statistics, null);
		pcv_statistics=(PieChartView) view.findViewById(R.id.pcv_statistics);
		pcv_statistics.setDataCount(5);
		pcv_statistics.setColor(new int[] { Color.YELLOW, Color.BLUE, Color.GRAY,
				Color.MAGENTA, Color.RED });
		data = new float[5];
		data[0] = statisticsController.getUndoQuestionNum(getActivity());
		data[1] = statisticsController.getRightAlwaysQuestionNum(getActivity());
		data[2] = statisticsController.getRightOftenQuestionNum(getActivity());
		data[3] = statisticsController.getWrongAlwaysQuestionNum(getActivity());
		data[4] = statisticsController.getWrongOftenQuestionNum(getActivity());
		pcv_statistics.setData(data);
		pcv_statistics.setDataTitle(getResources().getStringArray(
				R.array.statistics_status));
	
		pcv_statistics.setPadding(15, 0, 15, 0);
		return view;
	}
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.tap_statistics);
//		
//		pcv_statistics=(PieChartView) findViewById(R.id.pcv_statistics);
//		pcv_statistics.setDataCount(5);
//		pcv_statistics.setColor(new int[] { Color.YELLOW, Color.BLUE, Color.GRAY,
//				Color.MAGENTA, Color.RED });
//		data = new float[5];
//		data[0] = statisticsController.getUndoQuestionNum(this);
//		data[1] = statisticsController.getRightAlwaysQuestionNum(this);
//		data[2] = statisticsController.getRightOftenQuestionNum(this);
//		data[3] = statisticsController.getWrongAlwaysQuestionNum(this);
//		data[4] = statisticsController.getWrongOftenQuestionNum(this);
//		pcv_statistics.setData(data);
//		pcv_statistics.setDataTitle(getResources().getStringArray(
//				R.array.statistics_status));
//	
//		pcv_statistics.setPadding(15, 0, 15, 0);
//	}
}
