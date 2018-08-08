package com.example.demo;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *@Author:xing.yang
 *@Date:2018年8月4日12:47:07
 *@Description；
 * 第有M张牌顺时针发给N个人。如果剩下的牌不够发，就不发。求手上牌的和最大的值。
 * 输入：M N 和所有的牌的数字。
 * 5 3
 * 1 5 6 2 9
 * 6
 *
 * 思路：首先求出M/N的值是发牌次数
 *
 *
 */

public class licensing {

    //初始化定义量
    public static int M = 11;
    public static int N = 3;
    public static String str = "1,5,6,2,9,12,23,2,3,23,21";
    public static int max = 0;


    //最后确定发牌集合
    public static List<Integer> numberList = new ArrayList();

    //结果字符串
    public static String result = "";

   public static void main(String[] args){

       int result = pretreatment(str);
       System.out.println(result);

    }

    public static int pretreatment(String str) {
        System.out.println("===========开始处理数据==========");
        int count = M/N;

        if(count == 0){
            return 0;
        }

        String[] strs = str.split(",");
        for (int i = 0 ;i< count*N;i++){
            numberList.add(Integer.valueOf(strs[i]));
        }

        for (int j =0 ;j < N; j++){
            int num = 0;
            for (int i = 0;i< count;i++){
                num += numberList.get(i*N+j);
            }

            if(num>max){
                System.out.println("=====进行一次最大值交换=====最大值是："+num);
                max = num;
            }
        }
        return max;

    }


}
