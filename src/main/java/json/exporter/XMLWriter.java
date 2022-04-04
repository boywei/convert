package json.exporter;

import json.tree.BehaviorType;
import json.tree.TreeDataContainer;
import json.tree.entity.*;
import org.apache.commons.io.FileUtils;
import xodr.exporter.BufferWriter;
import xodr.importer.XODRInputReader;
import xodr.importer.XODRParser;
import xodr.map.MapDataContainer;

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
    private static final String DEFINED_PATH = "src/main/resources/uppaal/defined.txt";
    private static final String FUNCTION_PATH = "src/main/resources/uppaal/function.txt";
    private static final String TRANSITION_PATH = "src/main/resources/uppaal/transition.txt";
    private static final String TIMER_PATH = "src/main/resources/uppaal/timer.xml";

    public static final int K = 10;

    // （一）对应XML声明头
    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
    private static final String UPPAAL_HEAD = "<!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_2.dtd'>\n";

    // （二）对应nta一个整体部分，XML头下面就是这一大块，涵盖1234部分
    private static void addNta(StringBuffer buffer) {
        buffer.append("<nta>\n");

        addDeclaration(buffer);
        addTimer(buffer);
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
        buffer.append("const int TIME_STEP = " + f(timeStep) + ";\n");
        // TODO: 2) 声明路网，初始化
//        addMap(buffer);
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
        // 1. 读取
        String input = XODRInputReader.readFromFile(map);
        // 2. 解析
        MapDataContainer container = XODRParser.parse(input);
        // 3. 写入buffer
        BufferWriter.write(container, buffer);
    }

    // 1.3 添加车辆声明 TODO: 车辆初始偏移随机
    private static void addCar(StringBuffer buffer) {
        int countOfCar = cars.length;
        buffer.append("//id, width, length, heading, speed, acceleration, maxSpeed, ..., offset\n");
        buffer.append("Car car[" + countOfCar + "] = {");
        for(int i = 0; i < countOfCar; i++) {
//            System.out.println(cars[i].toString());
            buffer.append("{");
            buffer.append(i + ", ");
            buffer.append(f(cars[i].getWidth()) + ", ");
            buffer.append(f(cars[i].getLength()) + ", ");

            buffer.append(cars[i].isHeading() + ", ");
            buffer.append(f(cars[i].getInitSpeed()) + ", ");
            buffer.append(0 + ", ");
            buffer.append(f(cars[i].getMaxSpeed()) + ", ");

            buffer.append(cars[i].getRoadId() + ", ");
            buffer.append(cars[i].getLaneId() + ", ");
            buffer.append(cars[i].getLaneSectionId() + ", ");
            buffer.append(cars[i].getRoadIndex() + ", ");
            buffer.append(cars[i].getLaneSectionIndex() + ", ");
            buffer.append(cars[i].getLaneIndex() + ", ");
            buffer.append(f(cars[i].getOffset()));
            buffer.append("}");

            if(i != countOfCar-1) {
                buffer.append(",");
            }
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

    // 2.0 timer自动机，用于同步时间
    private static void addTimer(StringBuffer buffer) {
        try {
            String definedContent = FileUtils.readFileToString(new File(TIMER_PATH), "UTF-8");
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
        addInit(buffer);
        addTransitions(buffer, index);
        addSelfTransitions(buffer, index);

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
        // 新增一个初始状态
        buffer.append("\t\t<location id=\"id0\">\n" +
                "\t\t\t<name>Start</name>\n" +
                "\t\t</location>\n");

        Behavior[] behaviors = cars[index].getmTree().getBehaviors();
        Map<Integer, Boolean> exist = new HashMap<>();
        for (Behavior behavior : behaviors) {
            if(!exist.containsKey(behavior.getId())) {
                exist.put(behavior.getId(), true);
                String id = "id" + behavior.getId(), name = behavior.getName();

                buffer.append("\t\t<location id=\"" + id + "\">\n");

                buffer.append("\t\t\t<name>");
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
    private static void addInit(StringBuffer buffer) {
        String id = "id0";
        buffer.append("\t\t<init ref=\"" + id + "\"/>\n");
    }

    // 2.6 连线
    private static void addTransitions(StringBuffer buffer, int index) {
        CommonTransition[] commonTransitions = cars[index].getmTree().getCommonTransitions();
        ProbabilityTransition[] probabilityTransitions = cars[index].getmTree().getProbabilityTransitions();

        // Start到行为树的根结点
        buffer.append("\t\t<transition>\n" +
                "\t\t\t<source ref=\"id0\"/>\n" +
                "\t\t\t<target ref=\"id1\"/>\n" +
                "\t\t\t<label kind=\"select\">offset:int[" + f(cars[index].getMinOffset()) + "," + f(cars[index].getMaxOffset()) + "]</label>\n" +
                "\t\t\t<label kind=\"assignment\">initCar(car[" + index + "], offset)</label>\n" +
                "\t\t</transition>\n");

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
                    "level == i &amp;&amp; group == j &amp;&amp; !lock" +
                    addGuards(commonTransition.getGuards()) + "</label>\n");

            // sync 普通迁移不需要信号，自循环才需要
            // buffer.append("<label kind=\"synchronisation\">update?</label>");

            // update/assignment 先更新边的坐标，再更新其他信息
            buffer.append("\t\t\t<label kind=\"assignment\">level = level+1, group = (group-1)*N+k, number=k, lock=true, t=0</label>\n");
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
            buffer.append("\t\t\t<label kind=\"assignment\">level = level+1, group = (group-1)*N+k, number=k</label>\n");

            buffer.append("\t\t\t<label kind=\"probability\">" + probabilityTransition.getWeight() + "</label>\n");
            buffer.append("\t\t</transition>\n");
        }

    }

    // 2.6.1 添加guards条件的辅助函数
    private static String addGuards(String[] guards) {
        // TODO: guards条件转换
//        StringJoiner joiner = new StringJoiner(" &amp;&amp; ", "(", ")");
//        for(String guard : guards) {
//            joiner.add(guard.
//                    replaceAll("&", "&amp;").
//                    replaceAll(">", "&gt;").
//                    replaceAll("<", "&lt;")
//                    );
//        }
//        return joiner.toString();
        return "";
    }

    // 2.7 自循环边
    /*
        keep: 自循环，当"时钟达到duration"时跳出
        accelerate: 自循环，当"时钟到到达duration"或"速度到达targetSpeed时退出"
        turnLeft: 瞬时动作，完成后左转道
        turnRight: 同上
        changeLeft: 同上
        changeRight: 同上
     */
    private static void addSelfTransitions(StringBuffer buffer, int index) {
        // TODO: 增加指向自己的边
        Behavior[] behaviors = cars[index].getmTree().getBehaviors();
        for(Behavior behavior : behaviors) {
            resolveBehavior(buffer, behavior);
        }
    }

    // 2.7.1 操作执行：自循环边不需要k也不需要更新group和level
    private static void resolveBehavior(StringBuffer buffer, Behavior behavior) {
        String from = "id" + behavior.getId(), to = "id" + behavior.getId();
        buffer.append("\t\t<transition>\n");
        buffer.append("\t\t\t<source ref=\"" + from + "\"/>\n");
        buffer.append("\t\t\t<target ref=\"" + to + "\"/>\n");

        // select 在这里可以妙用，（i,j,k)作为该边在树中的坐标
        buffer.append("\t\t\t<label kind=\"select\">" +
                "i: int[" + behavior.getLevel() + "," + behavior.getLevel() + "], " +
                "j:int[" + behavior.getGroup() + "," + behavior.getGroup() + "]" +
//                "k:int[" + behavior.getNumber() + "," + behavior.getNumber() + "]" +
                "</label>\n");

        // guard 这里需先比对边是否衔接（坐标对应），再比较其他条件
        buffer.append("\t\t\t<label kind=\"guard\">" +
                "level == i &amp;&amp; group == j &amp;&amp; lock" +
                "</label>\n");

        buffer.append("\t\t\t<label kind=\"synchronisation\">update?</label>\n");

        // update/assignment 先更新边的坐标，再更新其他信息
        buffer.append("\t\t\t<label kind=\"assignment\">" +
//                "level = level+1, group = (group-1)*N+k, number=k, " +
                "lock=(" + operate(behavior) + "), " +
                "t=t+TIME_STEP" +
                "</label>\n");
        buffer.append("\t\t</transition>\n");

    }

    // 2.7.2
    private static String operate(Behavior behavior) {
        StringBuffer buffer = new StringBuffer();

        if(behavior.getName().equals(BehaviorType.ACCELERATE.getValue())) {

        } else if(behavior.getName().equals(BehaviorType.DECELERATE.getValue())) {

        } else if(behavior.getName().equals(BehaviorType.KEEP.getValue())) {

        } else if(behavior.getName().equals(BehaviorType.TURN_LEFT.getValue())) {

        } else if(behavior.getName().equals(BehaviorType.TURN_RIGHT.getValue())) {

        } else if(behavior.getName().equals(BehaviorType.CHANGE_LEFT.getValue())) {

        } else if(behavior.getName().equals(BehaviorType.CHANGE_RIGHT.getValue())) {

        } else if(behavior.getName().equals(BehaviorType.IDLE.getValue())) {

        }

//        return buffer.toString();
        return "false";
    }

    // 3 对应system部分，即系统模版声明处
    private static void addSystem(StringBuffer buffer) {
        buffer.append("\t<system>\n");

        buffer.append("system timer");
        for (Car car : cars) {
            buffer.append(", " + car.getName());
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

    // 用于计算放大后的数字，因为uppaal中double计算验证会出问题
    private static int f(double x) {
        return (int) Math.round(x * K);
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
