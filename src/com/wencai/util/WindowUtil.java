package com.wencai.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.Window;

@SuppressLint({ "NewApi", "SdCardPath" })
@SuppressWarnings("deprecation")
public class WindowUtil {

	private Activity mActivity;
	public WindowUtil(Activity mActivity) {
		super();
		this.mActivity = mActivity;	
	}
	
	public Point getDefaultDisplaySize(){
		Display display = mActivity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		size.y=display.getHeight();
		size.x=display.getWidth();
		return size;
	}
	
	public View getWindowDecorView(){
		return mActivity.getWindow().getDecorView();
	}
	
	public Rect getWindowDecorViewVisibleDisplayFrame(){
		Rect frames = new Rect();
		getWindowDecorView().getWindowVisibleDisplayFrame(frames);
		return frames;
	}
	
	public int getStatusBarHeight(){
		Rect frame = new Rect();  
		mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
		return frame.top; 
	}
	
	public int getTitleBarHeight(){
		int contentTop = mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		return contentTop - getStatusBarHeight(); 
	}
}
