package com.example.demo;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *@Author:xing.yang
 *@Date:2018年8月4日12:47:07
 *@Description；
 * 英文数字zero.one.two到nine.现给一串字符串，里面是打乱的英文数字字母，
 * 而且最后得到的数字是非递减的。怎么做。比如字符串是ozonetower 则结果为012
 *
 * 思路：区分每个数字的不同点，因为是打乱的，所以找出关键的字符作为区分
 * zero，one，two,three,four,five,six,seven,eight,nine
 * x:six u:four g:eight z:zero w:two
 * one,three,five,seven,nine
 * o:one r:three f:five v:seven nine
 *
 */

public class number {

    //标示符
    public static String Xcharacter = "x";
    public static String Ucharacter = "u";
    public static String Gcharacter = "g";
    public static String Zcharacter = "z";
    public static String Wcharacter = "w";
    public static String Ocharacter = "o";
    public static String Rcharacter = "r";
    public static String Fcharacter = "f";
    public static String Vcharacter = "v";

    //预处理字符集合
    public static List<String> nameList = new ArrayList();

    //结果字符串
    public static String result = "";

   public static void main(String[] args){
        String str = "ozonetwer";
        String resultSys = pretreatment(str);
       System.out.println(resultSys);

    }

    public static String pretreatment(String str){
        System.out.println("===========开始处理数据==========");
        if(StringUtils.isBlank(str)){
            System.out.println("传入字符串为空串！");
            return null;
        }
        char[] charList = str.toCharArray();
        for (Character c: charList) {
            nameList.add(String.valueOf(c).toLowerCase());
        }

        dealWith(Xcharacter,"i","x","6");
        dealWith(Ucharacter,"f","o","r","4");
        dealWith(Gcharacter,"e","i","h","t","8");
        dealWith(Zcharacter,"e","r","o","0");

        dealWith(Wcharacter,"t","o","2");
        dealWith(Ocharacter,"n","e","1");
        dealWith(Rcharacter,"t","h","e","e","3");
        dealWith(Fcharacter,"i","v","e","5");
        dealWith(Vcharacter,"s","e","e","n","7");
        dealWith("n","i","n","e","9");

        return result;
    }

    public static void dealWith(String... args){
        System.out.println("=====开始判断数据=======");
        while (nameList.contains(args[0])) {
            for (int i = 0 ;i < args.length-1;i++) {
                if(!nameList.contains(args[i])){
                    System.out.println("=====数据错误====");
                    return;
                }
                nameList.remove(args[i]);
            }
            System.out.println(args[args.length-1]);
            result += args[args.length-1];
        }
        System.out.println("======判断数据结束=======");
        return;

    }
}
