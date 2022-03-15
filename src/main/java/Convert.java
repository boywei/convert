import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Convert {

    private int countOfCar;


    // （一）对应XML声明头
    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private static final String UPPAAL_HEAD = "<!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_2.dtd'>\n";

    // （二）对应nta一个整体部分，XML头下面就是这一大块，涵盖1234部分
    private void addNta(StringBuffer buffer) {
        buffer.append("<nta>\n");

        addDeclaration(buffer);
        // 可能有多个template需要添加
        addTemplate(buffer);
        addSystem(buffer);
        addQueries(buffer);

        buffer.append("</nta>");
    }

    // 1 对应declaration部分，即代码编写处
    private void addDeclaration(StringBuffer buffer) {
        buffer.append("\t<declaration>");

        // 已定义部分，与JSON文本内容无关
        addDefined(buffer);
        // 新创建的变量声明等，与JSON内容相关
        create(buffer);

        buffer.append("\t</declaration>");
    }

    // 1.1 添加已经定义好的地图数据结构、车辆数据结构、一些信息部分和函数部分
    private void addDefined(StringBuffer buffer) {

    }

    // 1.2 根据JSON中提取到的信息，创建声明变量的语句
    private void create(StringBuffer buffer) {

    }

    // 2 对应template部分，即每辆车的行为树
    private void addTemplate(StringBuffer buffer) {
        buffer.append("\t<template>\n");

        addName(buffer);
        addLocalDeclaration(buffer);
        addLocations(buffer);
        addBranchpoints(buffer);
        addInit(buffer);
        addTransitions(buffer);

        buffer.append("\t</template>\n");
    }

    // 2.1 template名称，即车辆名称
    private void addName(StringBuffer buffer) {

    }

    // 2.2 局部变量的声明
    private void addLocalDeclaration(StringBuffer buffer) {

    }

    // 2.3 自动机的每个location，每个状态
    private void addLocations(StringBuffer buffer) {

    }

    // 2.4 转换点branch point
    private void addBranchpoints(StringBuffer buffer) {

    }

    // 2.5 初始节点
    private void addInit(StringBuffer buffer) {

    }

    // 2.6 连线
    private void addTransitions(StringBuffer buffer) {

    }


    // 3 对应system部分，即系统模版声明处
    private void addSystem(StringBuffer buffer) {

    }

    // 4 对应queries部分，即性质规约？
    private void addQueries(StringBuffer buffer) {
        buffer.append("\t<queries>");

        // 可能有多个query
        addQuery(buffer);

        buffer.append("\t</queries>");
    }

    // 4.1
    private void addQuery(StringBuffer buffer) {
        buffer.append("\t\t<query>\n");

        buffer.append("\t\t\t<formula>");
        // 这里是formula的内容
        buffer.append("</formula>\n");

        buffer.append("\t\t\t<comment>");
        // 这里是comment的内容
        buffer.append("</comment>\n" );

        buffer.append("\t\t</query>");
    }

    // 从这里开始
    public String start() {
        try {
            String jsonStr = FileUtils.readFileToString(new File("src/main/resources/test2.json"), "UTF-8");
            JSONObject jsonObject = JSON.parseObject(jsonStr);

            StringBuffer buffer = new StringBuffer();
            buffer.append(XML_HEAD);
            buffer.append(UPPAAL_HEAD);
            addNta(buffer);

            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new Convert().start());
    }
}
