package com.nengfei.widget;

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
		//计算左边距
		int paddingLeft = getPaddingLeft();
		//计算右边距
		int paddingRight = getPaddingRight();
		//计算上边距
		int paddingTop = getPaddingTop();
		//计算下边距
		int paddingBottom = getPaddingBottom();
		//计算高度
		int height = getHeight() - paddingTop - paddingBottom;
		//计算宽度
		int width = getWidth() - paddingLeft - paddingRight;
		//如果数据不为空
		if (data != null) {
			//保存原有画布
			canvas.save();
			//设置画布的边距
			canvas.translate(paddingLeft, paddingTop);
			//设置画布的宽度高度
			canvas.clipRect(0, 0,width,height);

			//canvas.drawColor(backColor);
			//计算扇形的宽度
			int w = width - piePaddingLeft - piePaddingRight - rightSpace;
			//计算扇形的高度
			int h = height - piePaddingTop - piePaddingBottom;
			//圆的半径
			int r = w;
			if (w > h)
				r = h;
			//半径为原来的3/4
			r=r*3/4;
			//矩形,设置边距
			RectF rf = new RectF(piePaddingLeft, piePaddingTop, piePaddingLeft
					+ r, piePaddingTop + r);
			//画笔
			Paint paint = new Paint();
			//设置画笔为平滑
			paint.setAntiAlias(true);
			//设置画笔的风格为全屏
			paint.setStyle(Paint.Style.FILL);
			//设置角度
			float ang = startAngle;
			//百分比
			float[] percent = new float[dataCount];
			//遍历所有的数据
			for (int i = 0; i < data.length; i++) {
				//设置画笔颜色
				paint.setColor(color[i]);
				//设置偏移角度比例
				float tmp = data[i] / (sumData * 1.0f);
				//存储百分比
				percent[i] = tmp;
				//换算成角度
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
					//绘制图像
					canvas.drawArc(sf, ang, toang, true, paint);
				} else
					//绘制图像
					canvas.drawArc(rf, ang, toang, true, paint);
					ang += toang;
			}
			//字体
			FontMetrics fm = paint.getFontMetrics();
			//x维度的字体大小
			float texty = piePaddingTop - fm.ascent;
			float textx = piePaddingLeft + r + 35;
			//最大字体的重量
			int maxTextWeitht=0;
			//实例化一个矩形
			Rect textRect=new Rect();
			//对每个数据
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
			//标题
			if (title != null) {
				float titlex=0;
				float titley=0;
				//如果宽度小于高度
				if(w<h){
					//标题设置左边距
					titlex= piePaddingLeft;
					//标题的上边距
					titley = Math.max(texty, piePaddingTop+r)+ 35;
					//对于标题上的每一个字
					for (int i = 0; i < title.length; i++) {
						//设置画笔的颜色
						paint.setColor(color[i]);
						//绘制矩形
						canvas.drawRect(titlex, titley, titlex + barWidth * 2,
								titley + barWidth * 2, paint);
						//设置颜色
						paint.setColor(textColor);
						//设置字体大小为20
						paint.setTextSize(20);
						//获取字体的属性
						fm = paint.getFontMetrics();
						canvas.drawText(title[i], titlex + barWidth * 2 + 10,
								titley - fm.ascent + 5, paint);
						titley += fm.descent - fm.ascent + 15;
					}
				}else{
					//字体属性设置
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
			//如果绘制图像失败，将恢复到原来的数据
			canvas.restore();
		}
	}

}
