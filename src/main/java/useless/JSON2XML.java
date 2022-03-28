package useless;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JSON2XML {

    /*
      辅助函数：用于打印缩进个数
     */
    public static String nTab(int step) {
        StringBuffer result = new StringBuffer();
        for(int i=0; i<step; i++) {
            result.append("\t");
        }
        return result.toString();
    }

    /*
        辅助函数：转化json中的键值对
     */
    public static void jsonMapHandle(Map<String, Object> m, StringBuffer buffer, int step) {
        for(String k : m.keySet()) {
            buffer.append(nTab(step) + "<" + k + ">");
            buffer.append(m.get(k));
            buffer.append("</" + k + ">\n");
        }
    }

    /*
        辅助函数：转化json中的数组类型，对其中元素逐一判断
     */
    public static void jsonArrayHandle(JSONArray jsonArray, StringBuffer buffer, String elementName, int step) {
        for (int i = 0; i < jsonArray.size(); i++) {
            buffer.append(nTab(step) + "<" + elementName + ">");
            Object o = jsonArray.get(i);
            if(o instanceof String || o instanceof Number) { // 数组里头是字符串或数字; 直接写一行里即可
                buffer.append(o.toString());
            } else {
                buffer.append("\n");
                if(o instanceof JSONObject) { // 数组里头是对象
                    jsonToXml((JSONObject) o, buffer, step+1);
                } else if(o instanceof JSONArray) { // 数组里头是数组
                    jsonArrayHandle((JSONArray) o, buffer, elementName, step+1);
                } else if(o instanceof Map) {
                    jsonMapHandle(JSON.parseObject(JSON.toJSONString(o), Map.class), buffer, step);
                }
                buffer.append(nTab(step));
            }
            buffer.append("</" + elementName + ">\n");
        }
    }

    /*
        主要函数：将json对象转化为set，set中每一元素为键值对，对每个键值对进行遍历
     */
    public static String jsonToXml(JSONObject jsonObject, StringBuffer buffer, int step){
        Set<Map.Entry<String, Object>> set = jsonObject.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = set.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> entry = iterator.next();
            String type = entry.getValue().getClass().getName();

            if (type.equals("com.alibaba.fastjson.JSONObject")) { // 1 值为JSON对象，则直接递归调用
                String objectName = entry.getKey();
                buffer.append(nTab(step) + "<" + objectName + ">\n");
                Object o = entry.getValue();
                if(o instanceof JSONObject) { // 值是对象
                    jsonToXml((JSONObject) o, buffer, step+1);
                } else if(o instanceof JSONArray) { // 值是数组
                    String elementName = objectName.substring(0, objectName.length()-1); // 直接去掉对象名最后的s
                    jsonArrayHandle((JSONArray) o, buffer, elementName, step+1);
                } else if(o instanceof String || o instanceof Number) { // 值是字符串或数字
                    buffer.append(nTab(step) + o.toString());
                }
//                else if(o instanceof Map){
//                    jsonMapHandle(JSON.parseObject(JSON.toJSONString(o), Map.class), buffer);
//                }
                buffer.append(nTab(step) + "</" + objectName + ">\n");
            } else if(type.equals("com.alibaba.fastjson.JSONArray")) { // 2 值为JSON数组，则调用辅助函数jsonArrayHandle
                JSONArray ja = jsonObject.getJSONArray(entry.getKey());
                String arrayName = entry.getKey(); // 数组名，以s结尾
                String elementName = arrayName.substring(0, arrayName.length()-1); // 直接去掉数组名最后的s
                buffer.append(nTab(step) + "<" + arrayName + ">\n");
                jsonArrayHandle(ja, buffer, elementName, step+1);
                buffer.append(nTab(step) + "</" + arrayName + ">\n");
            } else if(type.equals("java.lang.String") || type.equals("java.lang.Number")) { // 3 值为String或数字，则直接打印
                buffer.append(nTab(step) + "<" + entry.getKey() + ">" + entry.getValue());
                buffer.append("</" + entry.getKey() + ">\n");
            } else { // 4 值为map类型，则调用辅助函数jsonMapHandle
                jsonMapHandle(JSON.parseObject(JSON.toJSONString(entry), Map.class), buffer, step);
            }
        }
        return buffer.toString();
    }

    public static String start() {
        try {
            String jsonStr = FileUtils.readFileToString(new File("src/main/resources/books.json"), "UTF-8");
            JSONObject jsonObject = JSON.parseObject(jsonStr);

            StringBuffer buffer = new StringBuffer();
            buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");

            return jsonToXml(jsonObject, buffer, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(start());
    }

}
