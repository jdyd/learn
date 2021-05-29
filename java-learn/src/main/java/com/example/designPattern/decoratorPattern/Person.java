package com.example.designPattern.decoratorPattern;

/**
 * @Auther: cuijian05
 * @Date: 2020/10/12
 * @Description: 被装饰者
 */
public class Person implements Human{

    @Override
    public void wearClothes(){
        System.out.println( "穿什么呢。。" );
    }

    @Override
    public void walkToWhere(){
        System.out.println( "去哪里呢。。" );
    }

}
