package com.example.proxy;

import com.example.proxy.function.UserDao;
import com.example.proxy.function.impl.UserDaoImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/3
 * @Description: JDK代理、接口代理。
 * <p>
 * 动态代理的对象，是利用JDK的API，动态的在内存中构建代理对象（是根据被代理的接口来动态生成代理类的class文件，并加载运行的过程），这就叫动态代理
 * <p>
 * 缺点：必须是面向接口，目标业务类必须实现接口
 * 优点：不用关心代理类，只需要在运行阶段才指定代理哪一个对象
 */
public class JDKProxyDemo{

    public static void main( String[] args ){
        UserDao userDao = new UserDaoImpl();
        UserDao proxy = ( UserDao )Proxy.newProxyInstance( userDao.getClass().getClassLoader(), userDao.getClass().getInterfaces(),
                                                           new InvocationHandler(){
                                                               @Override
                                                               public Object invoke( Object o, Method method, Object[] objects ) throws Throwable{
                                                                   System.out.println( "before" );
                                                                   method.invoke( userDao, objects );
                                                                   System.out.println( "end" );
                                                                   return null;
                                                               }
                                                           } );

        //通过代理类调用目标类方法
        proxy.save();
    }
}


