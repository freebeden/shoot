package com.yysports.shoot.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author:john
 * @Description:数据处理工具类
 * @Date:17:18 2018/10/19
 */
public class DateUtils {

    /**
     * @Author:john
     * @Description: yyyy 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String DEFAULT_YEAR_FORMAT = "yyyy";
    /**
     * @Author:john
     * @Description:MM月dd日 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String SIMPLE_DATE_FORMAT = "MM月dd日";
    /**
     * @Author:john
     * @Description: yyyy-MM-dd 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * @Author:john
     * @Description:yyyy-MM-dd HH:mm:ss 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String DEFAULT_DATETIME_FORMAT1 = "yyyy-MM-dd HH:mm:ss";
    /**
     * @Author:john
     * @Description:yyyy-MM-dd HH:mm 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String DEFAULT_DATETIME_FORMAT2 = "yyyy-MM-dd HH:mm";
    /**
     * @Author:john
     * @Description:yyyy/MM/dd HH:mm:ss 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String DEFAULT_DATETIME_FORMAT3 = "yyyy/MM/dd HH:mm:ss";
    /**
     * @Author:john
     * @Description:yyyy-MM-dd HH24:mm:ss 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String DEFAULT_DATETIME_FORMAT4 = "yyyy-MM-dd HH24:mm:ss";
    /**
     * @Author:john
     * @Description:yyyy-MM-dd HH24:mm 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String DEFAULT_DATETIME_FORMAT5 = "yyyy-MM-dd HH24:mm";
    /**
     * @Author:john
     * @Description:YYYY-MM-DD:HH24:MI:SS 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String DEFAULT_DATETIME_FORMAT6 = "YYYY-MM-DD:HH24:MI:SS";
    /**
     * @Author:john
     * @Description:yyyy/MM/dd 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String DEFAULT_DATETIME_FORMAT7 = "yyyy/MM/dd";
    /**
     * @Author:john
     * @Description:yyyyMMddHHmmss 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String DEFAULT_DATETIME_FORMAT8 = "yyyyMMddHHmmss";
    /**
     * @Author:john
     * @Description:yyyyMMdd 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String DEFAULT_DATETIME_FORMAT9 = "yyyyMMdd";
    /**
     * @Author:john
     * @Description:yyyyMM 时间格式
     * @Date:22:54 2019/3/11
     */
    public static final String DEFAULT_DATETIME_FORMAT10 = "yyyyMM";
    /**
     * @Author:john
     * @Description:HH:mm:ss 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    /**
     * @Author:john
     * @Description:yyyyMMdd 时间格式
     * @Date:17:20 2018/10/19
     */
    /**
     * @Author:john
     * @Description:HH:mm 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String OTHER_TIME_FORMAT = "HH:mm";
    /**
     * @Author:john
     * @Description:HHmmss 时间格式
     * @Date:17:20 2018/10/19
     */
    public static final String HOUR_MIN_SEC_FORMAT = "HHmmss";
    /**
     * @Author:john
     * @Description:UTC格式
     * @Date:17:20 2018/10/19
     */
    public static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
    private static final String ERROR_MSG = "格式化异常";
    /**
     * @Author:john
     * @Description:yyyy 时间格式
     * @Date:17:20 2018/10/19
     */

