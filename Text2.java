package com.lqy.demo08;

import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text2 {
    public static void main(String[] args) throws IOException {
        //网址
        String familyNameNet="https://hanyu.baidu.com/shici/detail?from=kg1&highlight=&pid=0b2f26d4c0ddb3ee693fdb1137ee1b0d&srcid=51369";
        String boyNameNet="http://www.haoming8.cn/baobao/10881.html";
        String girlNameNet="http://www.haoming8.cn/baobao/7641.html";

        //提取内容
        String familyNameStr = webCrawler(familyNameNet);
       // System.out.println(familyNameStr);
        String boyNameStr = webCrawler(boyNameNet);
        //System.out.println(boyNameStr);
        String girlNameStr = webCrawler(girlNameNet);
       // System.out.println(girlNameStr);


        //正则表达式提取内容

        //姓氏  程嵇邢滑，裴陆荣翁。
       ArrayList<String> familyNameTempStr = getData(familyNameStr, "(.{4})(，|。)",1);
       // System.out.println(familyNameTempStr);
        //男生名字  脱俗、福寿。/[\u4E00-\u9FA5]/
       ArrayList<String> boyNameTempStr = getData(boyNameStr, "([\\u4E00-\\u9FA5]{2})(、|。)",1);
      //System.out.println(boyNameTempStr);
        //女生名字  彤舞 芊静 艾丝 惠蕙 语月
       ArrayList<String> girlNameTempStr = getData(girlNameStr, "([\\u4E00-\\u9FA5]{2} ){4}([\\u4E00-\\u9FA5]{2})",0);
        //System.out.println(girlNameTempStr);

        //提取各个集合有用的元素

        //姓氏 赵钱孙李
        ArrayList<String> familyNamelist=new ArrayList<>();
        for (String s : familyNameTempStr) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                familyNamelist.add(c+"");
            }
        }
       // System.out.println(familyNamelist);

        //男生名字最终集合  大气, 美好, 特色, 大气
        ArrayList<String> boyNamelist=new ArrayList<>();
        for (String s : boyNameTempStr) {
            if (!boyNamelist.contains(s)){
                boyNamelist.add(s);
            }
        }
       // System.out.println(boyNamelist);

        //女生名字最终集合 彤舞 芊静 艾丝 惠蕙 语月
        ArrayList<String> girlNamelist=new ArrayList<>();
        for (String s : girlNameTempStr) {
            String[] s1 = s.split(" ");
            for (int i = 0; i < s1.length; i++) {
                girlNamelist.add(s1[i]);
            }
        }
        //System.out.println(girlNamelist);
        // 获取男生女生名字
        ArrayList<String> list=getIofos(familyNamelist,boyNamelist,girlNamelist,100,50);

        //打乱顺序
        Collections.shuffle(list);
        //写入本地文件
        BufferedWriter bw=new BufferedWriter(new FileWriter("empty-app\\src\\com\\lqy\\demo08\\name.txt"));
        String s;
        for (String s1 : list) {
            bw.write(s1);
            bw.newLine();
        }
        bw.close();

    }
    /*
    * 利用正则表达式提取数据
    * */
    private static ArrayList<String> getData(String str, String regex,int index) {
        //创建储存集合
        ArrayList<String> list=new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(str);
        while(m.find()){
            list.add(m.group(index));
        }
        return list;
    }

    /*
    * 解析网址内容
    * */
    private static String webCrawler(String net) throws IOException {
        //字符串拼接
        StringBuilder sb=new StringBuilder();
        //网址对象
        URL url=new URL(net);
        //打开网址
        URLConnection conn = url.openConnection();
        //创建读取对象
        InputStreamReader isr=new InputStreamReader(conn.getInputStream());
        //读取内容
        int len;
        while((len=isr.read())!=-1){
            sb.append((char)len);
        }
        return sb.toString();
    }

    /*
     * 获取男生女生名字
     * */
    public static ArrayList<String> getIofos(ArrayList<String> familylist,ArrayList<String> boylist,ArrayList<String> grillist,int boycount,int grilcount)
    {
        //男生名字
        HashSet<String> boyName=new HashSet<>();
        while(true){
            if (boycount== boyName.size()) {
                break;
            }
                Collections.shuffle(familylist);
                Collections.shuffle(boylist);
                boyName.add(familylist.get(0)+boylist.get(0));
        }
        //System.out.println(boyName);

        //女名字
        HashSet<String> grilName =new HashSet<>();
        while(true){
            if (grilcount== grilName.size()) {
                break;
            }
            Collections.shuffle(familylist);
            Collections.shuffle(grillist);
            grilName.add(familylist.get(0)+grillist.get(0));
        }
      //  System.out.println(grilName);

        ArrayList<String> list=new ArrayList<>();
        for (String s : boyName) {
            list.add(s);
        }
        for (String s : grilName) {
            list.add(s);
        }

        return list;
    }

}
