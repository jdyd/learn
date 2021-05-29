package com.example.algorithm;

import org.junit.Test;

/**
 * @Author: cuijian05
 * @Date: 2020/8/22
 * @Description: 【面试】给定一个字符串和左侧长度leftSize，将str左侧leftSize部分和右侧部分做整体交换，要求额外空间复杂度O(1).
 *                  将字符串abcdefgh的前五个字符和后三个字符交换位置，变为fghabcde
 */
public class ReverseStr{

    //----------------------------------------方法一-----------------------------------------

    /**
     * 方法一：左侧逆序，右侧逆序，然后整体逆序
     * 1、左侧逆序：edcba
     * 2、右侧逆序：hgf
     * 此时字符串变为：edcbahgf
     * 3、整体逆序：fghabcde
     */
    @Test
    public void test1(){
        String str = "abcdefgh";
        char[] chars = str.toCharArray();
        reverseStr1( chars, 5 );
        System.out.println( chars );
    }

    private void reverseStr1( char[] chars, int leftSize ){
        reverse1( chars, 0, leftSize - 1 );
        reverse1( chars, leftSize, chars.length - 1 );
        reverse1( chars, 0, chars.length - 1 );
    }

    private void reverse1( char[] chars, int leftIndex, int rightIndex ){
        while( leftIndex < rightIndex ){
            char temp = chars[ leftIndex ];
            chars[ leftIndex++ ] = chars[ rightIndex ];
            chars[ rightIndex-- ] = temp;
        }
    }

    //----------------------------------------方法二-----------------------------------------

    /**
     * 方法二
     */
    @Test
    public void test2(){
        String str = "abcdefgh";
        char[] chars = str.toCharArray();
        reverseStr2( chars, 5 );
        System.out.println( chars );
    }

    private void reverseStr2( char[] chars, int leftSize ){
        int len = chars.length;

        int leftIndex = 0;
        int rightIndex = chars.length - 1;
        int leftPartSize = leftSize;
        int rightPartSize = len - leftPartSize;
        int same = Math.min( leftPartSize, rightPartSize );
        int diff = leftPartSize - rightPartSize;
        exchange( chars, leftIndex, rightIndex, same );
        while( diff != 0 ){
            if( diff > 0 ){ //左侧多
                leftIndex += same;
                leftPartSize = diff;
            }
            else{ //右侧多
                rightIndex -= same;
                rightPartSize = -diff;
            }
            same = Math.min( leftPartSize, rightPartSize );
            diff = leftPartSize - rightPartSize;
            exchange( chars, leftIndex, rightIndex, same );
        }
    }

    /**
     * str[leftIndex....] 输出size大小，和str[....rightIndex]输出size大小，交换
     * @param chars
     * @param leftIndex
     * @param rightIndex
     * @param size
     */
    private void exchange( char[] chars, int leftIndex, int rightIndex, int size ){
        while( size > 0 ){
            char temp = chars[ leftIndex ];
            chars[ leftIndex++ ] = chars[ rightIndex - size + 1 ];
            chars[ rightIndex - size + 1 ] = temp;
            size--;
        }
    }

}
