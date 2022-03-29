package deprecated.xodr2.data;

import deprecated.xodr2.data.road.Road;

import java.util.List;

public class OpenDriveDataContainer {

    private List<Road> roads;

    public OpenDriveDataContainer(List<Road> roads) {
        this.roads = roads;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public Road getRoad(int id){
        for(Road r : roads)
            if(r.getId() == id)
                return r;
        return null;
    }

    public Road getUnmeshedRoad(){
        for(Road r : roads)
            if(!r.isMeshed())
                return r;
        return null;
    }

    public boolean areUnmeshedRoadsLeft(){
        for(Road r : roads)
            if(!r.isMeshed())
                return true;
        return false;
    }
}
