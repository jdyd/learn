package com.example.designPattern.decoratorPattern;

/**
 * @Auther: cuijian05
 * @Date: 2020/10/12
 * @Description: 装饰器1
 */
public class Decorator_one extends Decorator{

    public Decorator_one( Human human ){
        super( human );
    }

    public void goHome(){
        System.out.println( "进房子。。。" );
    }

    public void findMap(){
        System.out.println( "书房找map。。。" );
    }

    @Override
    public void wearClothes(){
        super.wearClothes();
        goHome();
    }

    @Override
    public void walkToWhere(){
        super.walkToWhere();
        findMap();
    }
}
