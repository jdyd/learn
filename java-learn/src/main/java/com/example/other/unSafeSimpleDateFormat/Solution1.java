package com.example.other.unSafeSimpleDateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/28
 * @Description: 每次使用都创建一个新的实例simpleDateFormat
 * 缺点：一个实例需要耗费很大的代价，大量的对象就这样被创建出来，占用大量的内存和 jvm空间。
 */
public class Solution1{

    public static String formatDate( Date date ) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        return sdf.format( date );
    }

    public static Date parse( String strDate ) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        return sdf.parse( strDate );
    }
}
