package com.hm.his.framework.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

public class DateTools {


    private static Logger logger = Logger.getLogger(DateTools.class.getName());

    private static String msg = new String();

    public static final String COMMON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FILE_DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    public static final String ONLY_DATE_FORMAT = "yyyy-MM-dd";
    public static final String SHORT_DATE_FORMAT = "yyyyMMdd";

    public static final String THE_DAY_MAX_TIME = " 23:59:59";

    /**
     * 根据给定格式得到当前日期时间
     * @param fmt 需要的日期格式
     * @return 符合格式要求的日期字符串 返回格式一般应为yyyy-MM-dd HH:mm:ss
     */
    public static String getDate(String fmt) {
        Date myDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sDateformat = new SimpleDateFormat(fmt);
        sDateformat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sDateformat.format(myDate).toString();
    }

    /**
     * 根据给定格式得到当前日期时间
     * @return 符合格式要求的日期字符串 返回格式一般应为yyyy-MM-dd HH:mm:ss
     */
    public static String getShortDateFormatDate() {
        Date myDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sDateformat = new SimpleDateFormat(SHORT_DATE_FORMAT);
        sDateformat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sDateformat.format(myDate).toString();
    }

    /**
     * 将指定日期格式化
     * @param fmt
     * @param date
     * @return
     */
    public static String getDate(String fmt,Date date) {
        SimpleDateFormat sDateformat = new SimpleDateFormat(fmt);
        return sDateformat.format(date).toString();
    }


    public static String getCommonDateStr(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(COMMON_DATE_FORMAT);
        return sdf.format(calendar.getTime());
    }

