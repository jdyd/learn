package com.example.proxy;

import com.example.proxy.function.UserDao;
import com.example.proxy.function.impl.UserDaoImpl;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/3
 * @Description: 由程序员创建或工具生成代理类的源码，再编译代理类。所谓静态也就是在程序运行前就已经存在代理类的字节码文件，代理类和委托类的关系在运行前就确定了。
 * <p>
 * 缺点：每个需要代理的对象都需要自己重复编写代理，很不舒服，
 * 优点：但是可以面相实际对象或者是接口的方式实现代理
 */
public class StaticProxy{

    private UserDao userDao;

    public StaticProxy( UserDao userDao ){
        this.userDao = userDao;
    }

    public void save(){
        System.out.println( "before" );
        userDao.save();
        System.out.println( "end" );
    }

    public static void main( String[] args ){
        StaticProxy staticProxy = new StaticProxy( new UserDaoImpl() );
        staticProxy.save();
    }
}
