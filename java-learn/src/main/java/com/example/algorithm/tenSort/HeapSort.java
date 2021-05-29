package com.example.algorithm.tenSort;

import com.example.util.SortUtils;

import java.util.Arrays;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/10
 * @Description:
 */
public class HeapSort{

    public static void main( String[] args ){
        int[] arr = new int[]{ 5, 7, 4, 3, 7, 3, 9, 1, 0 };
        System.out.println( Arrays.toString( arr ) );
        heapSort( arr );
        System.out.println( Arrays.toString( arr ) );
    }

    //声明全局变量，用于记录数组array的长度；
    private static int len;

    /**
     * 堆排序算法
     *
     * @param array
     * @return
     */
    public static int[] heapSort( int[] array ){
        len = array.length;
        if( len < 1 ) return array;
        //1.构建一个最大堆
        buildMaxHeap( array );
        //2.循环将堆首位（最大值）与末位交换，然后在重新调整最大堆
        while( len > 0 ){
            SortUtils.swap( array, 0, len - 1 );
            len--;
            adjustHeap( array, 0 );
        }
        return array;
    }

    /**
     * 建立最大堆
     *
     * @param array
     */
    private static void buildMaxHeap( int[] array ){
        //从最后一个非叶子节点开始向上构造最大堆
        for( int i = ( len / 2 - 1 ); i >= 0; i-- ){
            adjustHeap( array, i );
        }
    }

    /**
     * 调整使之成为最大堆
     *
     * @param array
     * @param i
     */
    private static void adjustHeap( int[] array, int i ){
        int maxIndex = i;
        //如果有左子树，且左子树大于父节点，则将最大指针指向左子树
        if( i * 2 < len && array[ i * 2 ] > array[ maxIndex ] ) maxIndex = i * 2;
        //如果有右子树，且右子树大于父节点，则将最大指针指向右子树
        if( i * 2 + 1 < len && array[ i * 2 + 1 ] > array[ maxIndex ] ) maxIndex = i * 2 + 1;
        //如果父节点不是最大值，则将父节点与最大值交换，并且递归调整与父节点交换的位置。
        if( maxIndex != i ){
            SortUtils.swap( array, maxIndex, i );
            adjustHeap( array, maxIndex );
        }
    }
}
