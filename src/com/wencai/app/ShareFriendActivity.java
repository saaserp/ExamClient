package com.wencai.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.wencai.util.FileUtil;

public class ShareFriendActivity extends BaseActivity {
	private ImageView iv_share_shot;	
	private FileUtil fu;
	private TextView tv_title;
	Bitmap bm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_share_friend);

		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.share_friend));
		
		iv_share_shot = (ImageView) findViewById(R.id.iv_share_shot);

		/*
		 * 如果截屏产生的图片太大，则无法通过Intent传递，会产生FAILED BINDER TRANSACTION 
		 */
		fu=new FileUtil(this);
		String pic_path=fu.getPic_path();
		BitmapFactory.Options options = new BitmapFactory.Options();   
		options.inSampleSize = 2;
		bm = BitmapFactory.decodeFile(pic_path, options);
		iv_share_shot.setImageBitmap(bm);
		
	}
	
	@Override
	protected void onDestroy() {
		if(!bm.isRecycled()){
			bm.recycle();
		}	
		super.onDestroy();
	}
	
	
	
}
