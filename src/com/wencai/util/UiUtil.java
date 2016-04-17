package com.wencai.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

public class UiUtil {

	public static void showAlertOnly(Context context, int messageId,
			int textId, int titleId) {
		new AlertDialog.Builder(context).setMessage(messageId)
				.setTitle(titleId).setPositiveButton(textId, null).show();
	}

	public static void showQueryAlert(Context context, int messageId,
			int textPositiveId, int textNegativeId, int titleId,
			OnClickListener onPositiveClickListener,
			OnClickListener onNegativeClickListener) {
		new AlertDialog.Builder(context).setMessage(messageId)
				.setTitle(titleId)
				.setPositiveButton(textPositiveId, onPositiveClickListener)
				.setNegativeButton(textNegativeId, onNegativeClickListener)
				.show();
	}

	public static void showToastLong(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}

	public static void showToastShort(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static void showToastLong(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
	}

	public static void showToastShort(Context context, int resId) {
		// TODO Auto-generated method stub
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
}
