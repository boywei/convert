package json.exporter;

import json.tree.TreeDataContainer;
import json.tree.entity.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class XMLWriter {

    private static Car[] cars;
    private static String map;
    private static String source;
    private static int timeStep;
    private static String weather;

    // 已定义好的数据结构路径、函数路径、边的变量路径
    private static String DEFINED_PATH = "src/main/resources/uppaal/defined.txt";
    private static String FUNCTION_PATH = "src/main/resources/uppaal/function.txt";
    private static String TRANSITION_PATH = "src/main/resources/uppaal/transition.txt";

    // （一）对应XML声明头
    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
    private static final String UPPAAL_HEAD = "<!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_2.dtd'>\n";

    // （二）对应nta一个整体部分，XML头下面就是这一大块，涵盖1234部分
    private static void addNta(StringBuffer buffer) {
        buffer.append("<nta>\n");

        addDeclaration(buffer);
        for(int i = 0; i < cars.length; i++) {
            addTemplate(buffer, i);
        }
        addSystem(buffer);
        addQueries(buffer);

        buffer.append("</nta>\n");
    }

    // 1 对应declaration部分，即代码编写处（需注意Uppaal中函数在后变量在前）
    private static void addDeclaration(StringBuffer buffer) {
        buffer.append("\t<declaration>\n");

        // 1）已定义的数据结构和变量信息
        addDefined(buffer);
        buffer.append("const double TIME_STEP = " + timeStep + ";\n");
        // 2) 声明路网，初始化
        addMap(buffer);
        // 3) 声明车辆，初始化
        addCar(buffer);
        // 4）已定义好的函数部分
        addFunction(buffer);

        buffer.append("\n\t</declaration>\n");
    }

    // 1.1 添加已经定义好的地图数据结构（包括一些变量信息）
    private static void addDefined(StringBuffer buffer) {
        try {
            String definedContent = FileUtils.readFileToString(new File(DEFINED_PATH), "UTF-8");
            buffer.append(definedContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 1.2 根据JSON中提取到的信息，创建声明变量的语句
    private static void addMap(StringBuffer buffer) {

        // TODO: 解析OpenDrive地图，道路声明
//        buffer.append(new xodr.MapConvert().convertMap(map));

    }

    // 1.3 添加车辆声明
    private static void addCar(StringBuffer buffer) {
        int countOfCar = cars.length;
        buffer.append("Car car[" + countOfCar + "] = {");
        for(int i = 0; i < countOfCar; i++) {
            buffer.append("{");
            buffer.append(i + ", ");
            buffer.append(i + ", ");
            buffer.append(i + ", ");

            buffer.append(cars[i].isHeading() + ", ");
            buffer.append(i + ", ");
            buffer.append(i + ", ");

            buffer.append(cars[i].getRoadId() + ", ");
            buffer.append(cars[i].getLaneId() + ", ");
            buffer.append(cars[i].getLaneSectionId() + ", ");
            buffer.append(i + ", ");
            buffer.append(i + ", ");
            buffer.append(i + ", ");
            buffer.append(i + ", ");
            buffer.append(i);
            buffer.append("}");

            if(i != countOfCar-1) {
                buffer.append(",");
            }

//            buffer.append("bool car" + (i+1) + "_heading = " + cars[i].isHeading() + ";\n");
//            buffer.append("double car" + (i+1) + "_speed = " + cars[i].getInitSpeed() + ";\n");
//            buffer.append("double car" + (i+1) + "_acceleration = " + 0 + ";\n");
//            buffer.append("double car" + (i+1) + "_width = " + 1.0 + ";\n");
//            buffer.append("double car" + (i+1) + "_length = " + 2.0 + ";\n");
//            buffer.append("int car" + (i+1) + "_laneId = " + cars[i].getLaneId() + ";\n");
//            buffer.append("int car" + (i+1) + "_roadId = " + cars[i].getRoadId() + ";\n");
//            buffer.append("double car" + (i+1) + "_laneOffset=random(" + cars[i].getMaxLaneOffset() + "-" + cars[i].getMinLaneOffset() + ")+" + cars[i].getMinLaneOffset() + ";\n");
//            buffer.append("double car" + (i+1) + "_roadOffset=random(" + cars[i].getMaxLaneOffset() + "-" + cars[i].getMinLaneOffset() + ")+" + cars[i].getMinLaneOffset() + ";");

        }
        buffer.append("};\n");
    }

    // 1.4 添加定义好的函数部分：行为的操作实现、地图查询方法、车辆查询方法等
    private static void addFunction(StringBuffer buffer) {
        try {
            String definedContent = FileUtils.readFileToString(new File(FUNCTION_PATH), "UTF-8");
            buffer.append(definedContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2 对应template部分，即每辆车的行为树
    private static void addTemplate(StringBuffer buffer, int index) {
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
    private static void addName(StringBuffer buffer, int index) {
        String name = cars[index].getName();

        buffer.append("\t\t<name>");
        buffer.append(name);
        buffer.append("</name>\n");
    }

    // 2.2 局部变量的声明: 包含三元组算法和自循环加锁算法的变量
    private static void addLocalDeclaration(StringBuffer buffer, int index) {
        buffer.append("\t\t<declaration>\n");

        try {
            String content = FileUtils.readFileToString(new File(TRANSITION_PATH), "UTF-8");
            buffer.append(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        buffer.append("\t\t</declaration>\n");
    }

    // 2.3 自动机的每个location，每个状态
    private static void addLocations(StringBuffer buffer, int index) {
        Behavior[] behaviors = cars[index].getmTree().getBehaviors();
        Map<Integer, Boolean> exist = new HashMap<>();
        for (Behavior behavior : behaviors) {
            if(!exist.containsKey(behavior.getId())) {
                exist.put(behavior.getId(), true);
                String id = "id" + behavior.getId(), name = behavior.getName();
                double x = behavior.getPosition().getX(), y = behavior.getPosition().getY();
                double nameX = x - 10, nameY = y - 34;

                buffer.append("\t\t<location id=\"" + id + "\" x=\"" + x + "\" y=\"" + y + "\">\n");

                buffer.append("\t\t\t<name x=\"" + nameX + "\" y=\"" + nameY + "\">");
                buffer.append(name);
                buffer.append("</name>\n");

                buffer.append("\t\t</location>\n");
            }
        }

    }

    // 2.4 转换点branch point
    private static void addBranchPoints(StringBuffer buffer, int index) {
        BranchPoint[] branchPoints = cars[index].getmTree().getBranchPoints();
        for (BranchPoint branchPoint : branchPoints) {
            String id = "id" + branchPoint.getId();
            double x = branchPoint.getPosition().getX(), y = branchPoint.getPosition().getY();
            buffer.append("\t\t<branchpoint id=\"" + id + "\" x=\"" + x + "\" y=\"" + y + "\">\n");
            buffer.append("\t\t</branchpoint>\n");
        }
    }

    // 2.5 初始节点
    private static void addInit(StringBuffer buffer, int index) {
        // <init ref="id0"/>
        String id = "id0";
        buffer.append("\t\t<init ref=\"" + id + "\"/>\n");
    }

    // 2.6 连线
    /*
        keep: 自循环，当"时钟达到duration"时跳出
        accelerate: 自循环，当"时钟到到达duration"或"速度到达targetSpeed时退出"
        turnLeft: 瞬时动作，完成后左转道
        turnRight: 同上
        changeLeft: 同上
        changeRight: 同上
     */
    private static void addTransitions(StringBuffer buffer, int index) {
        CommonTransition[] commonTransitions = cars[index].getmTree().getCommonTransitions();
        ProbabilityTransition[] probabilityTransitions = cars[index].getmTree().getProbabilityTransitions();
        Behavior[] behaviors = cars[index].getmTree().getBehaviors();

        for (CommonTransition commonTransition : commonTransitions) {
            String from = "id" + commonTransition.getSourceId(), to = "id" + commonTransition.getTargetId();
            buffer.append("\t\t<transition>\n");
            buffer.append("\t\t\t<source ref=\"" + from + "\"/>\n");
            buffer.append("\t\t\t<target ref=\"" + to + "\"/>\n");

            // select 在这里可以妙用，（i,j,k)作为该边在树中的坐标
            buffer.append("\t\t\t<label kind=\"select\">i: int[" +
                    commonTransition.getLevel() + "," + commonTransition.getLevel() + "], j:int[" +
                    commonTransition.getGroup() + "," + commonTransition.getGroup() + "], k:int[" +
                    commonTransition.getNumber() + "," + commonTransition.getNumber() + "]</label>\n");

            // guard 这里需先比对边是否衔接（坐标对应），再比较其他条件
            buffer.append("\t\t\t<label kind=\"guard\">" +
                    "level == i &amp;&amp; group == j" + " &amp;&amp; " +
                    addGuards(commonTransition.getGuards()) + "</label>\n");

            // update/assignment 先更新边的坐标，再更新其他信息
            buffer.append("\t\t\t<label kind=\"assignment\">level = level+1, group = (group-1)*N+k, number=k</label>\n");
            buffer.append("\t\t</transition>\n");
        }

        for(ProbabilityTransition probabilityTransition : probabilityTransitions) {
            String from = "id" + probabilityTransition.getSourceId(), to = "id" + probabilityTransition.getTargetId();
            buffer.append("\t\t<transition>\n");
            buffer.append("\t\t\t<source ref=\"" + from + "\"/>\n");
            buffer.append("\t\t\t<target ref=\"" + to + "\"/>\n");

            // select 在这里可以妙用，（i,j,k)作为该边在树中的坐标
            buffer.append("\t\t\t<label kind=\"select\">i: int[" +
                    probabilityTransition.getLevel() + "," + probabilityTransition.getLevel() + "], j:int[" +
                    probabilityTransition.getGroup() + "," + probabilityTransition.getGroup() + "], k:int[" +
                    probabilityTransition.getNumber() + "," + probabilityTransition.getNumber() + "]</label>\n");

            // update/assignment 先更新边的坐标，再更新其他信息
            buffer.append("\t\t\t<label kind=\"assignment\">level = level+1, group = (group-1)*N+number, number=k</label>\n");

            buffer.append("\t\t\t<label kind=\"probability\">" + probabilityTransition.getWeight() + "</label>\n");
            buffer.append("\t\t</transition>\n");
        }

        // TODO: 增加指向自己的边
        // Keep/Accelerate/Decelerate 都隐含着一条自循环的transition
        for(Behavior behavior : behaviors) {

            if(behavior.getName().equals("Accelerate")) {

            } else if(behavior.getName().equals("Decelerate")) {

            } else if(behavior.getName().equals("Keep")) {

            }
        }

    }

    // 2.6.1 添加guards条件的辅助函数
    private static String addGuards(String[] guards) {
        // TODO: guards条件转换
        StringJoiner joiner = new StringJoiner(" &amp;&amp; ", "(", ")");
        for(String guard : guards) {
            joiner.add(guard.
                    replaceAll(">", "&gt;").
                    replaceAll("<", "&lt;").
                    replaceAll("&", "&amp;"));
        }
        return joiner.toString();
    }

    // 3 对应system部分，即系统模版声明处
    private static void addSystem(StringBuffer buffer) {
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
    private static void addQueries(StringBuffer buffer) {
        buffer.append("\t<queries>\n");

        // 可能有多个query
        addQuery(buffer);

        buffer.append("\t</queries>\n");
    }

    // 4.1
    private static void addQuery(StringBuffer buffer) {
        buffer.append("\t\t<query>\n");

        buffer.append("\t\t\t<formula>");
        // 这里是formula的内容
        buffer.append("</formula>\n");

        buffer.append("\t\t\t<comment>");
        // 这里是comment的内容
        buffer.append("</comment>\n" );

        buffer.append("\t\t</query>\n");
    }

    // 初始化，便于使用
    private static void init(TreeDataContainer container) {
        cars = container.getCars();
        map = container.getMap();
        source = container.getSource();
        timeStep = container.getTimeStep();
        weather = container.getWeather();
    }

    // 从这里开始
    public static void write(TreeDataContainer container, String XMLpath) {
        System.out.println("Writing XML to file: " + XMLpath + "...");

        init(container);
        try {
            StringBuffer buffer = new StringBuffer();

            // uppaal两大组成：头和nta
            buffer.append(XML_HEAD);
            buffer.append(UPPAAL_HEAD);
            addNta(buffer);

            String result = buffer.toString();

            // 输出到XML文件
            File f = new File(XMLpath);
            FileOutputStream fop = new FileOutputStream(f);
            OutputStreamWriter writer = new OutputStreamWriter(fop, StandardCharsets.UTF_8);

            writer.append(result);

            writer.close();
            fop.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Finishing Writing...");
    }
}
