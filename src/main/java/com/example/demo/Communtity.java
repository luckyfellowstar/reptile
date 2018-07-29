package com.example.demo;

import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by jianyj on 2018/7/26.
 */
public class Communtity {
    public static Set<String> result = Sets.newHashSet();

    public static List<String> nameList = new ArrayList();


    public static void main(String[] args) throws IOException, InterruptedException {
        //get("https://nanjing.anjuke.com/community/p1/");
        //普通住宅
        //get("https://nanjing.anjuke.com/community/o4-t25/");
        //别墅

        //get("https://nanjing.anjuke.com/community/o4-t27/");
        //公寓
        //根据不同纬度取删选
        for(int i = 1;i<= 200;i++){
            get("https://nanjing.anjuke.com/community/o2-p"+i+"-t26/");
            Thread.sleep(3000);
            get("https://nanjing.anjuke.com/community/o4-p"+i+"-t26/");
            Thread.sleep(3000);
            get("https://nanjing.anjuke.com/community/o8-p"+i+"-t26/");
            Thread.sleep(3000);
            get("https://nanjing.anjuke.com/community/o6-p"+i+"-t26/");
            System.out.println(i);
            Thread.sleep(3000);
        }
        /*for(int i = 1;i<= 200;i++){
            get("https://nanjing.anjuke.com/community/o4-p"+i+"-t26/");
            System.out.println("https://nanjing.anjuke.com/community/o4-p"+i+"-t26/");
            Thread.sleep(3000);

        }

        for(int i = 1;i<= 200;i++){
            get("https://nanjing.anjuke.com/community/o8-p"+i+"-t26/");
            System.out.println("https://nanjing.anjuke.com/community/o4-p"+i+"-t26/");
            Thread.sleep(3000);

        }
        for(int i = 1;i<= 200;i++){
            get("https://nanjing.anjuke.com/community/o6-p"+i+"-t26/");
            System.out.println("https://nanjing.anjuke.com/community/o4-p"+i+"-t26/");
            Thread.sleep(3000);

        }*/

        //get("https://nanjing.anjuke.com/community/o2-t26/");
        //get("https://nanjing.anjuke.com/community/o8-t26/");
        //get("https://nanjing.anjuke.com/community/o6-t26/");


        FileUtils.writeLines(new File("D:/data/apartment123.txt"), result);
    }

    public static void get(String url) throws IOException, InterruptedException {
        Document document = Jsoup.connect(url).get();

        Elements elements = document.select(".li-itemmod");
        elements.forEach(e -> {
            try {
                String title = e.select(".li-info > h3 > a").attr("title");
                if(nameList.contains(title)){
                    return;
                }
                String date = e.select("p.date").get(0).ownText();
                date = date.replaceAll("竣工日期：","").replaceAll("年","");
                nameList.add(title);
                String idText = e.select(".li-info > h3 > a").attr("href");
                String id = idText.substring(idText.lastIndexOf("/"));
                Thread.sleep(3000);
                Document dc = Jsoup.connect("https://nanjing.anjuke.com/community/view/"+id).get();

                Elements elementDetails = dc.select(".basic-parms-mod");
                //物业费
                String price = elementDetails.select("dd").get(1).ownText();
                //面积
                String area = elementDetails.select("dd").get(2).ownText();
                //总户数
                String count = elementDetails.select("dd").get(3).ownText();
                //停车数
                String parkingSpace = elementDetails.select("dd").get(5).ownText();
                //容积率
                String volumeRate = elementDetails.select("dd").get(6).ownText();
                //开发商
                String developer = elementDetails.select("dd").get(8).ownText();
                //物业公司
                String propertyCompany = elementDetails.select("dd").get(9).ownText();
                Thread.sleep(3000);
                Document dcmt = Jsoup.connect("https://nanjing.anjuke.com/community/round/"+id).get();

                String subway1 = "";
                String subway2 = "";

                Elements ee = dc.select(".metro-list");
                if(ee != null){
                    if(e.select("p.metro-t").size() == 1){
                        subway1 = e.select("p.metro-t").get(0).ownText();
                    }else{
                        subway1 = e.select("p.metro-t").get(0).ownText();
                        subway2 = e.select("p.metro-t").get(0).ownText();
                    }

                }

                //切分地址
                String address = e.select(".li-info address").get(0).ownText();
                String addressTop = "";
                String addressName = "";
                String addressDetail = "";
                if(address.indexOf("［") != -1 && address.indexOf("-") != -1 && address.indexOf("］") != -1){
                    addressTop = address.substring(address.indexOf("［")+1, address.indexOf("-"));
                    addressName = address.substring(address.indexOf("-")+1, address.indexOf("］"));
                    //addressDetail = address.substring(address.indexOf("］")+1);
                }

                //纬度
                String lat = "";
                //经度
                String lon = "";
                //切分经纬度
                String latLon = e.select(".bot-tag > a").attr("href");
                if(latLon.indexOf("#l1=") != -1 && latLon.indexOf("&l2=") != -1 && latLon.indexOf("&l3=") != -1){
                    lat = latLon.substring(latLon.indexOf("#l1=")+4, latLon.indexOf("&l2="));
                    lon = latLon.substring(latLon.indexOf("&l2=")+4, latLon.indexOf("&l3="));
                }
                System.out.println(addressTop + "@" + "@" + addressName + "@" + title+ "@" + date+ "@" + developer+"@" + propertyCompany+"@" + area+"@" + count+"@" + parkingSpace+"@" + volumeRate+"@" + lat + "@" + lon+ "@" + subway1+ "@" + subway2);
                result.add(addressTop + "@" + "@" + addressName + "@" + title+ "@" + date+ "@" + developer+"@" + propertyCompany+"@" + area+"@" + count+"@" + parkingSpace+"@" + volumeRate+"@" + lat + "@" + lon+ "@" + subway1+ "@" + subway2);
            }catch (Exception ex){
                return ;
            }

        });

/*        String next = document.body().select(".aNxt").attr("href");
        if (StringUtils.isNotEmpty(next)) {
            System.out.println("递归调用:" + next);
            Thread.sleep(3000);
            System.out.println(next);
            get(next);
        }*/
    }
}
