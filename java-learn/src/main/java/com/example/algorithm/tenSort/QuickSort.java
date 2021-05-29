package com.example.algorithm.tenSort;

import com.example.util.SortUtils;
import org.junit.Test;

/**
 * @Auther: cuijian05
 * @Date: 2020/8/28
 * @Description:快速排序 1）描述
 * 在数据序列中选择一个值作为比较的基准值，每趟从数据序列的两端开始交替进行，将小于基准值的元素交换到序列前端，大于基准值的元素交换到序列后端，介于两者之间的位置则称为基准值的最终位置。同时序列被划分成两个子序列，再用同样的方法对了两个子序列进行排序，知道子序列长度为1，则完成排序。
 * 2）复杂度分析
 * <p>
 * 时间复杂度：最好情况-每趟排序将序列分成长度相近的两个子序列。   o(nlog2n) ；最坏情况：每趟排序将序列分成长度差异很大的两个子序列   o(n*n)
 * 空间复杂度：最好情况 o(log2n)  ；最坏情况 o(n)
 * <p>
 * 3） 稳定性
 * 不稳定
 */
public class QuickSort{

    @Test
    public void test(){
        int[] arr = new int[]{ 5, 1, 8, 2, 3, 4, 6, 7 };
        quickSort( arr, 0, arr.length - 1 );
        SortUtils.println( arr );
    }

    private void quickSort( int[] arr, int low, int high ){
        if( low < high ){
            int partition = partition( arr, low, high );
            quickSort( arr, 0, partition - 1 );
            quickSort( arr, partition + 1, high );
        }
    }

    private int partition( int[] arr, int low, int high ){
        int pivotkey = arr[ low ];
        while( low < high ){
            while( low < high && arr[ high ] >= pivotkey ){
                high--;
            }
            arr[ low ] = arr[ high ];
            while( low < high && arr[ low ] <= pivotkey ){
                low++;
            }
            arr[ high ] = arr[ low ];
        }
        arr[ low ] = pivotkey;
        return low;
    }

}
