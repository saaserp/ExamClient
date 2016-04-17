package com.wencai.controller;

import com.wencai.model.ExamResultService;
import com.wencai.model.QuestionBankService;

import android.content.Context;


public class StatisticsController {
	private QuestionBankService questionBankService=new QuestionBankService();
	private ExamResultService examResultService=new ExamResultService();
	public int getUndoQuestionNum(Context context){
		return questionBankService.getUndoQuestionNum(context);
	}
	
	public int getRightAlwaysQuestionNum(Context context){
		return questionBankService.getRightAlwaysQuestionNum(context);
	}
	
	public int getWrongAlwaysQuestionNum(Context context){
		return questionBankService.getWrongAlwaysQuestionNum(context);
	}
	
	public int getWrongOftenQuestionNum(Context context){
		return questionBankService.getWrongOftenQuestionNum(context);
	}
	
	public int getRightOftenQuestionNum(Context context){
		return questionBankService.getRightOftenQuestionNum(context);
	}
	
	public int getBestScoreTimes(Context context){
		return examResultService.getBestScoreTimes(context);
	}
	
	public int getBetterScoreTimes(Context context){
		return examResultService.getBetterScoreTimes(context);
	}
	
	public int getJustSoSoScoreTimes(Context context){
		return examResultService.getJustSoSoScoreTimes(context);
	}
	
	public int getBadScoreTimes(Context context){
		return examResultService.getBadScoreTimes(context);
	}
	
	public int getWorseScoreTimes(Context context){
		return examResultService.getWorseScoreTimes(context);
	}
	
	public int getWorstScoreTimes(Context context){
		return examResultService.getWorstScoreTimes(context);
	}
	
	public int getTestTimes(Context context){
		return examResultService.getTestTimes(context);
	}
}
