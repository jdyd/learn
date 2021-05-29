package com.example.algorithm.tenSort;

import com.example.util.SortUtils;
import org.junit.Test;

/**
 * @Auther: cuijian05
 * @Date: 2020/8/28
 * @Description: 直接插入排序
 * 1）描述
 * <p>
 * 直接插入排序就是第 i 趟把第 i 个元素放到前面已经排好序的序列中去
 * 重复上述操作。 n 个元素共需 n-1 趟扫描，每趟将一个元素插入到它前面的子序列中
 * <p>
 * 2）复杂度分析
 * <p>
 * 时间复杂度：最好情况 o(n) (当数据序列已排序)；最坏情况 o(n * n)(数据序列反序); 随机（n * n）
 * 空间复杂度: o(1)
 * <p>
 * <p>
 * 稳定性：直接插入排序是稳定的。
 */
public class InsertSort{

    @Test
    public void test(){
        int[] arr = new int[]{ 5, 1, 8, 2, 3, 4, 6, 7 };
        insertSort( arr );
        SortUtils.println( arr );
    }

    private void insertSort( int[] arr ){
        int size = arr.length;
        for( int i = 1; i < size; i++ ){
            int temp = arr[ i ];
            int j;
            for( j = i - 1; j >= 0 && arr[ j ] > temp; j-- ){
                arr[ j + 1 ] = arr[ j ];
            }
            arr[ j + 1 ] = temp;
        }
    }
}
