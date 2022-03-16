import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Convert {

    // 这里对应json的各个部分
    private Car[] cars;
    private String map;
    private String source;
    private int timeStep;
    private String weather;

    // （一）对应XML声明头
    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
    private static final String UPPAAL_HEAD = "<!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_2.dtd'>\n";

    // （二）对应nta一个整体部分，XML头下面就是这一大块，涵盖1234部分
    private void addNta(StringBuffer buffer) {
        buffer.append("<nta>\n");

        addDeclaration(buffer);
        for(int i = 0; i < cars.length; i++) {
            addTemplate(buffer, i);
        }
        addSystem(buffer);
        addQueries(buffer);

        buffer.append("</nta>\n");
    }

    // 1 对应declaration部分，即代码编写处
    private void addDeclaration(StringBuffer buffer) {
        buffer.append("\t<declaration>\n");

        // 已定义部分，与JSON文本内容无关
        addDefined(buffer);
        // 新创建的变量声明等，与JSON内容相关
        create(buffer);

        buffer.append("\t</declaration>\n");
    }

    // 1.1 添加已经定义好的地图数据结构、车辆数据结构、一些信息部分和函数部分
    private void addDefined(StringBuffer buffer) {

    }

    // 1.2 根据JSON中提取到的信息，创建声明变量的语句
    private void create(StringBuffer buffer) {

    }

    // 2 对应template部分，即每辆车的行为树
    private void addTemplate(StringBuffer buffer, int index) {
        buffer.append("\t<template>\n");

        addName(buffer, index);
        addLocalDeclaration(buffer, index);
        addLocations(buffer, index);
        addBranchPoints(buffer, index);
        addInit(buffer, index);
        addTransitions(buffer, index);

        buffer.append("\t</template>\n");
    }

    // 2.1 template名称，即车辆名称
    private void addName(StringBuffer buffer, int index) {
        String name = cars[index].getName();

        buffer.append("\t\t<name>");
        buffer.append(name);
        buffer.append("</name>\n");
    }

    // 2.2 局部变量的声明
    private void addLocalDeclaration(StringBuffer buffer, int index) {
        buffer.append("\t\t<declaration>\n");

        // 局部变量，也可以不写

        buffer.append("\t\t</declaration>\n");
    }

    // 2.3 自动机的每个location，每个状态
    private void addLocations(StringBuffer buffer, int index) {
        Behavior[] behaviors = cars[index].getmTree().getBehaviors();
        int countOfLocation = behaviors.length;
        for(int i = 0; i < countOfLocation; i++) {
            String id = "id" + behaviors[i].getId(), name = behaviors[i].getName();
            double x = behaviors[i].getPosition().getX(), y = behaviors[i].getPosition().getY();
            double nameX = x-10, nameY = y-34;

            buffer.append("\t\t<location id=\"" + id + "\" x=\"" + x + "\" y=\"" + y + "\">\n");

            buffer.append("\t\t\t<name x=\"" + nameX + "\" y=\"" + nameY + "\">");
            buffer.append(name);
            buffer.append("</name>\n");

            buffer.append("\t\t</location>\n");
        }

    }

    // 2.4 转换点branch point
    private void addBranchPoints(StringBuffer buffer, int index) {
        BranchPoint[] branchPoints = cars[index].getmTree().getBranchPoints();
        for (BranchPoint branchPoint : branchPoints) {
            String id = "id" + branchPoint.getId();
            double x = branchPoint.getPosition().getX(), y = branchPoint.getPosition().getY();
            buffer.append("\t\t<branchpoint id=\"" + id + "\" x=\"" + x + "\" y=\"" + y + "\">\n");
            buffer.append("\t\t</branchpoint>\n");
        }
    }

    // 2.5 初始节点
    private void addInit(StringBuffer buffer, int index) {
        // <init ref="id0"/>
        String id = "id0";
        buffer.append("\t\t<init ref=" + id + "/>\n");
    }

    // 2.6 连线
    private void addTransitions(StringBuffer buffer, int index) {
        //      <transition>
        //			<source ref="id3"/>
        //			<target ref="id0"/>
        //		</transition>
        CommonTransition[] commonTransitions = cars[index].getmTree().getCommonTransitions();
        ProbabilityTransition[] probabilityTransitions = cars[index].getmTree().getProbabilityTransitions();

        for(CommonTransition commonTransition : commonTransitions) {
            String from = "id" + commonTransition.getId(), to = "id" + commonTransition.getId();
            buffer.append("\t\t<transition>\n");
            buffer.append("\t\t\t<source ref=\"" + from + "\">\n");
            buffer.append("\t\t\t<target ref=\"" + to + "\">\n");
            buffer.append("\t\t</transition>\n");
        }

    }


    // 3 对应system部分，即系统模版声明处
    private void addSystem(StringBuffer buffer) {
        buffer.append("\t<system>\n");

        buffer.append("system ");
        buffer.append(cars[0].getName());
        for(int i=1; i<cars.length; i++) {
            buffer.append(", " + cars[i].getName());
        }
        buffer.append(";\n");

        buffer.append("\t</system>\n");
    }

    // 4 对应queries部分，即性质规约？
    private void addQueries(StringBuffer buffer) {
        buffer.append("\t<queries>\n");

        // 可能有多个query
        addQuery(buffer);

        buffer.append("\t</queries>\n");
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

        buffer.append("\t\t</query>\n");
    }

    // 提取所有的信息，转化为Java中的类
    private void init(JSONObject jsonObject) {
        Set<Map.Entry<String, Object>> set = jsonObject.entrySet();

        for(Map.Entry<String, Object> entry : set) {
            if(entry.getKey().equals("cars")) {
                JSONArray ja = jsonObject.getJSONArray(entry.getKey());
                int countOfCar = ja.size();
                cars = new Car[countOfCar];
                for(int i = 0; i < countOfCar; i++) {
                    Car car = JSONObject.parseObject(ja.get(i).toString(), Car.class);
                    cars[i] = car;
                }
            }
            else if(entry.getKey().equals("map")){
                map = entry.getValue().toString();
            }
            else if(entry.getKey().equals("source")) {
                source = entry.getValue().toString();
            }
            else if(entry.getKey().equals("timeStep")) {
                timeStep = (Integer) entry.getValue();
            }
            else if(entry.getKey().equals("weather")) {
                weather = entry.getValue().toString();
            }

        }
    }

    // 从这里开始
    public String start() {
        try {
            String jsonStr = FileUtils.readFileToString(new File("src/main/resources/test.json"), "UTF-8");
            JSONObject jsonObject = JSON.parseObject(jsonStr);

            init(jsonObject);

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