    private final SimpleDateFormat sdfYear = new SimpleDateFormat(DEFAULT_YEAR_FORMAT);
    /**
     * @Author:john
     * @Description:yyyy-MM-dd 时间格式
     * @Date:17:20 2018/10/19
     */
    private final SimpleDateFormat sdfDay = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    /**
     * @Author:john
     * @Description:yyyyMMdd 时间格式
     * @Date:17:20 2018/10/19
     */
    private final SimpleDateFormat sdfDays = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT9);
    /**
     * @Author:john
     * @Description:yyyy-MM-dd HH:mm:ss 时间格式
     * @Date:17:20 2018/10/19
     */
    private final SimpleDateFormat sdfTime = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT1);

    /**
     * @Author:john
     * @Description:获取最大时间
     * @Date:16:48 2018/9/11
     */
    public static Date getMaxDate() {
        return getDate("9999-12-31", DEFAULT_DATE_FORMAT, null);
    }

    /**
     * @Author:john
     * @Description: 格式化方法 yyyyMMddHHmmss
     * @Date:15:59 2018/9/11
     */
    public static String getTransDate() {
        return formatDate(new Date(), DEFAULT_DATETIME_FORMAT8, null);
    }

    /**
     * @Author:john
     * @Description: 将时间参数 格式化 yyyy-MM-dd HH:mm:ss
     * @params:date
     * @Date:16:00 2018/9/11
     */
    public static Date getDateTime(String date) {
        if (StringUtils.isNotBlank(date)) {
            date = date.replaceAll("/", "-");
            return getDate(date, DEFAULT_DATETIME_FORMAT1, null);
        }
        return null;
    }

    /**
     * @Author:john
     * @Description:制定时间格式化
     * @params: date, format
     * @Date:16:01 2018/9/11
     */
    public static long getDateMilles(Date date, String format) {
        String formateDate = new SimpleDateFormat(format).format(date);
        try {
            return new SimpleDateFormat(format).parse(formateDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            LOGGER.info(ERROR_MSG + ",{}", e);
        }
        return 0L;
    }

    /**
     * @Author:john
     * @Description:制定时间格式化
     * @params: date, format
     * @Date:16:01 2018/9/11
     */
    public static Date getDate(String date, String format) {
        return getDate(date, format, null);
    }

    /**
     * @Author:john
     * @Description:制定时间静态格式化
     * @params: date, format
     * @Date:16:01 2018/9/11
     */
    public static Date getDate(String date, String format, Date defVal) {
        Date d;
        try {
            d = new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            d = defVal;
        }
        return d;
    }

    /**
     * @Author:john
     * @Description:制定时间静态格式化
     * @params: date, format
     * @Date:16:01 2018/9/11
     */
    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_DATE_FORMAT, null);
    }

    /**
     * @Author:john
     * @Description:制定时间静态格式化
     * @params: date, format
     * @Date:16:01 2018/9/11
     */
    public static String forDatetime(Date date) {
        if (date != null) {
            return formatDate(date, DEFAULT_DATETIME_FORMAT1, null);
        }
        return null;
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static String formatTime(Date date) {
        return formatDate(date, DEFAULT_TIME_FORMAT, null);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static String formatTime(Date date, String format) {
        return formatDate(date, format, null);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static String formatDate(Date date, String format) {
        return formatDate(date, format, null);
    }

    /**
     * @Author:john
     * @Description:按照 yyyyMMdd 格式化时间
     * @Date:16:48 2018/9/11
     */
    public static String formatDate2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT9);
        String str = sdf.format(date);
        return str;
    }

    /**
     * @Author:john
     * @Description:指定时间以及格式进行时间格式化
     * @para: date，format，timeZone
     * @Date:16:48 2018/9/11
     */
    public static String formatDateTimeZone(Date date, String format, TimeZone timeZone) {
        String ret = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(timeZone);
            ret = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("系统异常", e);
        }
        return ret;
    }

    /**
     * @Author:john
     * @Description:指定参数进行时间格式化
     * @para:date,format,defVal
     * @Date:16:48 2018/9/11
     */
    public static String formatDate(Date date, String format, String defVal) {
        String ret;
        ret = new SimpleDateFormat(format).format(date);
        return ret;
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date plusDays(Date date, int days) {
        if (date == null) {
            date = getToday();
        }
        return changeDays(date, days);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date plusHours(Date date, int hours) {
        if (date == null) {
            date = getToday();
        }
        return changeHours(date, hours);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date plusMinute(Date date, int minutes) {
        if (date == null) {
            date = getToday();
        }
        return changeMinute(date, minutes);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date plusMonth(Date date, int months) {
        if (date == null) {
            date = getToday();
        }
        return changeMonth(date, months);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date plusYear(Date date, int years) {
        if (date == null) {
            date = getToday();
        }
        return changeYear(date, years);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date getToday() {
        return new Date();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static long currentTimeMillis() {
        return getToday().getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static java.sql.Date getTodaySqlDate() {
        return new java.sql.Date(getToday().getTime());
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static String getTodayStr(Date date, String format) {
        if (date == null) {
            date = getToday();
        }
        if (StringUtils.isBlank(format)) {
            format = DEFAULT_DATE_FORMAT;
        }
        return formatDate(date, format);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static int intervalDay(Date d1, Date d2) {
        if (d1 == null) {
            d1 = getToday();
        }
        long intervalMillSecond = setToDayStartTime(d1).getTime() - setToDayStartTime(d2).getTime();

        return (int) (intervalMillSecond / 86400000L);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static int intervalMinutes(Date date1, Date date2) {
        long intervalMillSecond = date1.getTime() - date2.getTime();

        return (int) (intervalMillSecond / 60000L + (intervalMillSecond % 60000L > 0L ? 1 : 0));
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static int intervalSeconds(Date date1, Date date2) {
        long intervalMillSecond = date1.getTime() - date2.getTime();

        return (int) (intervalMillSecond / 1000L + (intervalMillSecond % 1000L > 0L ? 1 : 0));
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date setToDayStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(date.getTime());
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);

        return calendar.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date setToDayEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(date.getTime());
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        calendar.set(14, 0);

        return calendar.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static String getDateStatus() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(11);
        if ((hour >= 6) && (hour < 12)) {
            return "morning";
        }
        if ((hour >= 12) && (hour < 18)) {
            return "noon";
        }
        if ((hour >= 18) && (hour < 24)) {
            return "evning";
        }
        return "midnight";
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static int getAge(Date birthday) {
        Calendar now = Calendar.getInstance();
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthday);

        int year = birth.get(1);

        int age = now.get(1) - year;

        now.set(1, year);
        age = now.before(birth) ? age - 1 : age;
        return age;
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static boolean isSameDate(Date d1, Date d2) {
        if ((d1 == null) || (d2 == null)) {
            return false;
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(d1.getTime());
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(d2.getTime());

        return (c1.get(1) == c2.get(1)) && (c1.get(2) == c2.get(2)) && (c1.get(5) == c2.get(5));
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static boolean isContinueDay(Date d1, Date d2) {
        if ((d1 == null) || (d2 == null)) {
            return false;
        }
        return intervalDay(d1, d2) == 1;
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date truncDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return c.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date truncDateHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(12, 0);
        c.set(13, 0);
        return c.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static String getCnDecade(Date input) {
        String day = formatDate(input);
        if (day == null) {
            return null;
        }
        String decade = day.replaceAll("01日", "上旬").replaceAll("11日", "中旬").replaceAll("21日", "下旬");
        return decade;
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date getTodayZero() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return c.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date getTheDayBefore(Date date) {
        return new Date(date.getTime() - 86400000L);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date[] getTenDayBefore() {
        Date[] ret = new Date[2];
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        int day = c.get(5);
        if (day < 10) {
            c.set(5, 1);
            ret[1] = new Date(c.getTime().getTime());
            c.setTime(getTheDayBefore(c.getTime()));
            c.set(5, 21);
            ret[0] = new Date(c.getTime().getTime());
        } else if ((10 < day) && (day <= 20)) {
            c.set(5, 1);
            ret[0] = new Date(c.getTime().getTime());
            c.set(5, 11);
            ret[1] = new Date(c.getTime().getTime());
        } else {
            c.set(5, 11);
            ret[0] = new Date(c.getTime().getTime());
            c.set(5, 21);
            ret[1] = new Date(c.getTime().getTime());
        }

        return ret;
    }

    /**
     * 获取当前日期
     *
     * @return
     * @author lyw
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date[] getCurrentTenDay(Date input) {
        Date[] ret = new Date[2];
        Calendar c = Calendar.getInstance();
        c.setTime(input);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        int day = c.get(5);
        if (day < 10) {
            c.set(5, 1);
            ret[0] = new Date(c.getTime().getTime());
            c.set(5, 11);
            ret[1] = new Date(c.getTime().getTime());
        } else if ((10 < day) && (day <= 20)) {
            c.set(5, 11);
            ret[0] = new Date(c.getTime().getTime());
            c.set(5, 21);
            ret[1] = new Date(c.getTime().getTime());
        } else {
            c.set(5, 21);
            ret[0] = new Date(c.getTime().getTime());
            ret[1] = getNextMonthFirst(c.getTime());
        }

        return ret;
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date getNextMonthFirst(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.add(2, 1);
        c.set(5, 1);
        return c.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date[] getTheMonthBefore(Date date) {
        Date[] ret = new Date[2];
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(5, 1);
        ret[1] = new Date(c.getTime().getTime());
        c.setTime(getTheDayBefore(c.getTime()));
        c.set(5, 1);
        ret[0] = new Date(c.getTime().getTime());
        return ret;
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Integer getCurrentQuarter() {
        int month = Integer.parseInt(formatDate(new Date(), "MM"));
        int quarter = 0;
        if ((month >= 1) && (month <= 3)) {
            quarter = 1;
        } else if ((month >= 4) && (month <= 6)) {
            quarter = 2;
        } else if ((month >= 7) && (month <= 9)) {
            quarter = 3;
        } else if ((month >= 10) && (month <= 12)) {
            quarter = 4;
        }
        return Integer.valueOf(quarter);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Map<String, String> getQuarterToYearMonthDay(Integer year, Integer quarter) {
        if ((year != null) && (year.intValue() > 0) && (quarter != null) && (quarter.intValue() > 0)) {
            Map map = new HashMap();
            if (quarter.intValue() == 1) {
                map.put("startTime", year + "-01-" + getMonthDays(year, Integer.valueOf(1)) + " 00:00:00");
                map.put("endTime", year + "-03-" + getMonthDays(year, Integer.valueOf(3)) + " 23:59:59");
            } else if (quarter.intValue() == 2) {
                map.put("startTime", year + "-04-" + getMonthDays(year, Integer.valueOf(4)) + " 00:00:00");
                map.put("endTime", year + "-06-" + getMonthDays(year, Integer.valueOf(6)) + " 23:59:59");
            } else if (quarter.intValue() == 3) {
                map.put("startTime", year + "-07-" + getMonthDays(year, Integer.valueOf(7)) + " 00:00:00");
                map.put("endTime", year + "-09-" + getMonthDays(year, Integer.valueOf(9)) + " 23:59:59");
            } else if (quarter.intValue() == 4) {
                map.put("startTime", year + "-10-" + getMonthDays(year, Integer.valueOf(10)) + " 00:00:00");
                map.put("endTime", year + "-12-" + getMonthDays(year, Integer.valueOf(12)) + " 23:59:59");
            }
            return map;
        }
        return null;
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Integer getMonthDays(Integer year, Integer month) {
        if ((year != null) && (year.intValue() > 0) && (month != null) && (month.intValue() > 0)) {
            Calendar c = Calendar.getInstance();
            c.set(1, year.intValue());
            c.set(2, month.intValue());
            c.set(5, 1);
            c.add(5, -1);
            return Integer.valueOf(c.get(5));
        }
        return Integer.valueOf(0);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static String getTimeDiffText(Date date1, Date date2) {
        long diff = Math.abs(date1.getTime() - date2.getTime()) / 1000L;
        long minuteSeconds = 60L;
        long hourSeconds = minuteSeconds * 60L;
        long daySeconds = hourSeconds * 24L;
        long weekSeconds = daySeconds * 7L;
        Date min = date1.compareTo(date2) < 0 ? date1 : date2;

        if (diff >= weekSeconds) {
            return formatDate(min);
        }
        if (diff >= daySeconds) {
            return diff / daySeconds + "天前";
        }
        if (diff >= hourSeconds) {
            return diff / hourSeconds + "小时前";
        }
        if (diff >= minuteSeconds) {
            return diff / minuteSeconds + "分钟前";
        }
        return diff + "秒前";
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static int getWeek(Date dt) {
        int[] week = { 7, 1, 2, 3, 4, 5, 6 };

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(7) - 1;
        if (w < 0) {
            w = 0;
        }
        return week[w];
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date getCurrentDate(String datePattern) {
        try {
            return new SimpleDateFormat(datePattern).parse(getCurrentDateByString(datePattern));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static String getCurrentDateByString(String datePattern) {
        return new SimpleDateFormat(datePattern).format(Long.valueOf(System.currentTimeMillis()));
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static String getCurrentDateByString(Date date, String datePattern) {
        return new SimpleDateFormat(datePattern).format(date);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static boolean beforeDate(Date date1, Date date2) {
        return date1.before(date2);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static boolean beforeDate(String date1, String date2) {
        Date dt1 = getDateTime(date1);
        Date dt2 = getDateTime(date2);
        if (dt1 == null || dt2 == null) {
            return false;
        }
        boolean result = beforeDate(dt1, dt2);
        return result;
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static boolean betweenDateScope(String date, String from, String end) {
        if ((date == null) || (from == null) || (end == null)) {
            return false;
        }
        return (!beforeDate(date, from)) && (beforeDate(date, end));
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static boolean checkTimeRange(String time, String startRange, String endRange) {
        String[] s = startRange.split(":");
        int totalStart = Integer.parseInt(s[0]) * 3600 + Integer.parseInt(s[1]) * 60 + Integer.parseInt(s[2]);
        String[] e = endRange.split(":");
        int totalEnd = Integer.parseInt(e[0]) * 3600 + Integer.parseInt(e[1]) * 60 + Integer.parseInt(e[2]);

        String[] t = time.split(":");
        int timeTotal = Integer.parseInt(t[0]) * 3600 + Integer.parseInt(t[1]) * 60 + Integer.parseInt(t[2]);
        return (timeTotal >= totalStart) && (timeTotal <= totalEnd);
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    private static Date changeMinute(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(12, minutes);
        return cal.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    private static Date changeHours(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(11, hours);
        return cal.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    private static Date changeDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(6, days);
        return cal.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    private static Date changeYear(Date date, int years) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(1, years);
        return cal.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date changeMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(2, months);
        return cal.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date getCurrentDayBegin() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date getCurrentDayEnd() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        return calendar.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date getLastDayBegin() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.add(5, -1);
        return calendar.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date getLastDayEnd() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        calendar.add(5, -1);
        return calendar.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date getBeforeYesterdayBegin() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.add(5, -2);
        return calendar.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date getBeforeYesterdayEnd() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        calendar.add(5, -2);
        return calendar.getTime();
    }

    /**
     * @Author:john
     * @Description:
     * @Date:16:48 2018/9/11
     */
    public static Date getCurrentMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, 0);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(5, 1);
        return calendar.getTime();
    }

    /**
     * @Author:john
     * @Description:30天前时间
     * @Date:16:46 2018/9/11
     */
    public static Date getLastMonthDayBegin() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.add(5, -30);
        return calendar.getTime();
    }


    /**
     * @Author:john
     * @Description:5年前时间
     * @Date:16:46 2018/9/11
     */
    public static Date getBeginTime() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.add(1, -5);
        return calendar.getTime();
    }

    /**
     * @Author:john
     * @Description:字符串形式比较时间大小
     * @params: d1, d2
     * @Date:16:40 2018/9/11
     */
    public static boolean compareDate(String s, String e) {
        Date date1 = fomatDate(s);
        Date date2 = fomatDate(e);
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.getTime() >= date2.getTime();
    }

    /**
     * @Author:john
     * @Description:比较时间大小
     * @params: d1, d2
     * @Date:16:40 2018/9/11
     */
    public static boolean compareDate(Date d1, Date d2) {
        return d1.getTime() >= d2.getTime();
    }

    /**
     * @Author:john
     * @Description:按照时间格式 yyyy-MM-dd 格式化date参数时间
     * @params:date
     * @Date:16:38 2018/9/11
     */
    public static Date fomatDate(String date) {
        DateFormat fmt = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author:john
     * @Description:按照时间格式 yyyy-MM-dd 格式化字符串参数时间
     * @params:s
     * @Date:16:38 2018/9/11
     */
    public static boolean isValidDate(String s) {
        DateFormat fmt = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            fmt.parse(s);
            return true;
        } catch (Exception e) {
            LOGGER.info("yyyy-MM-dd格式化异常", e);
        }
        return false;
    }

    /**
     * @Author:john
     * @Description:年份时间差
     * @params:startTime，endTime
     * @Date:16:37 2018/9/11
     */
    public static int getDiffYear(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            int years = (int) ((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / 86400000L / 365L);
            return years;
        } catch (Exception e) {
            LOGGER.info("yyyy-MM-dd格式化异常", e);
        }
        return 0;
    }

    /**
     * @Author:john
     * @Description: 开始时间至结束时间的天数（两者相减的天数比实际天数少1天)
     * @params:beginDateStr，endDateStr
     * @Date:16:36 2018/9/11
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0L;
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (endDate == null || beginDate == null) {
            return 0;
        }
        day = (endDate.getTime() - beginDate.getTime()) / 86400000L;
        return day;
    }

    /**
     * @Author:john
     * @Description:按照指定格式 "yyyy-MM-dd HH:mm:ss 时间将日期进行调整
     * @params:
     * @Date:16:36 2018/9/11
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance();
        canlendar.add(5, daysInt);
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT1);
        String dateStr = sdfd.format(date);
        return dateStr;
    }

    /**
     * @Author:john
     * @Description: 按照指定时间将日期进行调整
     * @params:days
     * @Date:16:30 2018/9/11
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance();
        canlendar.add(5, daysInt);
        Date date = canlendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * @Author:john
     * @Description: 时间拼接
     * @params: date, buff
     * @Date:16:28 2018/9/11
     */
    public static String getSearchBeginDate(String date, SearchDateBuff buff) {
        StringBuilder builder = new StringBuilder(date);
        builder.append(" ").append(buff.toString());
        return builder.toString();
    }

    /**
     * @Author:john
     * @Description:获得当前时间的秒数
     * @Date:16:27 2018/9/11
     */
    public static int getCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    /**
     * @Author:john
     * @Description:获取特殊时间的前一天
     * @params: specifiedDay, format, defaultStr
     * @Date:16:26 2018/9/11
     */
    public static String getSpecifiedDayBefore(String specifiedDay, String format, String defaultStr) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        String dayBefore = "";
        try {
            date = new SimpleDateFormat(format).parse(specifiedDay);
            c.setTime(date);
            int day = c.get(5);
            c.set(5, day - 1);

            dayBefore = new SimpleDateFormat(format).format(c.getTime());
        } catch (Exception e) {
            LOGGER.info(ERROR_MSG, e);
            return defaultStr;
        }
        return dayBefore;
    }

    /**
     * @return @Description：将天数换算成月（day/30 余数满15进1）
     * @author：lyw
     * @date: 2016年10月22日 下午4:57:10
     */
    public static Integer dayChangeMonth(Integer dayNum) {
        if (dayNum == null) {
            return null;
        }
        return dayNum / 30 + (dayNum % 30 >= 15 ? 1 : 0);
    }

    /**
     * @Author:john
     * @Description:给定时间点是否在0点到1点之间 compareDate
     * @params:nowDate
     * @Date:16:18 2018/9/11
     */
    public static boolean inWeeHours(Date nowDate) {
        Date zeroTime = getCurrentDayBegin();// 0点的时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        cal.set(Calendar.HOUR_OF_DAY, 1);// 小时为1点
        cal.set(Calendar.MINUTE, 0);// 分钟
        cal.set(Calendar.SECOND, 0);// 分钟
        Date oneTime = cal.getTime();
        return nowDate.after(zeroTime) && oneTime.after(nowDate);
    }

    /**
     * @Author:john
     * @Description:根据指定日期，获取该日期当月的天数
     * @params:date
     * @Date:16:18 2018/9/11
     */
    public static int getDaysOfDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, 1);
        c.roll(Calendar.DATE, -1);
        return c.get(Calendar.DATE);
    }

    /**
     * @Author:john
     * @Description:比较两个日期的
     * @params: DATE1, DATE2, DATE_FORMAT
     * @Date:16:17 2018/9/11
     */
    public static int compareDate(String date1, String date2, String date_format) {
        DateFormat df = new SimpleDateFormat(date_format);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            LOGGER.info(ERROR_MSG, exception);
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * @Author:john
     * @Description:转为日期
     * @params:time
     * @Date:16:17 2018/9/11
     */
    public static Date int2Date(int time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000L);
        return calendar.getTime();
    }

    /**
     * @Author:john
     * @Description:比较三个日期，第1个日期是否是在2,3 日期之间，如果2,3日期的时间段包含1，则返回1，否则返回0
     * @params: DATE1, DATE2, DATE3
     * @Date:16:17 2018/9/11
     */
    public static int rangeDateCompar(String date1, String date2, String date3) {
        DateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            Date dt3 = df.parse(date3);
            if (dt1.getTime() >= dt2.getTime() && dt1.getTime() <= dt3.getTime()) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            LOGGER.info(ERROR_MSG, ex);
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * @Author:john
     * @Description:获取前一天日期
     * @params:date
     * @Date:16:16 2018/9/11
     */
    public static Date getLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    /**
     * @Author:john
     * @Description:获取后一天日期
     * @params:date
     * @Date:16:16 2018/9/11
     */
    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        date = calendar.getTime();
        return date;
    }

    public static long getHourBetweenDays(Date start, Date end) {
        Long diffMis = end.getTime() - start.getTime();
        return Math.abs(diffMis / 3600000);
    }

    /**
     * @param endDate
     * @param beforeDate
     * @return
     * @Description：计算两个日期相差的月份（相差不满30天算0月） @author：lyw
     * @date: 2017年9月25日 下午6:01:32
     */
    public static int getMonthDiff(Date endDate, Date beforeDate) {
        // DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(endDate);
        c2.setTime(beforeDate);
        // 设置0时0点
        c1.set(Calendar.HOUR, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.HOUR, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);
        // 比较日期
        if (c1.getTimeInMillis() < c2.getTimeInMillis()) {
            return 0;
        }
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16 d2 = 2011-9-30
        int yearInterval = year1 - year2;
        // 获取月数差值
        int monthInterval = month1 - month2;
        int monthDiff = yearInterval * 12 + monthInterval;
        // 修正月份
        Calendar c3 = (Calendar) c2.clone();
        // 计算以整月计算的预期日期
        c3.add(Calendar.MONTH, monthDiff);
        // System.out.println("monthDiff:"+monthDiff);
        // System.out.println("c1:"+format.format(c1.getTime()));
        // System.out.println("c2:"+format.format(c2.getTime()));
        // System.out.println("c3:"+format.format(c3.getTime()));
        // System.out.println("c3.getTimeInMillis():"+c3.getTimeInMillis());
        // System.out.println("c1.getTimeInMillis():"+c1.getTimeInMillis());
        // 比预期日期与c1判断是否满一个月
        if (c3.getTimeInMillis() > c1.getTimeInMillis()) {
            // 不足一个月
            monthDiff--;
        }
        return monthDiff;
    }

    /**
     * @Author:john
     * @Description: 时间格式化
     * @Date:16:08 2018/9/11
     */
    public static String longToDate(long sdate) {
        SimpleDateFormat sf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sdate);
        return sf.format(calendar.getTime());
    }

    /**
     * @Author:john
     * @Description: 获取最早时间点
     * @params: date
     * @Date:16:05 2018/9/11
     */
    public static Date getCurrentDateBegin() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
                0, 0);
        Date beginOfDate = calendar.getTime();
        return beginOfDate;
    }

    /**
     * @Author:john
     * @Description: 获取最晚时间点
     * @params: date
     * @Date:16:05 2018/9/11
     */
    public static Date getCurrentDateEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
                59, 59);
        Date endOfDate = calendar.getTime();
        return endOfDate;
    }

    /**
     * @Author:john
     * @Description: 获取最早时间点
     * @params: date
     * @Date:16:05 2018/9/11
     */
    public static Date getDateStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
                0, 0);
        Date endOfDate = calendar.getTime();
        return endOfDate;
    }

    /**
     * @Author:john
     * @Description: 获取最晚时间点
     * @params: date
     * @Date:16:05 2018/9/11
     */
    public static Date getDateEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
                59, 59);
        Date endOfDate = calendar.getTime();
        return endOfDate;
    }

    /**
     * @param endDate
     * @param beforeDate
     * @return
     * @Description：计算两个日期相差的月份-不修正(相差不满30天也算1月) @author：lyw
     * @date: 2017年9月25日 下午6:01:32
     */
    public static int getMonthDiffNoAmendment(Date endDate, Date beforeDate) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(endDate);
        c2.setTime(beforeDate);
        if (c1.getTimeInMillis() < c2.getTimeInMillis()) {
            return 0;
        }
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16 d2 = 2011-9-30
        int yearInterval = year1 - year2;
        // 获取月数差值
        int monthInterval = month1 - month2;
        return yearInterval * 12 + monthInterval;
    }

    /**
     * 得到当前时间的日期 YYYYMMDD 创 建 人: wenc 创建时间: 2014年6月23日 下午5:54:28
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getCurDT() {
        return getCurTime(DEFAULT_DATETIME_FORMAT9);
    }

    /**
     * 得到当前时间 格式 HHMMSS 创 建 人: wenc 创建时间: 2014年6月23日 下午5:55:10
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getCurTM() {
        return getCurTime(HOUR_MIN_SEC_FORMAT);
    }

    /**
     * 当前时间 创 建 人: wenc 创建时间: 2014年6月26日 下午2:51:54
     *
     * @param format
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getCurTime(String format) {
        StringBuilder str = new StringBuilder();
        Date ca = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        str.append(sdf.format(ca));
        return str.toString();
    }

    /**
     * @param startTime 开始时间戳 long startTime = System.currentTimeMillis();
     * @return
     * @Description: * 取得线程耗时
     * @author zhou_jn@suixingpay.com
     * @date 2018年6月28日 下午3:53:46
     */
    public static String getHaoShiTimeMsg(long startTime) {
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        long day = time / (24 * 60 * 60 * 1000);
        long hour = time / (60 * 60 * 1000) - day * 24;
        long min = (time / (60 * 1000)) - day * 24 * 60 - hour * 60;
        long s = time / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
        long hs = time - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000;
        return "耗时: " + day + "天" + hour + "小时 " + min + "分 " + s + "秒 " + hs + "毫秒";
    }

    /**
     * @Author:john
     * @Description:获得上个月时间
     * @params: date
     * @Date:12:06 2019/3/11
     */
    public static Date getPrevMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    /**
     * @Author:john
     * @Description:yyyy-MM-dd
     * @Date:16:48 2018/9/11
     */
    public Date getDate(String date) {
        return getDate(date, DEFAULT_DATE_FORMAT, null);
    }

    /**
     * @Author:john
     * @Description:按照 yyyy格式化时间
     * @Date:16:42 2018/9/11
     */
    public String getYear() {
        return sdfYear.format(new Date());
    }

    /**
     * @Author:john
     * @Description:按照 yyyy-MM-dd 格式化时间
     * @Date:16:42 2018/9/11
     */
    public String getDay() {
        return sdfDay.format(new Date());
    }

    /**
     * @Author:john
     * @Description:按照 yyyyMMdd 格式化时间
     * @Date:16:42 2018/9/11
     */
    public String getDays() {
        return sdfDays.format(new Date());
    }

    /**
     * @Author:john
     * @Description:按照 yyyy-MM-dd HH:mm:ss 格式化时间
     * @Date:16:42 2018/9/11
     */
    public String getTime() {
        return sdfTime.format(new Date());
    }

    /**
     * @Author:john
     * @Description:内部枚举时间
     * @Date:16:19 2018/9/11
     */
    public enum SearchDateBuff {
        SEARCH_BEGIN_TIME("00:00:00"), SEARCH_END_TIME("23:59:59");

        private String buff;

        /**
         * @Author:john
         * @Description:枚举构造方法
         * @params:buff
         * @Date:16:19 2018/9/11
         */
        private SearchDateBuff(String buff) {
            this.buff = buff;
        }

        /**
         * @Author:john
         * @Description:
         * @Date:16:20 2018/9/11
         */
        @Override
        public String toString() {
            return this.buff;
        }
    }

}
