package json.tree;

import json.tree.entity.Car;

public class TreeDataContainer {
    // 这里对应json的各个部分
    private Car[] cars;
    private String map;
    private String source;
    private int timeStep;
    private String weather;

    public TreeDataContainer() {

    }

    public TreeDataContainer(Car[] cars, String map, String source, int timeStep, String weather) {
        this.cars = cars;
        this.map = map;
        this.source = source;
        this.timeStep = timeStep;
        this.weather = weather;
    }

    public Car[] getCars() {
        return cars;
    }

    public void setCars(Car[] cars) {
        this.cars = cars;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
