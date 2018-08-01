package com.example.demo;

import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;

/**
 * @Author:xing.yang
 * @Date:2018年8月1日14:47:07
 * @Description；
 * 此类是为了爬安居客小区
 * 根据不同地区的分类来爬数据
 * 每页最多爬30条，根据下一页最多可以爬100页，如果超过100页需要根据分页的方式去爬
 * 每个地区又会根据不同的类别去爬
 * 根据不同地区生成不同的txt文件
 * 数据之间以@区分
 */

public class Communtity {
    //去重（使用小区名称）
    public static List<String> nameList = new ArrayList();
    //存储最终集合
    public static Set<String> result = Sets.newHashSet();
    //根据类型分
    public static LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
    //不同的地区集合
    public static HashMap<String, String> urlMap = new HashMap<String, String>();
    //间隔1秒，直接处理会被限制
    public static int threadSleepTime = 1000;

    static {
        typeMap.put("公寓", "t26");
        typeMap.put("别墅", "t27");
        typeMap.put("普通住宅", "t25");
        typeMap.put("平房", "t12");
        typeMap.put("未知", "");

        urlMap.put("江宁", "https://nanjing.anjuke.com/community/jiangninga/p1");
        urlMap.put("浦口", "https://nanjing.anjuke.com/community/pukou/p1");
        urlMap.put("建邺", "https://nanjing.anjuke.com/community/jianye/p1");
        urlMap.put("鼓楼", "https://nanjing.anjuke.com/community/guloua/p1");
        urlMap.put("秦淮", "https://nanjing.anjuke.com/community/qinhuai/p1");
        urlMap.put("玄武", "https://nanjing.anjuke.com/community/xuanwub/p1");
        urlMap.put("栖霞", "https://nanjing.anjuke.com/community/qixia/p1");
        urlMap.put("雨花台", "https://nanjing.anjuke.com/community/yuhuatai/p1");
        urlMap.put("六合", "https://nanjing.anjuke.com/community/liuhe/p1");
        urlMap.put("溧水", "https://nanjing.anjuke.com/community/lishuia/p1");
        urlMap.put("高淳", "https://nanjing.anjuke.com/community/gaochun/p1");
        urlMap.put("南京周边", "https://nanjing.anjuke.com/community/nanjingzhoubian/p1");
    }

    public static void main(String[] args) throws Exception {
        //根据不同区域爬取数据
        runData("江宁");
/*        runData("浦口");
        runData("建邺");
        runData("鼓楼");
        runData("秦淮");
        runData("玄武");
        runData("栖霞");
        runData("雨花台");
        runData("六合");
        runData("溧水");
        runData("高淳");
        runData("南京周边");*/
    }

    public static void runData(String quyu) throws Exception {
        System.out.println("=============爬取[" + quyu + "]数据开始===========");
        Date start = new Date();
        result = Sets.newHashSet();
        for (Map.Entry<String, String> entry : typeMap.entrySet()) {
            String typeName = entry.getKey();
            String value = entry.getValue();
            String tempURL = urlMap.get(quyu);
            if (StringUtils.isNotEmpty(value)) {
                tempURL = urlMap.get(quyu) + "-" + value;
            }
            get(tempURL, quyu, typeName);
            Thread.sleep(threadSleepTime);
        }

        FileUtils.writeLines(new File("D:/data/" + quyu + ".txt"), result);
        long time = ((new Date().getTime() - start.getTime()) / 1000) / 60;
        //result.add(quyu + "数据总共：" + result.size() + "条数据，费时[" + time + "]分钟");
        System.out.println("=============爬取[" + quyu + "]数据结束===========总共[" + result.size() + "]条数据，费时[" + time + "]分钟");
    }



