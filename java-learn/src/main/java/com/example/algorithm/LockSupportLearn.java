package com.example.algorithm;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: cuijian05
 * @Date: 2020/8/21
 * @Description: 两个线程交替打印1A2B3C....26Z
 */
public class LockSupportLearn{

    private static Thread t1 = null, t2 = null;

    //----------------------推荐1：lockSupport------------------------

    @Test
    public void test1() throws InterruptedException{

        char[] a = "1234567".toCharArray();
        char[] b = "abcdefg".toCharArray();

        t1 = new Thread( ()->{
            for( char c : a ){
                System.out.print( c );
                LockSupport.unpark( t2 );
                LockSupport.park();
            }
        }, "t1" );
        t2 = new Thread( ()->{
            for( char c : b ){
                LockSupport.park();
                System.out.print( c );
                LockSupport.unpark( t1 );
            }
        }, "t2" );

        t1.start();
        t2.start();

        Thread.sleep( 1000L );
    }

    //----------------------------推荐2：condition-----------------------------

    @Test
    public void test4() throws InterruptedException{
        Lock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        char[] a = "1234567".toCharArray();
        char[] b = "abcdefg".toCharArray();

        t1 = new Thread( ()->{
            try{
                lock.lock();
                for( char c : a ){
                    System.out.print( c );
                    condition1.signal();
                    condition2.await();
                }
                condition1.signal();
            }
            catch( Exception e ){
                e.printStackTrace();
            }
            finally{
                lock.unlock();
            }
        }, "t1" );
        t2 = new Thread( ()->{
            try{
                lock.lock();
                for( char c : b ){
                    System.out.print( c );
                    condition2.signal();
                    condition1.await();
                }
                condition2.signal();
            }
            catch( Exception e ){
                e.printStackTrace();
            }
            finally{
                lock.unlock();
            }
        }, "t2" );

        t1.start();
        t2.start();
        Thread.sleep( 1000L );
    }

    //----------------------------推荐3：synchronized--------------------------------

    @Test
    public void test3() throws Exception{
        Object o = new Object();

        char[] a = "1234567".toCharArray();
        char[] b = "abcdefg".toCharArray();

        t1 = new Thread( ()->{
            synchronized( o ){
                for( char c : a ){
                    System.out.print( c );
                    try{
                        o.notify();
                        o.wait();
                    }
                    catch( InterruptedException e ){
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        }, "t1" );
        t2 = new Thread( ()->{
            synchronized( o ){
                for( char c : b ){
                    System.out.print( c );
                    try{
                        o.notify();
                        o.wait();
                    }
                    catch( InterruptedException e ){
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        }, "t2" );

        t1.start();
        t2.start();
        Thread.sleep( 1000L );
    }

    //----------------------BlockingQueue------------------------

    BlockingQueue< String > b1 = new ArrayBlockingQueue<>( 1 );
    BlockingQueue< String > b2 = new ArrayBlockingQueue<>( 1 );

    @Test
    public void test2() throws InterruptedException{
        char[] a = "1234567".toCharArray();
        char[] b = "abcdefg".toCharArray();

        t1 = new Thread( ()->{
            for( char c : a ){
                System.out.print( c );
                try{
                    b1.put( "ok" );
                    b2.take();
                }
                catch( InterruptedException e ){
                    e.printStackTrace();
                }
            }
        }, "t1" );
        t2 = new Thread( ()->{
            for( char c : b ){
                try{
                    b2.put( "ok" );
                    System.out.print( c );
                    b1.take();
                }
                catch( InterruptedException e ){
                    e.printStackTrace();
                }
            }
        }, "t2" );

        t1.start();
        t2.start();
        Thread.sleep( 1000L );
    }

}
