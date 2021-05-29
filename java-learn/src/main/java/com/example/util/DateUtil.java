package com.example.util;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/28
 * @Description: 时间工具类
 */
public class DateUtil{
    private static final String date_format = "yyyy-MM-dd HH:mm:ss:SSS";

    @Test
    public void getOneYearAfterTest(){
        System.out.println( "当前时间：  " + format( new Date() ) );
        System.out.println( "一年后时间： " + format( getXXYearAfter( 1 ) ) );
    }

    /**
     * 获取一年后时间date
     *
     * @return
     */
    public static Date getXXYearAfter( int year ){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        //calendar.add(calendar.DAY_OF_YEAR, 1);//增加一天,负数为减少一天
        //calendar.add(calendar.DAY_OF_MONTH, 1);//增加一天
        //calendar.add(calendar.DATE,1);//增加一天
        //calendar.add(calendar.WEEK_OF_MONTH, 1);//增加一个礼拜
        //calendar.add(calendar.WEEK_OF_YEAR,1);//增加一个礼拜
        calendar.add( calendar.YEAR, year ); //把日期往后增加一年.整数往后推,负数往前移动
        return calendar.getTime();
    }

    private static ThreadLocal< DateFormat > threadLocal2 = new ThreadLocal< DateFormat >();

    public static DateFormat getDateFormat(){
        DateFormat df = threadLocal2.get();
        if( df == null ){
            df = new SimpleDateFormat( date_format );
            threadLocal2.set( df );
        }
        return df;
    }

    public static String format( Date date ){
        return getDateFormat().format( date );
    }

    public static Date parse( String strDate ) throws Exception{
        return getDateFormat().parse( strDate );
    }
}
