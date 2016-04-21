package com.nengfei.util;

import java.io.File;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

@SuppressLint("SdCardPath")
public class FileUtil {
	private String pic_path;
	private Activity mActivity;
	private WindowUtil wu;
	private static final String FILE_PREFERENCES_NAME = "file_path";

	public String getPic_path() {
		return SharedPreferencesUtil.read(mActivity, FILE_PREFERENCES_NAME,
				"Pic_Path", "");
	}

	public void setPic_path() {
		pic_path = "/data/data/"
				+ PackageUtil.getAppInfo(this.mActivity).getAsString(
						"packageName") + "/image.png";
		SharedPreferencesUtil.write(mActivity, FILE_PREFERENCES_NAME,
				"Pic_Path", pic_path);
	}

	public FileUtil(Activity mActivity) {
		this.mActivity = mActivity;

		wu = new WindowUtil(this.mActivity);
	}

	/**
	 * 
	 * @param file_path
	 * @param x
	 *            The x coordinate of the first pixel in source
	 * @param y
	 *            The y coordinate of the first pixel in source
	 * @param width
	 *            The number of pixels in each row
	 * @param height
	 *            The number of rows
	 * @return
	 */
	public Bitmap shotAndSave(String file_path, int x, int y, int width,
			int height) {
		View decorView = wu.getWindowDecorView();
		decorView.buildDrawingCache();
		Bitmap bmp = Bitmap.createBitmap(decorView.getDrawingCache(), x, y,
				width, height);
		File file = new File(file_path);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bmp.compress(Bitmap.CompressFormat.PNG, 70, out)) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	public Bitmap shotAndSave(String file_path) {
		Rect frames = wu.getWindowDecorViewVisibleDisplayFrame();
		int statusBarHeight = frames.top;
		Point size = wu.getDefaultDisplaySize();
		return this.shotAndSave(file_path, 0, statusBarHeight, size.x, size.y
				- statusBarHeight);

	}
}
