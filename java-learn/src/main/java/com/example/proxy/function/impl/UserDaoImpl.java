package com.example.proxy.function.impl;

import com.example.proxy.function.UserDao;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/3
 * @Description:
 */

public class UserDaoImpl implements UserDao{

    @Override
    public void save(){
        System.out.println( "userDaoImpl save()" );
    }
}