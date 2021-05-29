package com.example.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: cuijian05
 * @Date: 2020/9/2
 * @Description:
 */
public class BioService{

    public static void main( String[] args ) throws Exception{

        //1、创建线程池
        //2、如果有客户端连接，就创建一个线程与之通信

        Executor newCachedThreadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket( 6666 );

        System.out.println( " === 服务器启动了！！！" );

        while( true ){
            Socket accept = serverSocket.accept();
            System.out.println( " === 客户端 " + Thread.currentThread().getName() + " 来了！！！" );
            newCachedThreadPool.execute( ()->{
                handler( accept );
            } );
        }
    }

    public static void handler( Socket socket ){
        byte[] bytes = new byte[ 1024 ];
        InputStream inputStream = null;
        try{
            inputStream = socket.getInputStream();
            while( true ){
                int read = inputStream.read( bytes );
                if( read != -1 ){
                    System.out.println( new String( bytes, 0, read ) );//输出客户端信息
                }
                else{
                    break;
                }
            }
        }
        catch( IOException e ){
            e.printStackTrace();
        }
        finally{
            try{
                inputStream.close();
            }
            catch( IOException e ){
                e.printStackTrace();
            }
        }
    }
}
