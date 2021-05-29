package com.example.algorithm.tenSort;

import java.util.Arrays;

/**
 * @Auther: cuijian05
 * @Date: 2020/9/10
 * @Description:
 * merge(归并)函数
 * 1.把一个数组想象成两个等份（相差不过1）的数组   左边的低位（i = low）到高位（middle）
 * 右边的低位（j = middle + 1）到高位（high）
 * 2.设置index指向装入下标
 * 3.设置备份数组，数组长度由递归过来的数组的长度决定，
 * 因为high=arr.length-1传过来的-1，在保证temp数组与传入的数组长度一样，所以要+1
 * 4.在两边数组的低位都还没超过(相应的)高位的情况下经行比较，
 * 把小（或大）的放在temp左（或右）边
 * index（从0到arr.length/2）指向放入位置的下标
 * 5.把还没超过(相应的)高位的数组，把剩下的数依次放入temp数组直到遍历到(相应的)高位为止
 * 6.剩下一个步骤要结合mergeSort函数来理解
 *
 * mergeSort（递归调用归并函数）函数
 * 1.在低位低于高位的情况下
 * 2.设置middle
 * 3.递归自己（把子拆成等分[相差不过1]的两半，递归调用）
 * 4.最后调用归并函数-------------看到这里可以理解为
 * 一个数组以每两个元素分成一组，每一组都经行大小比较
 * 一轮结束后每四个元素分为一组....
 * 所以merge函数的最后一步这样理解比较好
 * arr下标 		0    和    1      2    和    3     4和5
 * value值   		4     3      2     1
 *             low   high | low   high
 * temp的下标          0  和    1   |  0  和    1
 * temp数组的元素    3     4   |  1     2
 *
 */
public class MergeSort{

    public static void main( String[] args ){
        int[] arr = new int[]{ 5, 7, 4, 3, 7, 3, 9, 1, 0 };
        System.out.println( Arrays.toString( arr ) );
        mergeSort( arr, 0, arr.length - 1 );
        System.out.println( Arrays.toString( arr ) );
        //先看merge函数
    }

    public static void mergeSort( int[] arr, int low, int high ){
        int middle = ( high + low ) / 2;
        if( low < high ){
            mergeSort( arr, low, middle );
            mergeSort( arr, middle + 1, high );
            //把一个数组分成两份  然后进行归并递归调用 直到low<high结束调用
            merge( arr, low, middle, high );
        }
    }

    public static void merge( int[] arr, int low, int middle, int high ){
        //low				middle					high
        // 5,  	 7, 4,	 	  3, 	 7, 3, 9, 1, 	 0
        int i = low;
        int j = middle + 1;
        int index = 0;
        int[] temp = new int[ high - low + 1 ];
        //这个临时数组的长度为high - low + 1   high传进来时是length-1
        //		while(i < j && j < arr.length)
        while( i <= middle && j <= high ){
            //把一个数组分成两个数组进行比较
            if( arr[ i ] <= arr[ j ] ){
                temp[ index ] = arr[ i ];
                i++;
                index++;
            }
            else{
                temp[ index ] = arr[ j ];
                j++;
                index++;
            }
            //把分成的两数组中最小的数依次放入temp
        }
        //两数组中较大的数赋值给剩下一半数组长度的temp
        while( i <= middle ){
            temp[ index ] = arr[ i ];
            i++;
            index++;
        }
        while( j <= high ){
            temp[ index ] = arr[ j ];
            j++;
            index++;
        }
        //然后把temp归并好的数传给arr
        //	for(int k = 0; k <= high; k++) {
        //	arr[k + low] = temp[k];
        //}
        //arr.length - 1  ->  high
        //temp.length   ->   (high - low + 1)
        //思考为什么上面一个写法会错
        for( int k = 0; k < temp.length; k++ ){
            arr[ k + low ] = temp[ k ];
            //下次归并时low会变
        }
        //这就归并好一次啦
        //然后利用mergeSort递归调用它
    }
}