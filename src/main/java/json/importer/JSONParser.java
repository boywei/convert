package json.importer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import json.tree.entity.Behavior;
import json.tree.entity.Car;
import json.tree.TreeDataContainer;

import java.util.Map;
import java.util.Set;

public class JSONParser {

    private static void modifyBehaviorId(Car[] cars) {
        if(cars == null) {
            return;
        }
        // 用于对location去重
        for(Car car : cars) {
            int id = 0;
            for(Behavior behavior : car.getmTree().getBehaviors()) {
                if(!car.location.containsKey(behavior.getName())) {
                    car.location.put(behavior.getName(), id);
                    behavior.setId(id);
                    id++;
                } else {
                    behavior.setId(car.location.get(behavior.getName()));
                }
            }
        }
    }

    public static TreeDataContainer parse(String input) {
        System.out.println("Parsing input...");

        Car[] cars = null;
        String map = "";
        String source = "";
        int timeStep = 0;
        String weather = "";

        JSONObject jsonObject = JSON.parseObject(input);

        Set<Map.Entry<String, Object>> set = jsonObject.entrySet();

        for(Map.Entry<String, Object> entry : set) {
            switch (entry.getKey()) {
                case "cars":
                    JSONArray ja = jsonObject.getJSONArray(entry.getKey());
                    int countOfCar = ja.size();
                    cars = new Car[countOfCar];
                    for (int i = 0; i < countOfCar; i++) {
                        Car car = JSONObject.parseObject(ja.get(i).toString(), Car.class);
                        cars[i] = car;
                    }
                    break;
                case "entity":
                    map = entry.getValue().toString();
                    break;
                case "source":
                    source = entry.getValue().toString();
                    break;
                case "timeStep":
                    timeStep = (Integer) entry.getValue();
                    break;
                case "weather":
                    weather = entry.getValue().toString();
                    break;
            }
        }

        // 需要对Behavior id进行更改，将同名节点归为同一id，否则会有重名节点
        modifyBehaviorId(cars);

        System.out.println("Finishing parsing...");
        return new TreeDataContainer(cars, map, source, timeStep, weather);
    }
}
