package com.example.lock.reentrantLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/2
 * @Description: 题目：多线程之间按顺序调用，实现A->B->C三个线程启动，要求如下：
 * A打印5次，B打印10次，C打印15次
 * 紧接着
 * A打印5次，B打印10次，C打印15次
 * ……
 * 重复10轮
 */
public class SyncAndReentrantLockDemo{

    public static void main( String[] args ){
        ShareResource resource = new ShareResource();

        new Thread( ()->{
            for( int i = 1; i <= 10; i++ ){
                resource.print5();
            }
        }, "A" ).start();

        new Thread( ()->{
            for( int i = 1; i <= 10; i++ ){
                resource.print10();
            }
        }, "B" ).start();

        new Thread( ()->{
            for( int i = 1; i <= 10; i++ ){
                resource.print15();
            }
        }, "C" ).start();
    }
}

class ShareResource{
    private int num = 1;
    private static Lock lock = new ReentrantLock();
    private static Condition condition1 = lock.newCondition();
    private static Condition condition2 = lock.newCondition();
    private static Condition condition3 = lock.newCondition();

    public void print5(){
        try{
            lock.lock();
            while( num != 1 ){
                condition1.await();
            }
            for( int i = 0; i < 5; i++ ){
                System.out.println( Thread.currentThread().getName() + " === " + i );
            }
            num = 2;
            condition2.signal();
        }
        catch( InterruptedException e ){
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }
    }

    public void print10(){
        try{
            lock.lock();
            while( num != 2 ){
                condition2.await();
            }
            for( int i = 0; i < 10; i++ ){
                System.out.println( Thread.currentThread().getName() + " === " + i );
            }
            num = 3;
            condition3.signal();
        }
        catch( InterruptedException e ){
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }
    }

    public void print15(){
        try{
            lock.lock();
            while( num != 3 ){
                condition3.await();
            }
            for( int i = 0; i < 15; i++ ){
                System.out.println( Thread.currentThread().getName() + " === " + i );
            }
            num = 1;
            condition1.signal();
        }
        catch( InterruptedException e ){
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }
    }
}