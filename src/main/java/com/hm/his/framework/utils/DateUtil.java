package com.hm.his.framework.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

	public static Map<Integer, String> WEEK_DAY_MAP;

	static Logger logger = Logger.getLogger(DateUtil.class);

	static {
		WEEK_DAY_MAP = new LinkedHashMap<Integer, String>();
		WEEK_DAY_MAP.put(0, "星期一");
		WEEK_DAY_MAP.put(1, "星期二");
		WEEK_DAY_MAP.put(2, "星期三");
		WEEK_DAY_MAP.put(3, "星期四");
		WEEK_DAY_MAP.put(4, "星期五");
		WEEK_DAY_MAP.put(5, "星期六");
		WEEK_DAY_MAP.put(6, "星期日");
	}

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String SHORT_FORMAT = "yy-MM-dd HH:mm";
	public static final String SHORT_FORMAT2 = "yyyy-MM-dd HH:mm";
	public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String ZHONGWEN_YMD_FORMAT = "yyyy年MM月dd日 HH:mm";
	public static final String ZHONGWEN_HM_FORMAT = "HH点mm分";
	public static final String ZHONGWEN_ALL_FORMAT = "yyyy年MM月dd日HH点mm分";
	public static final String DOT_DATE_FORMAT = "yyyy.MM.dd";
	public static final String DOT_DATE_HOUR_MINUTE_FORMAT = "yyyy.MM.dd HH:mm";
	public static final String MONTH_DAY_FORMAT = "M.dd";
	public static final String HOUR_MINUTE_FORMAT = "HH:mm";

	//为了防止SimpleDateFormat带来的线程不安全问题，统一改成每个方法单独new一个SimpleDateFormat对象
	//然后，只使用公共的DateFormat的格式
	//	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	//	private static final SimpleDateFormat SHORT_FORMAT = new SimpleDateFormat("yy-MM-dd HH:mm");
	//	private static final SimpleDateFormat SHORT_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	//	private static final SimpleDateFormat STANDARD_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	//	public static final SimpleDateFormat ZHONGWEN_YMD_FORMAT = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	//	public static final SimpleDateFormat ZHONGWEN_ALL_FORMAT = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");

	public static Date shortParse(String date) {
		try {
			return new SimpleDateFormat(SHORT_FORMAT).parse(date);
		} catch (ParseException e) {
			logger.error(e);
		}
		return null;
	}

	public static String shortFormat(Date date) {
		return new SimpleDateFormat(SHORT_FORMAT).format(date);
	}

	public static String dotDateFormat(Date date) {
		return new SimpleDateFormat(DOT_DATE_FORMAT).format(date);
	}

	public static String dotDateHourMinuteFormat(Date date) {
		return new SimpleDateFormat(DOT_DATE_HOUR_MINUTE_FORMAT).format(date);
	}

	public static String shortFormat2(Date date) {
		return new SimpleDateFormat(SHORT_FORMAT2).format(date);
	}

	public static String standardFormat(Date date) {
		return new SimpleDateFormat(STANDARD_FORMAT).format(date);
	}

	public static String monthDayFormat(Date date) {
		return new SimpleDateFormat(MONTH_DAY_FORMAT).format(date);
	}

	public static String hourMinuteFormat(Date date) {
		return new SimpleDateFormat(HOUR_MINUTE_FORMAT).format(date);
	}

	public static String dateFormat(Date date) {
		return new SimpleDateFormat(DATE_FORMAT).format(date);
	}

	public static Date parseDateOrNull(String s) {
		Date date = null;
		try {
			date = new SimpleDateFormat(STANDARD_FORMAT).parse(s);
		} catch (Exception e) {

		}
		if (date == null) {
			try {
				date = new SimpleDateFormat(DATE_FORMAT).parse(s);
			} catch (Exception e) {

			}
		}
		return date;
	}

	public static String timeFormat(Date date) {
		return new SimpleDateFormat(TIME_FORMAT).format(date);
	}

	public static Date format(String time, String format) {
		try {
			SimpleDateFormat formater = new SimpleDateFormat(format);
			return formater.parse(time);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	public static String format(Date time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time);
	}

	public static Date getStartTimeOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getEndTimeOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}


	/**
	 * 当前时间增加指定的天数
	 * @param day 待增加的天数,此值可为负数
	 * @return
	 */
	public static Date addDay(int day) {
		return addDay(new Date(), day);
	}

	/**
	 * 当前时间减去指定的天数
	 * @param day 待增加的天数,此值可为负数
	 * @return
	 */
	public static Date subtractDay(int day) {
		return subtractDay(new Date(), day);
	}

	/**
	 * 时间减去指定的天数
	 * @param day 待增加的天数,此值可为负数
	 * @return
	 */
	public static Date subtractDay(Date date,int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - day);
		return calendar.getTime();
	}

	/**
	 * 时间增加指定的天数
	 * @param day 待增加的天数,此值可为负数
	 * @return
	 */
	public static Date addDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);

		return calendar.getTime();
	}

	/**
	 * 时间增加指定的小时数
	 * @param date 待增加的小时数,此值可为负数
	 * @return
	 */
	public static Date addHour(Date date , int hour){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hour);
		return calendar.getTime();
	}

	/**
	 * 获取时间中的日期(月中的日)
	 * @param date 时间
	 * @return 日期
	 */
	public static int getDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 获取时间中的月份
	 * @param date 时间
	 * @return 月份
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 计算星座
	 * @param date 日期
	 * @return ["魔羯座"、"水瓶座"、"双鱼座"、"白羊座"、"金牛座"、"双子座"、"巨蟹座"、"狮子座"、"处女座"、"天秤座"、"天蝎座"、"射手座"]
	 */
	public static String getConstellation(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getConstellation(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 计算星座
	 * @param month 月
	 * @param day 日
	 * @return ["魔羯座"、"水瓶座"、"双鱼座"、"白羊座"、"金牛座"、"双子座"、"巨蟹座"、"狮子座"、"处女座"、"天秤座"、"天蝎座"、"射手座"]
	 */
	public static String getConstellation(int month, int day) {
		/*
		 * 生日与星座关系：
		 * 摩羯座（12月22日-1月19日）、水瓶座（1月20日-2月18日）、双鱼座（2月19日-3月20日）、白羊座（3月21日-4月19日）、
		 * 金牛座（4月20日-5月20日）、双子座（5月21日-6月21日）、巨蟹座（6月22日-7月22日）、狮子座（7月23日-8月22日）、
		 * 处女座（8月23日-9月22日）、天秤座（9月23日-10月23日）、天蝎座（10月24日-11月22日）、
		 * 射手座（11月23日-12月21日）
		 */
		String s = "魔羯水瓶双鱼白羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
		Integer[] arr = {20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};
		Integer num = month * 2 - (day < arr[month - 1] ? 2 : 0);
		return s.substring(num, num + 2) + "座";
	}

	/**
	 * 制造时间
	 * @param year 年
	 * @param month 月
	 * @param date 日
	 * @param hour 时
	 * @param minute 分
	 * @param second 秒
	 * @param milliSecond 毫秒
	 * @return
	 */
	public static Date makeDate(int year, int month, int date, int hour, int minute, int second, int milliSecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, date, hour, minute, second);
		calendar.set(Calendar.MILLISECOND, milliSecond);
		return calendar.getTime();
	}

	/**
	 * 计算两个时间的时间差，结束时间-开始时间。如果开始时间为空，则时间差为结束时间的getTime()值；如果结束时间为空，则时间差为开始时间的-getTime()值；若两者皆为空，则时间差为0。
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public static long getInterval(Date startDate, Date endDate) {
		if (null == startDate) {
			if (null == endDate) {
				return 0L;
			} else {
				return endDate.getTime();
			}
		}

		if (null == endDate) {
			return -startDate.getTime();
		}

		return endDate.getTime() - startDate.getTime();
	}

	/**
	 * 判断时间是否在指定的时间范围内
	 * @param time
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean isBetweenRange(Date time, Date startTime, Date endTime) {
		if ((null != time) && (null != startTime) && (null != endTime)) {
			if ((time.after(startTime)) && (time.before(endTime))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 计算两个时间之间的有效日期，例如"2013-11-10 08:34:00"~"2013-11-12 09:45:00"的计算结果为["2013-11-10 00:00:00","2013-11-11 00:00:00","2013-11-12 00:00:00"]
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public static List<Date> validateDateList(Date startTime, Date endTime) {
		List<Date> list = new ArrayList<Date>();
		Date date = DateUtil.getStartTimeOfDate(startTime);
		list.add(date);
		while (true) {
			date = DateUtil.addDay(date, 1);
			if (date.after(endTime)) {
				break;
			}
			list.add(date);
		}

		return list;
	}
	/**
	 * 默认日期格式
	 */
	public static String DEFAULT_FORMAT = "yyyy-MM-dd";

	private static SimpleDateFormat formatter;

	/**
	 * 格式化日期yyyy-MM-dd
	 * @param date 日期对象
	 * @return String 日期字符串
	 */
	public static String formatDate(Date date){
		SimpleDateFormat f = new SimpleDateFormat(DEFAULT_FORMAT);
		String sDate = f.format(date);
		return sDate;
	}
	
	/**
	 * 根据字符串得到通用时间对象
	 * 格式yyyy-MM-dd HH:mm:ss
	 * @param stringDate String 时间格式的字符串
	 * @return Date
	 */
	public static Date parserGeneralDate (String stringDate) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return simpleDateFormat.parse(stringDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将日期格式化成yyyy-MM-dd HH:mm:ss的字符串
	 * @param date Date 时间对象
	 * @return String
	 */
	public static String getGeneralDate(Date date) {
		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(date == null)
			return formatter.format(new Date());
		else
			return formatter.format(date);
	}
	
	/**
	 * 将日期格式化成yyyy-MM-dd的字符串
	 * @param stringDate Date 时间对象
	 * @return String
	 */
	public static String getGeneralDate(String stringDate) {
		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtils.isEmpty(stringDate))
			return "";
		Date date = parserGeneralDate(stringDate);
		if(date == null)
			return "";
		else
			return formatter.format(date);
	}

	/**
	 * 获取当年的第一天
	 * @return
	 */
	public static Date getCurrYearFirst(){
		Calendar currCal=Calendar.getInstance();  
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearFirst(currentYear);
	}
	
	/**
	 * 获取当年的最后一天
	 * @return
	 */
	public static Date getCurrYearLast(){
		Calendar currCal=Calendar.getInstance();  
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearLast(currentYear);
	}
	
	/**
	 * 获取某年第一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}
	
	/**
	 * 获取某年最后一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearLast(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		
		return currYearLast;
	}

}