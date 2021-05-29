package com.example.other.unSafeSimpleDateFormat;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/28
 * @Description: SimpleDateFormat的线程安全问题
 * 导致现象：
 * 1、线程报java.lang.NumberFormatException: multiple points错误，直接挂死
 * 2、输出的时间是有错误的
 */
public class UnSafeSimpleDateFormat{

    @Test
    public void testUnSafe() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        String[] dateString = { "2017-11-05", "2017-11-06", "2017-11-07", "2017-11-08", "2017-11-09", "2017-11-10", "2017-11-11", "2017-11-12", "2017-11-13", "2017-11-14" };
        Thread[] threads = new Thread[ 10 ];
        for( int i = 0; i < threads.length; i++ ){
            //多个线程使用的是同一个SimpleDateFormat对象
            threads[ i ] = new MyThread( sdf, dateString[ i ] );
        }
        for( int i = 0; i < threads.length; i++ ){
            threads[ i ].start();
        }

        //等待子线程执行完
        Thread.sleep( 3000L );
    }

}

class MyThread extends Thread{
    private SimpleDateFormat sdf;
    private String dateString;

    public MyThread( SimpleDateFormat sdf, String dateString ){
        this.sdf = sdf;
        this.dateString = dateString;
    }

    @Override
    public void run(){
        try{
            Date date = sdf.parse( dateString );
            String dateStr = sdf.format( date );
            if( !dateStr.equals( dateString ) ){
                System.out.println( "ThreadName=" + this.getName() + "报错了，日期字符串：" + dateString + ",转换成的日期字符串：" + dateStr );
            }
            else{
                System.out.println( "ThreadName=" + this.getName() + "成功，日期字符串：" + dateString );
            }
        }
        catch( Exception e ){
            e.printStackTrace();
        }
    }
}