    public static void get(String url, String quyu, String typeName) throws Exception {
        System.out.println("=============爬取[" + quyu + "-" + typeName + "]**本页**数据开始===========URL[" + url + "]");

        Map<String, String> headers = new HashMap<String, String>();
        //伪装头信息，不是真正的浏览器访问，需要伪装
        headers.put("method", "POST");
        headers.put("origin", "https://nanjing.anjuke.com");
        headers.put("cookie","aQQ_ajkguid=BF976E17-6744-4CA9-C64C-0A137BC462CB; 58tj_uuid=2be49bca-b8f8-4c07-a608-09eebd3d91bd; als=0; _ga=GA1.2.1463840483.1530093464; _gid=GA1.2.37730930.1532759827; lps=http%3A%2F%2Fnanjing.anjuke.com%2Fcommunity%2Fview%2F401058%7Chttps%3A%2F%2Fnanjing.anjuke.com%2Fcommunity%2Fround%2F401058; ctid=16; search_words=%E4%B8%9C%E6%96%B9%E9%BE%99%E6%B9%96%E6%B9%BE; sessid=7FF8A3CA-B74E-1750-3AED-D917C15BCE2D; twe=2; init_refer=https%253A%252F%252Fnanjing.anjuke.com%252Fcommunity%252Fgaochun%252Ft27%252F; new_uv=17; new_session=0; __xsptplusUT_8=1; __xsptplus8=8.17.1532877656.1532878891.18%233%7Cwww.google.com%7C%7C%7C%7C%23%23hOEHhFi5284XScpXS3paPyPe869pSWrO%23");
        Document document = Jsoup.connect(url).timeout(4000)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
                .headers(headers)
                .referrer(url)
                .get();

        Elements elements = document.select(".li-itemmod");
        if(elements == null){
            System.out.println("=======本页元素为空========");
            return ;
        }
        for (Element e : elements) {
            try {
                //拼接结果字符串
                String resultStr ="";
                //小区名称
                String title = e.select(".li-info > h3 > a").attr("title");
                //去重
                if (nameList.contains(title)) {
                    System.out.println("重复数据");
                    continue;
                }
                nameList.add(title);

                String idText = e.select(".li-info > h3 > a").attr("href");
                String id = idText.substring(idText.lastIndexOf("/") + 1);
                resultStr += id ;

                //切分地址
                String address = e.select(".li-info address").get(0).ownText();
                String communtityAddress = "暂无数据";
                if (address.indexOf("［") != -1 && address.indexOf("-") != -1 && address.indexOf("］") != -1) {
                    String addressTop = address.substring(address.indexOf("［") + 1, address.indexOf("-"));
                    String addressName = address.substring(address.indexOf("-") + 1, address.indexOf("］"));
                    communtityAddress = address.substring(address.indexOf("］") +1);
                    resultStr += "@" + addressTop + "@" + addressName;
                } else{
                    resultStr = stitching(resultStr,2);
                }

                String date = e.select("p.date").get(0).ownText().replaceAll("竣工日期：", "").replaceAll("年", "");
                resultStr = stitching(resultStr,2) + "@" + title + "@" + typeName + "@" + date;

                //获得详细信息
                resultStr = getDetail(resultStr,id);
                resultStr =stitching(resultStr,4);

                //切分经纬度
                String latLon = e.select(".bot-tag > a").attr("href");
                if (latLon.indexOf("#l1=") != -1 && latLon.indexOf("&l2=") != -1 && latLon.indexOf("&l3=") != -1) {
                    //纬度
                    String  lat = latLon.substring(latLon.indexOf("#l1=") + 4, latLon.indexOf("&l2="));
                    //经度
                    String  lon = latLon.substring(latLon.indexOf("&l2=") + 4, latLon.indexOf("&l3="));
                    resultStr += "@" + lat + "@" + lon;
                }else {
                    resultStr = stitching(resultStr,2);
                }

                //获得地铁信息
                resultStr = getSubway(resultStr,id);

                /*result.add(id + "@@" + addressTop + "@@" + addressName + "@@" + title + "@@" + typeName + "@@" + date + "@@" + developer + "@@" + propertyCompany + "@@" + area + "@@" + count + "@@" +
                        parkingSpace + "@@" + volumeRate + "@@" + price + "@@" + lat + "@@" + lon + "@@" + lineCount + "@@" + subway1);*/
                resultStr += "@" + communtityAddress;
                result.add(resultStr);
            } catch (Exception ex) {
                System.out.println("异常数据:" + ex.getMessage());
                continue;
            }
        }
        System.out.println("=============爬取[" + quyu + "-" + typeName + "]**此页**数据结束===========URL[" + url + "], 总条数["+result.size()+"]");
        String next = document.body().select(".aNxt").attr("href");
        if (StringUtils.isNotEmpty(next)) {
            System.out.println("递归调用:" + next);
            Thread.sleep(threadSleepTime);
            System.out.println("===========准备爬取[" + quyu + "-" + typeName + "]**下页**数据===========" );
            get(next, quyu, typeName);
        }
        System.out.println("=============爬取[" + quyu + "-" + typeName + "]数据结束===========URL[" + url + "], 总条数["+result.size()+"]");
    }

