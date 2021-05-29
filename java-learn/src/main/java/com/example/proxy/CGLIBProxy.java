package com.example.proxy;

import com.example.proxy.function.UserDao;
import com.example.proxy.function.impl.UserDaoImpl;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/3
 * @Description: 利用asm开源包，对代理对象类的class文件加载进来，通过修改其字节码生成子类来处理。
 * CGLIB动态代理和jdk代理一样，使用反射完成代理，不同的是他可以直接代理类（jdk动态代理不行，他必须目标业务类必须实现接口），
 * CGLIB动态代理底层使用字节码技术，CGLIB动态代理不能对 final类进行继承。（CGLIB动态代理需要导入jar包）
 */
public class CGLIBProxy{

    public static void main( String[] args ){
        UserDao userDao = new UserDaoImpl();
        Enhancer enhancer = new Enhancer();
        //继承被代理类
        enhancer.setSuperclass( userDao.getClass() );
        enhancer.setCallback( new MethodInterceptor(){  //设置回调
            //CGLib 定义的 intercept() 接口方法, 拦截所有目标类方法的调用.  其中 o 代表目标类的实例,  method 为目标类方法的反射;  objects为方法的动态入参,  proxy为代理类实例.
            @Override
            public Object intercept( Object o, Method method, Object[] objects, MethodProxy methodProxy ) throws Throwable{
                System.out.println( "before" );
                methodProxy.invokeSuper( o, objects );
                System.out.println( "end" );
                return null;
            }
        } );
        //创建代理对象
        UserDao proxy = ( UserDao )enhancer.create();
        proxy.save();
    }
}

