package com.wencai.model;

import java.io.Serializable;

import android.content.ContentValues;

@SuppressWarnings("serial")
public class QuestionClassEntry implements Serializable{
	private int _id;
	private String description="";
	public int get_id() {
		return _id;
	}
	public String getDescription() {
		return description;
	}
	
	public void set_id(int _id) {
		this._id = _id;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public QuestionClassEntry(String description) {
		super();
		this.description = description;
	}
	public QuestionClassEntry() {
		
	}
	
	public ContentValues getContentValuesByEntry(){
		ContentValues values=new ContentValues();
		//values.put("_id", _id);
		values.put("description", description);
		return values;
	}
}