    public static String getDetail(String str,String id) throws Exception {
        Thread.sleep(threadSleepTime);
        Document dc = Jsoup.connect("https://nanjing.anjuke.com/community/view/" + id).get();

        Elements elementDetails = dc.select(".basic-parms-mod");
        if(elementDetails == null){
            System.out.println("=====没有获取到详细信息=====");
            str = stitching(str,7);
            return str;
        }
        //物业费
        String price = elementDetails.select("dd").get(1).ownText();
        if (price.contains("元/㎡/月")) {
            price = price.substring(0, price.indexOf("元/㎡/月"));
        }
        if (price.contains("元/平米/月")) {
            price = price.substring(0, price.indexOf("元/平米/月"));
        }
        //面积
        String area = elementDetails.select("dd").get(2).ownText().replaceAll("m²","");
        //总户数
        String count = elementDetails.select("dd").get(3).ownText().replaceAll("户","");
        //停车数
        String parkingSpace = elementDetails.select("dd").get(5).ownText();
        //容积率
        String volumeRate = elementDetails.select("dd").get(6).ownText();
        //开发商
        String developer = elementDetails.select("dd").get(8).ownText();
        //物业公司
        String propertyCompany = elementDetails.select("dd").get(9).ownText();

        str += "@" + developer + "@" + propertyCompany + "@" + area + "@" + count + "@" + parkingSpace + "@" + volumeRate;
        //小区描述
        if(dc.select(".cm-facility-mod p").size() == 0){
            str = stitching(str,1)+ "@" + price;
            return str;
        }
        String communtityDescription = dc.select(".cm-facility-mod p").get(0).ownText();
        str +=  "@" + communtityDescription + "@" + price;
        return str;
    }

    public static String getSubway(String str ,String id)throws Exception{
        Thread.sleep(threadSleepTime);
        Document dcmt = Jsoup.connect("https://nanjing.anjuke.com/community/round/" + id).get();

        Elements ee = dcmt.select(".metro-list");
        if(ee.size() ==0 || ee == null){
            str = stitching(str,4);
            return str;
        }

        String lineCount = ee.select("p.metro-t").size() + "";

        String subwayDetail = "{'metro':[";
        for (int i = 0; i < ee.select("p.metro-t").size(); i++) {

            String subwayTemp = ee.select("p.metro-t").get(i).ownText();
            String subwayLine = subwayTemp.substring(0, subwayTemp.indexOf("线") + 1);
            String subwayStand = subwayTemp.substring(subwayTemp.indexOf("线") + 1, subwayTemp.indexOf("－"));
            if (subwayStand.indexOf("·") != -1) {
                subwayStand = subwayStand.substring(subwayStand.lastIndexOf("·") + 1);
            }
            String subwayDistance = subwayTemp.substring(subwayTemp.indexOf("步行") + 2, subwayTemp.indexOf("m"));
            if(0 == i){
                str += "@" + subwayLine + "@" + subwayStand + "@" + subwayDistance;
                subwayDetail = subwayDetail + "{'lineName':"+ subwayLine +"','standName':'"+subwayStand+"','distance':'"+subwayDistance+"'}";
            }else{
                subwayDetail = subwayDetail + ",{'lineName':"+ subwayLine +"','standName':'"+subwayStand+"','distance':'"+subwayDistance+"'}";
            }
        }
        str = str + "@"+ subwayDetail + "]}";

        return str;
    }

    /**
     * 拼接字符串
     * @param str
     * @param count
     * @return
     */
    public static String stitching(String str,int count){
        for (int i = 0;i<count;i++){
            str = str + "@" + "暂无数据";
        }
        return str;
    }
}