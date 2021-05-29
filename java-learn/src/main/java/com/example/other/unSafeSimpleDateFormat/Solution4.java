package com.example.other.unSafeSimpleDateFormat;

import com.example.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/28
 * @Description: 抛弃JDK，使用其他类库中的时间格式化类 DateUtils
 */
public class Solution4{

    /**
     * 说明：向返回新对象日期添加若干天，原始日期不变；
     * 参数：date-日期，可能为空；amount-添加的量可能为复值；
     * 返回值：返回一个新日期，原始日期不变；
     * 抛出异常：IllegalArgumentException，如果日期为null；
     */
    @Test
    public void addDaysTest(){
        Date date = new Date();
        System.out.println( DateUtil.format( date ) );
        date = DateUtils.addDays( date, 1 );
        //        date = DateUtils.addHours( date, 1 );
        //        date = DateUtils.addMinutes( date, 1 );
        //        date = DateUtils.addSeconds( date, 1 );
        //        date = DateUtils.addMilliseconds( date, 1 );
        //        date = DateUtils.addYears( date, 1 );
        //        date = DateUtils.addMonths( date, 1 );
        //        date = DateUtils.addWeeks( date, 1 );
        //        date = DateUtils.setYears( date, 2015 );
        System.out.println( DateUtil.format( date ) );
    }

    /*-----------------------------设置日期中年、月、日、时、分、秒、毫秒值为指定的数字值------------------------------*/
    @Test
    public void setYearsTest(){
        Date date = new Date();
        System.out.println( DateUtil.format( date ) );
        date = DateUtils.setYears( date, 2015 );
        //        date = DateUtils.setMonths( date, 11 );
        //        date = DateUtils.setDays( date, 11 );
        //        date = DateUtils.setHours( date, 16 );
        //        date = DateUtils.setMinutes( date, 16 );
        //        date = DateUtils.setSeconds( date, 10 );
        //        date = DateUtils.setMilliseconds( date, 10 );
        System.out.println( DateUtil.format( date ) );
    }

    /*-----------------------------检查日期是否相同------------------------------*/

    /**
     * 说明：检查两个日期对象是否同一天忽略时间（28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true. 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.）；
     * 参数：date1-第一个参数，不能改变，非空；date2-第二个参数，不能改变，非空；
     * 返回值：true,如果两个日期在同一天；
     * 抛出异常：IllegalArgumentException如果有一个日期为null；
     */
    @Test
    public void isSameDay1(){
        Date date1 = new Date();
        Date date2 = new Date();
        System.out.println( DateUtils.isSameDay( date1, date2 ) );
    }

    @Test
    public void isSameDay2(){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        System.out.println( DateUtils.isSameDay( cal1, cal2 ) );
    }

    /**
     * 说明：检查两个日期对象是否代表同一时刻，此方法比较两个日期精确到毫秒数；
     * 参数：date1-第一个参数，不能改变，非空；date2-第二个参数，不能改变，非空；
     * 返回值：true,如果两个日期代表相同的毫秒时刻；
     * 抛出异常：IllegalArgumentException如果有一个日期为null；
     */
    @Test
    public void isSameInstant1(){
        Date date1 = new Date();
        date1 = DateUtils.addMinutes( date1, 23 );
        Date date2 = new Date();
        System.out.println( DateUtils.isSameInstant( date1, date2 ) );
    }

    /**
     * 说明：检查两个日期对象是否代表同一时刻，此方法比较两个日期精确到毫秒数；
     * 参数：date1-第一个参数，不能改变，非空；date2-第二个参数，不能改变，非空；
     * 返回值：true,如果两个日期代表相同的毫秒时刻；
     * 抛出异常：IllegalArgumentException如果有一个日期为null；
     */
    @Test
    public void isSameInstant2(){
        Calendar cal1 = Calendar.getInstance();
        Date date3 = DateUtils.addHours( cal1.getTime(), 12 );
        cal1.setTime( date3 );
        Calendar cal2 = Calendar.getInstance();
        System.out.println( DateUtils.isSameInstant( cal1, cal2 ) );
    }

}