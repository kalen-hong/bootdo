package com.openapi.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author yuanbo.liu
 */
public class TimeUtil {
    public static Date getYesterdayStart() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 获取指定天数的 开始时间
     * @param day   负数表示前几天 正数表示后
     * @return
     */
    public static Date getOffsetStart(int day) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    /**
     * 获取指定天数的 结束时间
     * @param day  负数表示前几天 正数表示后
     * @return
     */
    public static Date getOffsetEnd(int day) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static Date getYesterdayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }


    public static long getCurTimeLong() {
        return System.currentTimeMillis() / 1000;
    }

    public static boolean isBeforeTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());
        // 截取当前时间时分
        int strDateH = Integer.parseInt(now.substring(11, 13));
        int strDateM = Integer.parseInt(now.substring(14, 16));
        //截取判断的 时间  时分
        int strDateBeginH = Integer.parseInt(time.substring(0, 2));
        int strDateBeginM = Integer.parseInt(time.substring(3, 5));
        //小时数小 或者 小时相等 分钟小
        return strDateH < strDateBeginH || (strDateH == strDateBeginH && strDateM < strDateBeginM);
    }

    public static String getCurTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getCurTime(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    public static Integer stringToIntTime(String time) {

        if (StringUtils.isEmpty(time)) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            try {
                date = sdf.parse(time);
            } catch (ParseException e) {
            }
            return (int) (date.getTime() / 1000);
        }

    }
    public static Long stringToSecondTimestamp(String str) {
        if(str == null || str.length() == 0) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            try {
                date = sdf.parse(str);
            }catch (ParseException e) {}

            return date.getTime() / 1000;
        }
    }

    @Deprecated
    public static int getTimestampOfSecond(Date date) {
        if (null == date) {
            return 0;
        } else {
            String timestamp = String.valueOf(date.getTime() / 1000L);
            return Integer.valueOf(timestamp);
        }
    }

    public static Integer getTimestampOfSecond() {
        return getTimestampOfSecond(new Date());
    }

    /**
     * 获取从今天起固定偏移天数的日期特定格式字符串
     * 如 获取前一天的日期字符串 getOffsetDateString(-1,"yyyyMMdd")
     *
     * @param dateOffset 日期偏移量 为正时增加天数，为负时减少天数
     * @param dateFormat 时间格式 如：yyyyMMdd
     * @return java.lang.String
     * @author wenhao.wang
     * @version v1.0
     * @date 2019/7/3 17:20
     */
    public static String getOffsetDateString(int dateOffset, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, dateOffset);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(calendar.getTime());
    }

    public static String getOffsetDateString(Date date, int dateOffset, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, dateOffset);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(calendar.getTime());
    }

    public static long getOffsetDateString(Date date, int dateOffset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dateOffset);
        return calendar.getTime().getTime();
    }


    public static void main(String[] args) {
//        System.out.println(isBeforeTime("19:00"));

    }

    /**
     * 时间戳按指定格式转化为日期（String）
     * @param timestamp 时间戳，秒
     * @param pattern
     * @return
     */
    public static String convertTimestamp2Date(Long timestamp, String pattern) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(new Date(timestamp * 1000));
    }

    public static String formatDate(String pattern) {
        pattern = pattern == null? "yyyy-MM-dd": pattern;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取一个之后的时间
     * @param pattern
     * @return
     */
    public static  String getNextMonth(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MONTH, 1);
        date = rightNow.getTime();
        return sdf.format(date);
    }

    public static Integer getNextMonth() {
        Date date = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MONTH, 1);
        return (int)(date.getTime()/1000);
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *

     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(String start, String end) throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date beforeDate = df.parse(start);
        beforeDate.setTime(beforeDate.getTime());
        Date afterDate = df.parse(end);
        afterDate.setTime(afterDate.getTime());
        Date time = df.parse(df.format(new Date()));
        return time.after(beforeDate) && time.before(afterDate);
    }

    /**
     * 将传入的时间字符串转换为另一种格式
     * 如 2019-07-17 11:16:35  patternSrc "yyyy-MM-dd HH:mm:ss" patternDir "yyyy.MM.dd HH:mm:ss" =>2019.07.17 11:16:35
     *
     * @param timeStr
     * @param patternSrc
     * @param patternDir
     * @return java.lang.String
     * @author wenhao.wang
     * @version v1.0
     * @date 2019/7/17 11:14
     */
    public static String timeStringTransform(String timeStr, String patternSrc, String patternDir) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(patternSrc);
        Date time = null;
        try {
            time = dateFormat.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dirDateFormat = new SimpleDateFormat(patternDir);
        return dirDateFormat.format(time);
    }


    /**
     * 将当前时间戳加上一个月
     * @param time
     * @return
     */
    public static long getNextMonth(long time) {
        Date date = new Date( time* 1000);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MONTH, 1);
        return rightNow.getTime().getTime()/1000;
    }

    /**
     * 如 获取前多少分钟 getOffsetDateString(-20,"yyyyMMdd")
     * @param dateOffset
     * @param dateFormat
     * @return
     */
    public static String getOffsetMinString(int dateOffset, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, dateOffset);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(calendar.getTime());
    }
}