    public static Calendar getCommonDate(String dateStr) {
        try {
            return getCal(dateStr, COMMON_DATE_FORMAT);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 把给定的日期字符串转换成Calendar型
     * @param strdate 给定日期字符串
     * @param fmt 给定日期字符串的格式
     * @return 初始化好的Calendar类
     * @exception ParseException
     */

    public static Calendar getCal(String strdate, String fmt) throws
            Exception {
        Calendar cal = null;
        try {
            // 判断给定参数是否为空，如果空则返回参数错误消息
            if (strdate == null || fmt == null) {
                msg = "Error: Method: DateTools.getCal :Incorrect para";
                String[] args = {
                        msg};
                throw new Exception(
                        "error.app.util.datatools.IncorrectPara", null);
            }
            // 根据给定日期格式得到当前时间
            SimpleDateFormat nowDate = new SimpleDateFormat(fmt);
            // 转换为格式为Date类型
            Date d = nowDate.parse(strdate, new java.text.ParsePosition(0));
            // 如果转换返回Date为空则抛出参数解析错误消息
            if (d == null) {
                msg = "Fatal: Method: DateTools.getCal :Incorrect Parse";
                String[] args = {
                        msg};
                throw new Exception(
                        "error.app.util.datatools.IncorrectParse", null);
            }
            // 得到一个Calendar实例
            cal = Calendar.getInstance();
            // Calendar日期归零
            cal.clear();
            // 设定当前时间
            cal.setTime(d);
        } catch (Exception e) { //此处应该捕获ParseException，由于采用了ParsePosition(0)格式所以此异常不用捕获
            msg = "Error: Method: DateTools.getCal";

        }
        return cal;

    }



    /**
     * 给定日期所在周的全部日期
     * @param strdate 给定的日期字符串
     * @param oldfmt 给定日期的格式
     * @param newfmt 返回日期的格式
     * @return 从周一开始到周日的日期

     */
    public static String[] getWeekDay(String strdate, String oldfmt,
                                      String newfmt) throws
            Exception {
        String weekday[] = new String[7];
        try {
            // 判断给定参数是否为空，如果空则返回参数错误消息
            if (strdate == null || oldfmt == null || newfmt == null) {
                msg = "Error: Method: DateTools.getWeekDay :Incorrect para";
                String[] args = {
                        msg};
                throw new Exception(
                        "error.app.util.datatools.IncorrectPara", null);
            }
            // 根据给定参数转换为Calendar类型
            Calendar cal = getCal(strdate, oldfmt);
            // 转换异常则返回错误消息
            if (cal == null) {
                msg =
                        "Error: Method: DateTools.getWeekDay :Incorrect Calendar";
                String[] args = {
                        msg};
                throw new Exception(
                        "error.app.util.datatools.IncorrectCalendar", null);
            }
            // 得到给定日期是本周的的几天
            int dayOfWeek = cal.get(cal.DAY_OF_WEEK);
            // 修改为中国习惯，从周一开始算一周时间
            cal.add(cal.DATE, -dayOfWeek + 2);
            // 根据参数设定返回格式
            SimpleDateFormat sdf = new SimpleDateFormat(newfmt);
            // 循环得到一周的时间
            weekday[0] = sdf.format(cal.getTime());
            for (int i = 1; i < 7; i++) {
                cal.add(cal.DATE, 1);
                weekday[i] = sdf.format(cal.getTime());
            }
        } catch (IndexOutOfBoundsException iobe) {
            throw new Exception(
                    "error.app.util.datatools.IndexOutOfBoundsException", null);
        }
        return weekday;

    }

    /**
     * 本方法完成得到给定周的全部日期
     * @param year 给定年
     * @param week 给定周
     * @param newfmt 返回日期的格式
     * @return 从周一开始到周日的日期
     */
    public static String[] getWeekDate(String year, int week, String newfmt) throws
            Exception {
        String jweekday[] = new String[7];
        try {
            // 判断给定参数是否为空，如果空则返回参数错误消息
            if (year == null || year.length() < 4 || week <= 0 || newfmt == null) {
                msg = "Error: Method: DateTools.getWeekDate :Incorrect para";
                String[] args = {
                        msg};
                throw new Exception(
                        "error.app.util.datatools.IncorrectPara", null);
            }
            // 根据给定参数转换为Calendar类型,起始计算时间调整为当年的1月1日
            Calendar cal = getCal(year + "0101", "yyyyMMdd");
            // 转换异常则返回错误消息
            if (cal == null) {
                msg =
                        "Error: Method: DateTools.getWeekDate :Incorrect Calendar";
                String[] args = {
                        msg};
                throw new Exception(
                        "error.app.util.datatools.IncorrectCalendar", null);

            }
            // java类的周计算从0开始，调整正常习惯为java描述需要减1
            week = week - 1;
            // 日期调整至当给定周的第一天
            cal.add(cal.DATE, week * 7 - cal.get(cal.DAY_OF_WEEK) + 2);
            // 根据参数设定返回格式
            SimpleDateFormat sdf = new SimpleDateFormat(newfmt);
            jweekday[0] = sdf.format(cal.getTime());
            // 循环得到一周的时间
            for (int i = 1; i < 7; i++) {
                cal.add(cal.DATE, 1);
                jweekday[i] = sdf.format(cal.getTime());
            }
        } catch (IndexOutOfBoundsException iobe) {
            throw new Exception();
        }
        return jweekday;

    }


    /**
     * 本方法完成计算给定时间之前一段时间的日期时间
     * @param deftime 给定时间字符串
     * @param timediff 以小时为单位
     * @param oldfmt 给定时间的格式
     * @param newfmt 返回时间的格式
     * @return 时间字符串
     */
    public static String getBeforeTime(String deftime, String oldfmt,
                                       int timediff,
                                       String newfmt) throws Exception {
        String strBeforeTime = null;
        try {
            // 判断给定参数是否为空，如果空则返回参数错误消息
            if (deftime == null || deftime.equals("")) {
                msg = "Error: Method: DateTools.getBeforeTime :Incorrect para";
                String[] args = {
                        msg};
                throw new Exception(
                        "error.app.util.datatools.IncorrectPara", null);
            }
            // 根据给定参数转换为Calendar类型
            Calendar cal = getCal(deftime, oldfmt);
            // 转换异常则返回错误消息
            if (cal == null) {
                msg =
                        "Error: Method: DateTools.getBeforeTime :Incorrect Calendar";
                String[] args = {
                        msg};
                throw new Exception(
                        "error.app.util.datatools.IncorrectCalendar", null);

            }
            // 以分钟计算之前的日期
            cal.add(cal.MINUTE, -timediff * 60);
            // 根据参数设定返回格式
            SimpleDateFormat sdf = new SimpleDateFormat(newfmt);
            // 格式化返回日期
            strBeforeTime = sdf.format(cal.getTime());
        } catch (Exception e) {
            throw new Exception("error.app.util.datatools.exception", null);
        }
        return strBeforeTime;

    }

    /**
     * 获取给定时间之后具体天数的时间
     * @param deftime String  指定的时间
     * @param oldfmt String   指定的时间格式
     * @param timediff int    天数
     * @param newfmt String   指定的时间格式
     * @return String
     * @throws Exception
     */
    public static String getAfterTime(String deftime, String oldfmt,
                                      int timediff,
                                      String newfmt) throws Exception {
        String strAfterTime = null;
        try {
            // 判断给定参数是否为空，如果空则返回参数错误消息
            if (deftime == null || deftime.equals("")) {
                msg = "Error: Method: DateTools.getBeforeTime :Incorrect para";
                String[] args = {
                        msg};
                throw new Exception(
                        "error.app.util.datatools.IncorrectPara", null);
            }
            // 根据给定参数转换为Calendar类型
            Calendar cal = getCal(deftime, oldfmt);
            // 转换异常则返回错误消息
            if (cal == null) {
                msg =
                        "Error: Method: DateTools.getBeforeTime :Incorrect Calendar";
                String[] args = {
                        msg};
                throw new Exception(
                        "error.app.util.datatools.IncorrectCalendar", null);

            }
            // 以分钟计算之后的日期
            cal.add(cal.MINUTE, +timediff * 60);
            // 根据参数设定返回格式
            SimpleDateFormat sdf = new SimpleDateFormat(newfmt);
            // 格式化返回日期
            strAfterTime = sdf.format(cal.getTime());
        } catch (Exception e) {
            throw new Exception("error.app.util.datatools.exception", null);
        }
        return strAfterTime;

    }

    /**
     * 本方法完成把给定字符串转换成需要格式，如果不够位数则自动0补满位
     * @param mydate 给定时间
     * @param oldfmt 给定时间格式
     * @param newfmt 返回时间格式
     * @return 字符串
     */
    public static String fmtDate(String mydate, String oldfmt, String newfmt) throws
            Exception {
        String restr = null;
        try {
            // 判断给定参数是否为空，如果空则返回参数错误消息
            if (mydate == null || oldfmt == null || newfmt == null) {
                msg = "Error: Method: DateTools.getBeforeTime :Incorrect para";
                String[] args = {
                        msg};
                throw new Exception(
                        "error.app.util.datatools.IncorrectPara", null);

            }
            SimpleDateFormat newDate = new SimpleDateFormat(newfmt);
            // 根据给定参数转换为Calendar类型
            Calendar cal = getCal(mydate, oldfmt);
            // 转换异常则返回错误消息
            if (cal == null) {
                msg =
                        "Error: Method: DateTools.getBeforeTime :Incorrect Calendar";
                String[] args = {
                        msg};
                throw new Exception(
                        "error.app.util.datatools.IncorrectCalendar", null);
            }
            // 按给定格式返回
            restr = newDate.format(cal.getTime());
        } catch (Exception e) {

            throw new Exception("error.app.util.datatools.exception", null);
        }
        return restr;

    }

    //判断当前时间是否在时间date之前
    //时间格式 2005-4-21 16:16:34
    public static boolean isDateBefore(String date) {
        try {
            Date date1 = new Date();
            DateFormat df = DateFormat.getDateTimeInstance();
            return date1.before(df.parse(date));
        } catch (ParseException e) {
            return false;
        }
    }

    //判断当前时间是否在时间date之前
    //时间格式 2005-4-21 16:16:34
    public static boolean isDateBetween(String time, String startTime,
                                        String endTime) {
        try {
            DateFormat df = DateFormat.getDateTimeInstance();
            Date date1 = df.parse(time);
            if (date1.before(df.parse(endTime)) &&
                    date1.after(df.parse(startTime))) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 判断是否超时
     * @param beginDate long
     * @param TimeOutmillis long
     * @return boolean
     */
    public static boolean whileUnitTimerOut(long beginDate, long TimeOutmillis) {
        long currentDate = System.currentTimeMillis();
        long endDate = beginDate + TimeOutmillis;
        return currentDate <= endDate;
    }

    /**
     * 格式化时间
     * @param dateString String
     * @return Date
     */
    public static Date formatDateString(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        try {
            startDate = sdf.parse(dateString);
        } catch (ParseException ex) {
            startDate = null;
        }
        return startDate;
    }

    /**
     * 格式化时间
     * @param dateString String
     * @return Date
     */
    public static Date formatDateString(String dateString,String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date startDate = null;
        try {
            startDate = sdf.parse(dateString);
        } catch (ParseException ex) {
            startDate = null;
        }
        return startDate;
    }
    /*
     * 判断当前的时间 密码是否过期。
     * pwdModDate 密码最后修改一次的时间
     * policyDate 密码策略的制定的天数
     * 现在的时间在周期的后的时间返回true 密码过期
     */
    public static Boolean pwdIsOverdue(String pwdModDate,String policyDate){
        String expiredDateStr = "";
        int policy = Integer.parseInt(policyDate);
        try {
            expiredDateStr = DateTools.getAfterTime(pwdModDate, "yyyy-MM-dd HH:mm:ss",policy * 24, "yyyy-MM-dd HH:mm:ss");
            Calendar expiredDate = DateTools.getCal(expiredDateStr,	"yyyy-MM-dd HH:mm:ss");
            String now = DateTools.getDate("yyyy-MM-dd HH:mm:ss");
            Calendar nowcalendar = DateTools.getCal(now,"yyyy-MM-dd HH:mm:ss");
            return nowcalendar.before(expiredDate);		//
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 把字符串的时间转变成long型 -- Date或者String转化为时间戳
     * @param str 字符串时间
     * @param mode 字符串时间格式
     * @return Date
     */
    public static long parse(String str,String mode) {
        Date date=null;
        SimpleDateFormat formatter = new SimpleDateFormat(mode);
        try {
            date=formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    /**
     * 根据给定格式得到指定日期时间 -- 时间戳转化为Sting
     * @param fmt 需要的日期格式
     * @param datelong 需要转换的long型时间
     * @return
     */
    public static String getLongbyDate(String fmt,long datelong) {
        Date myDate = new Date(datelong);
        SimpleDateFormat sDateformat = new SimpleDateFormat(fmt);
        //sDateformat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sDateformat.format(myDate).toString();
    }

    /**
     * 根据日期得到周几
     * @param dt
     * @return
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    public static void main(String[] ss) throws ParseException {
//        String time = System.currentTimeMillis()+"";
//        long l = Long.valueOf(time);
//        long ll = 5*60*60;
//        boolean b = whileUnitTimerOut(l,ll);
        //时间戳转化为Sting或Date
        SimpleDateFormat format =  new SimpleDateFormat(DateTools.COMMON_DATE_FORMAT);
        Long time=new Long(445555555);
        String d = format.format(time);
        Date date=format.parse(d);
        System.out.println("Format To String(Date):"+d);
        System.out.println("Format To Date:"+date);

        //Date或者String转化为时间戳
        SimpleDateFormat dformat =  new SimpleDateFormat(DateTools.COMMON_DATE_FORMAT);
        String dtime="2999-01-01 08:00:00";
        Date ddate = dformat.parse(dtime);
        System.out.print("Format To times:"+ddate.getTime());
    }
}
