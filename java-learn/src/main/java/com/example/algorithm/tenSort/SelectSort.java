package com.example.algorithm.tenSort;

import com.example.util.SortUtils;
import org.junit.Test;

/**
 * @Auther: cuijian05
 * @Date: 2020/8/28
 * @Description: 直接选择排序
 * 1）描述
 * <p>
 * 第一趟从n个元素的数据中选出关键字最小或最大的元素放到最前或最后位置，下一趟再从n-1个元素……。直接选择排序算法可用顺序表和单链表实现。
 * <p>
 * 2）复杂度分析
 * <p>
 * 时间复杂度：o(n*n)
 * 空间复杂度：o(1)
 * 3）稳定性
 * <p>
 * 不稳定
 */
public class SelectSort{

    @Test
    public void test(){
        int[] arr = new int[]{ 5, 1, 8, 2, 3, 4, 6, 7 };
        selectSort( arr );
        SortUtils.println( arr );
    }

    private void selectSort( int[] arr ){
        int size = arr.length;
        for( int i = 0; i < size - 1; i++ ){
            int min = i;
            for( int j = i + 1; j < size && arr[ j ] < arr[ min ]; j++ ){
                min = j;
            }
            if( min != i ){
                SortUtils.swap( arr, min, i );
            }
        }
    }
}
