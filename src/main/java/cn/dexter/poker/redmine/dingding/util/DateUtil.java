package cn.dexter.poker.redmine.dingding.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        if (date == null)
            return "";
        return new SimpleDateFormat(pattern).format(date);
    }


    /**
     * 格式化当前时间
     *
     * @param pattern
     * @return
     */
    public static String formatNow(String pattern) {
        return format(new Date(), pattern);
    }


    /**
     * 格式化当前时间-yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatTime() {
        return formatNow("yyyy-MM-dd HH:mm:ss");
    }

    public static Date toDate(String dateString, String format) {
        if (StringUtil.isEmpty(dateString))
            return null;
        DateFormat df = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = df.parse(dateString);
        } catch (Exception e) {
            LOGGER.error("日期转换错误[{}][{}]", dateString, format);
        }
        return d;
    }

    /**
     * 毫秒 转 日期
     *
     * @param s 毫秒
     * @return
     */
    public static Date timestampString2Date(String s) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.valueOf(s));
        return c.getTime();
    }

    /**
     * 指定日期上加时间
     * @param date
     * @param seconds  秒
     * @return
     */
    public static Date addSecondsToDate(Date date, int seconds){
        long l = date.getTime() + seconds * 1000;
        return timestampString2Date(String.valueOf(l));
    }





}
