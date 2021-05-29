package com.example.lock.reentrantLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/2
 * @Description:
 */
public class ProdConsumer_TraditionalDemo{

    public static void main( String[] args ){
        ShareData shareData = new ShareData();

        new Thread( ()->{
            for( int i = 0; i < 5; i++ ){
                try{
                    shareData.increment();
                }
                catch( Exception e ){
                    e.printStackTrace();
                }
            }
        }, "A" ).start();

        new Thread( ()->{
            for( int i = 0; i < 5; i++ ){
                try{
                    shareData.decrement();
                }
                catch( Exception e ){
                    e.printStackTrace();
                }
            }
        }, "B" ).start();
    }
}

class ShareData{

    private int num = 0;
    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public void increment(){
        try{
            lock.lock();
            while( num != 0 ){
                condition.await();
            }
            num++;
            System.out.println( Thread.currentThread().getName() + " === " + num );
            condition.signal();
        }
        catch( InterruptedException e ){
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }
    }

    public void decrement(){
        try{
            lock.lock();
            while( num != 1 ){
                condition.await();
            }
            num--;
            System.out.println( Thread.currentThread().getName() + " === " + num );
            condition.signal();
        }
        catch( InterruptedException e ){
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }
    }
}
