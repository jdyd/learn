package com.example.util;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.Random;

/**
 * @Auther: cuijian05
 * @Date: 2020/8/28
 * @Description:
 */
public class SortUtils{

    public static void println( int[] arr ){
        if( ArrayUtils.isNotEmpty( arr ) ){
            Arrays.stream( arr ).forEach( System.out::println );
        }
    }

    public static void swap( int[] arr, int i, int j ){
        int temp = arr[ i ];
        arr[ i ] = arr[ j ];
        arr[ j ] = temp;
    }

    public static int[] getRandomInts( int size ){
        int[] arr = new int[ size ];
        for( int i = 0; i < size; i++ ){
            arr[ i ] = new Random().nextInt( size );
        }
        return arr;
    }

    public static int[] getOrderedInts( int size ){
        int[] arr = new int[ size ];
        for( int i = 0; i < size; i++ ){
            arr[ i ] = i;
        }
        return arr;
    }
}
