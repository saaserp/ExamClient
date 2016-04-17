package com.wencai.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆饼图
 */
@SuppressLint({ "DrawAllocation", "DefaultLocale" })
public class PieChartView extends View {

	/** 背景色 backcolor */
	@SuppressWarnings("unused")
	private int backColor = Color.WHITE;
	private int piePaddingLeft = 0;
	private int piePaddingTop = 15;
	private int piePaddingRight = 0;
	private int piePaddingBottom = 15;
	private int specialSpace = 10;

	private int rightSpace = 100;

	/** 数据 the data */
	private float[] data = null;
	/** 每个数据对应的标题 the data title */
	private String[] title = null;
	/** 缺省数据颜色 */
	private int defColor = Color.GREEN;
	/** 数据的颜色 data color */
	private int[] color = null;
	/** 数据和 */
	private float sumData = 0;
	/** 数据个数 */
	private int dataCount = 0;
	/** 关注部分索引 */
	private int specialIndex = -1;
	/** 开始绘制角度 */
	private float startAngle = 30;

	private int barWidth = 15;

	private int textColor = 0xaa333333;
	
	public PieChartView(Context context) {
		super(context);
	}

	public PieChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PieChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 指定一个数据是否要关注 当一个数据关注时，会从其它数据中分离开来
	 * 
	 * @param index
	 *            数据对应的index，从0开始
	 */
	public void setSpecial(int index) {
		if (data != null && dataCount > index) {
			specialIndex = index;
		}
	}

	/**
	 * 设置数据个数
	 * 
	 * @param count
	 */
	public void setDataCount(int count) {
		if (count > 0) {
			data = new float[count];
			title = new String[count];
			dataCount = count;
			color = new int[count];
			for (int i = 0; i < count; i++) {
				color[i] = defColor;
			}
		}
	}

	/**
	 * 设置数据
	 * 
	 * @param d
	 */
	public void setData(float[] d) {
		if (d != null && d.length == dataCount) {
			for (int i = 0; i < dataCount; i++) {
				sumData += d[i];
			}
			data = d;
		}
	}

	/**
	 * 设置一个数据
	 * 
	 * @param index
	 *            数据index，从0开始
	 * @param d
	 */
	public void setData(int index, float d) {
		if (data != null && dataCount > index) {
			sumData -= data[index];
			data[index] = d;
			sumData += d;
		}
	}

	/**
	 * 设置数据标题
	 * 
	 * @param desc
	 */
	public void setDataTitle(String[] desc) {
		if (desc != null && dataCount == desc.length) {
			title = desc;
		}
	}

	/**
	 * 设置一个数据标题
	 * 
	 * @param index
	 *            数据index，从0开始
	 * @param desc
	 */
	public void setDataTitle(int index, String desc) {
		if (title != null && dataCount > index) {
			title[index] = desc;
		}
	}

	/**
	 * 设置数据颜色
	 * 
	 * @param c
	 */
	public void setColor(int[] c) {
		if (color != null && c.length == dataCount) {
			color = c;
		}
	}

	/**
	 * 设置一个数据颜色
	 * 
	 * @param index
	 *            数据index，从0开始
	 * @param c
	 */
	public void setColor(int index, int c) {
		if (color != null & dataCount > index) {
			color[index] = c;
		}
	}

	public void setBackgroundColor(int color) {
		backColor = color;
	}

	@SuppressLint("DefaultLocale")
	@Override
	protected void onDraw(Canvas canvas) {
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int paddingTop = getPaddingTop();
		int paddingBottom = getPaddingBottom();

		int height = getHeight() - paddingTop - paddingBottom;
		int width = getWidth() - paddingLeft - paddingRight;

		if (data != null) {
			canvas.save();
			canvas.translate(paddingLeft, paddingTop);
			canvas.clipRect(0, 0,width,height);

			//canvas.drawColor(backColor);

			int w = width - piePaddingLeft - piePaddingRight - rightSpace;
			int h = height - piePaddingTop - piePaddingBottom;

			int r = w;
			if (w > h)
				r = h;
			r=r*3/4;
			RectF rf = new RectF(piePaddingLeft, piePaddingTop, piePaddingLeft
					+ r, piePaddingTop + r);

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.FILL);

			float ang = startAngle;

			float[] percent = new float[dataCount];

			for (int i = 0; i < data.length; i++) {
				paint.setColor(color[i]);
				float tmp = data[i] / (sumData * 1.0f);
				percent[i] = tmp;
				tmp = tmp * 360;
				float toang = Math.round(tmp);

				if (specialIndex == i) {
					float ds = (ang + toang / 2);
					float dy = (float) Math.abs((specialSpace * Math
							.sin(ds * 0.01745)));
					float dx = (float) Math.abs((specialSpace * Math
							.cos(ds * 0.01745)));
					if (ds > 0 && ds <= 90) {

					} else if (ds > 90 && ds <= 180) {
						dx = dx * (-1);
					} else if (ds > 180 && ds <= 270) {
						dx = dx * (-1);
						dy = dy * (-1);
					} else if (ds > 270) {
						dy = dy * (-1);
					}
					RectF sf = new RectF(piePaddingLeft + dx, piePaddingTop
							+ dy, piePaddingLeft + dx + r, piePaddingTop + r
							+ dy);
					canvas.drawArc(sf, ang, toang, true, paint);
				} else
					canvas.drawArc(rf, ang, toang, true, paint);
				ang += toang;
			}

			FontMetrics fm = paint.getFontMetrics();
			float texty = piePaddingTop - fm.ascent;
			float textx = piePaddingLeft + r + 35;
			int maxTextWeitht=0;
			Rect textRect=new Rect();
			for (int i = 0; i < dataCount; i++) {
				paint.setColor(color[i]);
				canvas.drawRect(textx, texty, textx + barWidth, texty
						+ barWidth, paint);
				paint.setColor(textColor);
				String text=String.format("%.1f%%", percent[i] * 100);
				canvas.drawText(text,
						textx + barWidth + 10, texty - fm.ascent, paint);
				texty += fm.descent - fm.ascent + 15;
				paint.getTextBounds(text, 0, text.length(), textRect);
				if(textRect.width()>maxTextWeitht){
					maxTextWeitht=textRect.width();
				}
			}
			
			if (title != null) {
				float titlex=0;
				float titley=0;
				if(w<h){
					titlex= piePaddingLeft;
					titley = Math.max(texty, piePaddingTop+r)+ 35;
					for (int i = 0; i < title.length; i++) {
						paint.setColor(color[i]);
						canvas.drawRect(titlex, titley, titlex + barWidth * 2,
								titley + barWidth * 2, paint);
						paint.setColor(textColor);
						paint.setTextSize(20);
						fm = paint.getFontMetrics();
						canvas.drawText(title[i], titlex + barWidth * 2 + 10,
								titley - fm.ascent + 5, paint);
						titley += fm.descent - fm.ascent + 15;
					}
				}else{
					titlex=textx + barWidth +maxTextWeitht+10;
					titley=piePaddingTop-fm.ascent;;
					for (int i = 0; i < title.length; i++) {
						fm = paint.getFontMetrics();
						canvas.drawText(title[i], titlex + barWidth,
								titley - fm.ascent, paint);
						titley += fm.descent - fm.ascent + 15;
					}
				}
			
				
			}
			canvas.restore();
		}
	}

}
