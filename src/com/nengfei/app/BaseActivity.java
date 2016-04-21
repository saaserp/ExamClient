package com.nengfei.app;

import com.nengfei.util.FileUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
 
import android.view.View;
import android.view.Window;

/**
 * @author sxenon 所有Activity的共同特征，比方说没有状态栏 不能直接使用！！！
 */
public abstract class BaseActivity extends Activity {
	private FileUtil fu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置背景
		//this.getWindow().setBackgroundDrawableResource(R.drawable.bg_base);
		
		
	
	}

	// 截屏
	public void shotView(View view) {
		fu = new FileUtil(this);
		Bitmap bm = fu.shotAndSave(fu.getPic_path());
		Intent toShare = new Intent(this, ShareFriendActivity.class);
		startActivity(toShare);
		// 保存完毕，及时回收
		if (!bm.isRecycled()) {
			bm.recycle();
		}
	}

	// 返回
	public void toBack(View view) {
		finish();
	}

}
