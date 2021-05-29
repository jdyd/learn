package com.example.other.unSafeSimpleDateFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/28
 * @Description: 使用ThreadLocal
 * 使用ThreadLocal, 也是将共享变量变为独享，线程独享肯定能比方法独享在并发环境中能减少不少创建对象的开销。如果对性能要求比较高的情况下，一般推荐使用这种方法
 */
public class Solution3{

    /*----------------------------------------写法一---------------------------------------------*/
    private static ThreadLocal< DateFormat > threadLocal1 = new ThreadLocal< DateFormat >(){
        @Override
        protected DateFormat initialValue(){
            return new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        }
    };

    public static Date parse1( String dateStr ) throws Exception{
        return threadLocal1.get().parse( dateStr );
    }

    public static String format1( Date date ){
        return threadLocal1.get().format( date );
    }

    /*----------------------------------------写法二---------------------------------------------*/
    private static final String date_format = "yyyy-MM-dd HH:mm:ss";
    private static ThreadLocal< DateFormat > threadLocal2 = new ThreadLocal< DateFormat >();

    public static DateFormat getDateFormat(){
        DateFormat df = threadLocal2.get();
        if( df == null ){
            df = new SimpleDateFormat( date_format );
            threadLocal2.set( df );
        }
        return df;
    }

    public static String format2( Date date ) throws Exception{
        return getDateFormat().format( date );
    }

    public static Date parse2( String strDate ) throws Exception{
        return getDateFormat().parse( strDate );
    }
}