package com.example.other.unSafeSimpleDateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/28
 * @Description: 同步SimpleDateFormat对象
 * 缺点：当线程较多时，当一个线程调用该方法时，其他想要调用此方法的线程就要block，多线程并发量大的时候会对性能有一定的影响。
 */
public class Solution2{

    private static SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

    public static String formatDate( Date date ) throws Exception{
        synchronized( sdf ){
            return sdf.format( date );
        }
    }

    public static Date parse( String strDate ) throws Exception{
        synchronized( sdf ){
            return sdf.parse( strDate );
        }
    }
}
