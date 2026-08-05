/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.wgcloud.util.staticvar.StaticKeys;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_PATTERN_ZH = "yyyy\u5e74MM\u6708dd\u65e5 HH:mm:ss";

    public static Timestamp getNowTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        Timestamp nowTime = Timestamp.valueOf(dateFormat.format(new Date()));
        return nowTime;
    }

    public static String getCurrentDateTime() {
        return DateUtil.getCurrentDateTime(DATETIME_PATTERN);
    }

    public static String getCurrentDate() {
        return DateUtil.getCurrentDateTime(DATE_PATTERN);
    }

    public static String getCurrentDateTime(String pattern) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(cal.getTime());
    }

    public static Date getDate(String dateStr) throws ParseException {
        return DateUtil.getDate(dateStr, DATETIME_PATTERN);
    }

    public static Date getDate(String dateStr, String pattern) throws ParseException {
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        date = dateFormat.parse(dateStr);
        return date;
    }

    public static String getDateString(Date date) {
        return DateUtil.getString(date, DATE_PATTERN);
    }

    public static String getDateTimeString(Date date) {
        return DateUtil.getString(date, DATETIME_PATTERN);
    }

    public static String getDateTimeZhString(Date date) {
        return DateUtil.getString(date, DATETIME_PATTERN_ZH);
    }

    public static String getString(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(11);
        return hour;
    }

    public static boolean isClearTime() {
        if (StaticKeys.HISTORY_DATA_TIME_OVER) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(11);
        int mins = calendar.get(12);
        return 6 == hour && mins >= 5 && mins <= 25;
    }

    public static boolean isClearTimeForHost() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(11);
        int mins = calendar.get(12);
        return 6 == hour && mins >= 5 && mins <= 30;
    }

    public static boolean isWarnTime(String cronStr) {
        boolean sign = true;
        if (StringUtils.isEmpty((CharSequence)cronStr)) {
            return sign;
        }
        try {
            String[] cronTimeArr;
            CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor((CronType)CronType.QUARTZ);
            CronParser parser = new CronParser(cronDefinition);
            for (String cronTime : cronTimeArr = cronStr.split("WGCLOUD")) {
                int seconds;
                int minutes;
                int hours;
                int day;
                int month;
                Date date;
                int year;
                LocalDateTime ldt;
                ZonedDateTime zbj;
                ExecutionTime executionTime = ExecutionTime.forCron((Cron)parser.parse(cronTime.trim()));
                sign = executionTime.isMatch(zbj = (ldt = LocalDateTime.of(year = (date = new Date()).getYear() + 1900, month = date.getMonth() + 1, day = date.getDate(), hours = date.getHours(), minutes = date.getMinutes(), seconds = date.getSeconds())).atZone(ZoneId.systemDefault()));
                if (!sign) continue;
                return true;
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u544a\u8b66\u65f6\u95f4\u6bb5\u8868\u8fbe\u5f0f\u9519\u8bef", (Throwable)e);
        }
        return sign;
    }

    public static String getDateAfter(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(5, now.get(5) + day);
        return DateUtil.getString(now.getTime(), DATETIME_PATTERN);
    }

    public static List<String> getDateAfterList(String dateStr, int days) throws ParseException {
        Date date = DateUtil.getDate(dateStr);
        ArrayList<String> dateList = new ArrayList<String>();
        String sevenDayAfter = DateUtil.getDateAfter(date, days);
        for (int i = 1; i < days + 1; ++i) {
            sevenDayAfter = DateUtil.getDateAfter(date, i);
            dateList.add(sevenDayAfter);
        }
        return dateList;
    }

    public static String getDateBefore(int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(5, now.get(5) - day);
        return DateUtil.getString(now.getTime(), DATETIME_PATTERN);
    }

    public static void setBeginEndTime(Map<String, Object> params, int day) {
        String beginTime = DateUtil.getDateBefore(day);
        params.put("startTime", beginTime);
        String endTime = DateUtil.getDateTimeString(new Date());
        params.put("endTime", endTime);
    }

    public static String setLastMonBeginEndTime(Map<String, Object> params) {
        Calendar calendar = Calendar.getInstance();
        int yearBegin = calendar.get(1);
        int monthBegin = calendar.get(2);
        if (0 == monthBegin) {
            monthBegin = 12;
            --yearBegin;
        }
        String lastMonStr = monthBegin + "";
        if (monthBegin < 10) {
            lastMonStr = "0" + monthBegin;
        }
        String beginTime = yearBegin + "-" + lastMonStr + "-01 00:00:00";
        int yearEnd = calendar.get(1);
        int monthEnd = calendar.get(2);
        String nowMonStr = monthEnd + 1 + "";
        if (monthEnd + 1 < 10) {
            nowMonStr = "0" + (monthEnd + 1);
        }
        String endTime = yearEnd + "-" + nowMonStr + "-01 00:00:00";
        params.put("startTime", beginTime);
        params.put("endTime", endTime);
        return yearBegin + "-" + lastMonStr;
    }

    public static String getLastWeekMonday(Date date) {
        SimpleDateFormat foramt = new SimpleDateFormat(DATE_PATTERN);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (1 == cal.get(7)) {
            cal.add(5, -1);
        }
        cal.add(5, -7);
        cal.set(7, 2);
        return foramt.format(cal.getTime()) + " 00:00:00";
    }

    public static String getLastWeekSunday(Date date) {
        SimpleDateFormat foramt = new SimpleDateFormat(DATE_PATTERN);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (1 == cal.get(7)) {
            cal.add(5, -1);
        }
        cal.set(7, 1);
        return foramt.format(cal.getTime()) + " 23:59:59";
    }

    public static String setLastWeekBeginEndTime(Map<String, Object> params) {
        Date date = new Date();
        String beginTime = DateUtil.getLastWeekMonday(date);
        String endTime = DateUtil.getLastWeekSunday(date);
        params.put("startTime", beginTime);
        params.put("endTime", endTime);
        String beginDate = beginTime.substring(0, 10);
        String endDate = endTime.substring(0, 10);
        return beginDate + " \u81f3 " + endDate;
    }

    public static String secondToDate(Long second, String patten) {
        if (second == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(second * 1000L);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(patten);
        String dateString = format.format(date);
        return dateString;
    }

    public static String millisToDate(String millisStr, String patten) {
        if (StringUtils.isEmpty((CharSequence)millisStr)) {
            return "";
        }
        try {
            Long millis = Long.valueOf(millisStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            Date date = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat(patten);
            String dateString = format.format(date);
            return dateString;
        }
        catch (Exception e) {
            logger.error("millisToDate\u9519\u8bef", (Throwable)e);
            return "";
        }
    }

    public static String beforeHourToNowDate(Integer hours) {
        if (null == hours || 0 == hours) {
            hours = 1;
        }
        Calendar c = Calendar.getInstance();
        long diffTimes = 3600000L;
        long nowTimes = c.getTimeInMillis();
        c.setTimeInMillis(nowTimes - (long)hours.intValue() * diffTimes);
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
        return sdf.format(c.getTime());
    }

    public static String beforeMinutesToNowDate(Integer minutes) {
        if (null == minutes || 0 == minutes) {
            minutes = 1;
        }
        Calendar c = Calendar.getInstance();
        long diffTimes = 60000L;
        long nowTimes = c.getTimeInMillis();
        c.setTimeInMillis(nowTimes - (long)minutes.intValue() * diffTimes);
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
        return sdf.format(c.getTime());
    }

    public static String getCurrentDateTimeNoChar() {
        String currentTime = DateUtil.getCurrentDateTime();
        if (!StringUtils.isEmpty((CharSequence)currentTime)) {
            currentTime = currentTime.replace("-", "").replace(" ", "").replace(":", "");
        }
        return currentTime;
    }

    public static void checkPwdExp(String expDate) {
        try {
            if (StringUtils.isEmpty((CharSequence)expDate)) {
                StaticKeys.PASSWD_EXP_DATE = false;
                return;
            }
            expDate = expDate.trim();
            long expDateLong = Long.valueOf(expDate.replace("-", ""));
            Long nowDate = Long.valueOf(DateUtil.getCurrentDate().replace("-", ""));
            StaticKeys.PASSWD_EXP_DATE = nowDate >= expDateLong;
        }
        catch (Exception e) {
            logger.error("\u68c0\u67e5\u5bc6\u7801\u662f\u5426\u8fc7\u671f\u9519\u8bef", (Throwable)e);
        }
    }
}

