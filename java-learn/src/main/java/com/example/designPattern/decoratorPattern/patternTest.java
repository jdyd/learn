package com.example.designPattern.decoratorPattern;

import org.junit.Test;

/**
 * @Auther: cuijian05
 * @Date: 2020/10/12
 * @Description: 装饰器设计模式
 */
public class patternTest{

    @Test
    public void test1(){
        Human person = new Person();
        Decorator decorator = new Decorator_three( new Decorator_two( new Decorator_one( person ) ) );
        decorator.wearClothes();
        decorator.walkToWhere();
    }
}
