package com.nengfei.model;

import java.io.Serializable;

import android.content.ContentValues;

@SuppressWarnings("serial")
public class ExamBackupsEntry implements Serializable{
	private int temp_id;//主键自增1
	private int question_id;
	private int rightAnswer;
	private int currentAnswer;
	public int getTemp_id() {
		return temp_id;
	}
	public void setTemp_id(int temp_id) {
		this.temp_id = temp_id;
	}
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public int getRightAnswer() {
		return rightAnswer;
	}
	public void setRightAnswer(int rightAnswer) {
		this.rightAnswer = rightAnswer;
	}
	public int getCurrentAnswer() {
		return currentAnswer;
	}
	public void setCurrentAnswer(int currentAnswer) {
		this.currentAnswer = currentAnswer;
	}
	public ExamBackupsEntry(int temp_id, int question_id, int rightAnswer,
			int currentAnswer) {
		super();
		this.temp_id = temp_id;
		this.question_id = question_id;
		this.rightAnswer = rightAnswer;
		this.currentAnswer = currentAnswer;
	}
	public ExamBackupsEntry() {
		
	}
	
	public ContentValues getContentValuesByEntry(){
		ContentValues values=new ContentValues();
		values.put("question_id", question_id);
		values.put("rightAnswer", rightAnswer);
		values.put("currentAnswer", currentAnswer);
		return values;
	}
}
