package com.onemeter.wiget;

import android.view.View;

import com.onemeter.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WheelYuYue {

	private View view;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	public int screenheight;
	private boolean hasSelectTime;
	private static int START_YEAR = 1950, END_YEAR = 2100;
	private int nowyear = 2015;
	private int nowmonth = 1;
	private int nowday = 1;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public WheelYuYue(View view) {
		super();
		this.view = view;
		hasSelectTime = false;
		Calendar calendar = Calendar.getInstance();
		nowyear = calendar.get(Calendar.YEAR);
		nowmonth = calendar.get(Calendar.MONTH);
		nowday = calendar.get(Calendar.DAY_OF_MONTH);
		setView(view);
	}

	public WheelYuYue(View view, boolean hasSelectTime) {
		super();
		this.view = view;
		this.hasSelectTime = hasSelectTime;
		setView(view);
	}

	public void initDateTimePicker(int year, int month, int day) {
		this.initDateTimePicker(year, month, day, 0, 0);
	}

	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void initDateTimePicker(int year, int month, int day, int h, int m) {
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH);
		// int day = calendar.get(Calendar.DATE);
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "01", "03", "05", "07", "08", "10", "12" };
		String[] months_little = { "04", "06", "09", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		// 年
		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		wv_month = (WheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);

		// 日
		wv_day = (WheelView) view.findViewById(R.id.day);
		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setLabel("日");
		wv_day.setCurrentItem(day - 1);

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
				// 选择“年”小于当前“年” 则退回至当前年
				if (year_num < nowyear) {
					wv_year.setCurrentItem(nowyear - START_YEAR);
				}
				// 选择“月”小于当前“月” 则退回至当前月
				if (wv_month.getCurrentItem() < nowmonth) {
					wv_month.setCurrentItem(nowmonth);
				}
				// 选择“日”小于当前“日” 则退回至当前日
				if (wv_day.getCurrentItem() < nowday) {
					wv_day.setCurrentItem(nowday - 1);
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				int selectday = 30;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					selectday = 31;
					wv_day.setAdapter(new NumericWheelAdapter(1, selectday));
				} else if (list_little.contains(String.valueOf(month_num))) {
					selectday = 30;
					wv_day.setAdapter(new NumericWheelAdapter(1, selectday));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0) {
						selectday = 29;
						wv_day.setAdapter(new NumericWheelAdapter(1, selectday));
					} else {
						selectday = 28;
						wv_day.setAdapter(new NumericWheelAdapter(1, selectday));
					}
				}
				wv_day.setCurrentItem(selectday - 1);
				// 当前年
				if ((wv_year.getCurrentItem() + START_YEAR) == nowyear) {
					// 选择“月”小于当前“月” 则退回至当前月
					if (month_num < nowmonth + 1) {
						wv_month.setCurrentItem(nowmonth);
					}
				}
				if (wv_day.getCurrentItem() < nowday) {
					wv_day.setCurrentItem(nowday - 1);
				}
			}
		};

		// 添加"日"监听
		OnWheelChangedListener wheelListener_day = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int day_num = newValue + 1;
				// 当前年
				if ((wv_year.getCurrentItem() + START_YEAR) == nowyear) {
					// 选择“月”大于当前“月” 则退回至当前月
					if (wv_month.getCurrentItem() == nowmonth) {
						// 选择“日”大于当前“日” 则退回至当前日
						if (day_num < nowday) {
							wv_day.setCurrentItem(nowday - 1);
						}
					}
				}
			}
		};

		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
		wv_day.addChangingListener(wheelListener_day);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		if (hasSelectTime)
			textSize = (screenheight / 100) * 2;
		else
			textSize = (screenheight / 100) * 3;
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;

	}

	public String getTime() {
		StringBuffer sb = new StringBuffer();
		if (!hasSelectTime)
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
					.append((wv_month.getCurrentItem() + 1)).append("-")
					.append((wv_day.getCurrentItem() + 1));
		else
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
					.append((wv_month.getCurrentItem() + 1)).append("-")
					.append((wv_day.getCurrentItem() + 1)).append(" ");
		return sb.toString();
	}
}
