package com.example.designPattern.decoratorPattern;

/**
 * @Auther: cuijian05
 * @Date: 2020/10/12
 * @Description: 装饰器2
 */
public class Decorator_two extends Decorator{

    public Decorator_two( Human human ){
        super( human );
    }

    public void goClothespress(){
        System.out.println( "去衣柜找找看。。" );
    }

    public void findPlaceOnMap(){
        System.out.println( "在Map上找找。。" );
    }

    @Override
    public void wearClothes(){
        super.wearClothes();
        goClothespress();
    }

    @Override
    public void walkToWhere(){
        super.walkToWhere();
        findPlaceOnMap();
    }
}
