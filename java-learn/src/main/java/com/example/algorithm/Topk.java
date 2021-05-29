package com.example.algorithm;

import org.junit.Test;

import java.util.Arrays;

/**
 * @Auther: cuijian05
 * @Date: 2020/8/20
 * @Description: topK问题
 */
public class Topk{

    @Test
    public void test(){
        int[] a = { 1, 17, 3, 4, 5, 6, 7, 16, 9, 10, 11, 12, 13, 14, 15, 8 };
        int[] b = topK( a, 4 );
        Arrays.stream( b ).forEach( System.out::println );
    }

    /**
     * 堆排序topK
     *
     * @param data
     * @param k
     * @return
     */
    private int[] topK( int[] data, int k ){
        int[] topK = new int[ k ];
        //先将前k个数放如堆中
        for( int i = 0; i < k && i < data.length; i++ ){
            topK[ i ] = data[ i ];
        }
        //构建最小堆
        MinHeap minHeap = new MinHeap( topK );
        for( int i = k; i < data.length; i++ ){
            int temp = data[ i ];
            if( temp > minHeap.getRoot() ){
                minHeap.setRootValue( temp );
            }
        }
        return topK;
    }

    /**
     * 最小堆（根节点下标为0）
     */
    class MinHeap{

        private int[] data;

        public int[] getData(){
            return data;
        }

        public MinHeap( int[] data ){
            this.data = data;
            buildHeap();
        }

        private void buildHeap(){
            //dataSize() / 2 - 1为下标最大的父节点
            for( int i = dataSize() / 2 - 1; i >= 0; i-- ){
                heapify( i );
            }
        }

        private void heapify( int i ){
            int left = getLeft( i );
            int right = getRight( i );
            int min = i;

            //-----------------------找出下标为left、right和i的值，---------------------------
            //如果左节点存在并且小于根节点 将min的值替换为左节点下标
            if( left >= 0 && left < dataSize() && data[ left ] < data[ min ] ){
                min = left;
            }
            //如果右节点存在并且小于根节点 将min的值替换为右节点下标
            if( right >= 0 && right < dataSize() && data[ right ] < data[ min ] ){
                min = right;
            }
            //------------------------------------------------------------------------------

            if( min == i ){
                return;
            }

            //将最小值和当前根节点调换
            swap( min, i );
            //由于替换，需要对子树再次排序
            heapify( min );

        }

        private int dataSize(){
            return data.length;
        }

        public int getRoot(){
            return data[ 0 ];
        }

        /**
         * 重新设置根节点的值，然后调整堆
         *
         * @param i
         */
        public void setRootValue( int i ){
            //将根节点设置为i
            data[ 0 ] = i;
            //重新调整堆结构
            heapify( 0 );
        }

        /**
         * 获取节点i的右节点下标
         *
         * @param i
         * @return
         */
        private int getRight( int i ){
            return ( i + 1 ) << 1;
        }

        /**
         * 获取节点i的左节点下标
         *
         * @param i
         * @return
         */
        private int getLeft( int i ){
            return ( ( i + 1 ) << 1 ) - 1;
        }

        private void swap( int min, int i ){
            int swap = data[ i ];
            data[ i ] = data[ min ];
            data[ min ] = swap;
        }

    }
}
