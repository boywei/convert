package xodr.importer;

import xodr.map.MapDataContainer;
import xodr.map.entity.Junction;
import xodr.map.entity.Road;

public class XODRParser {

    public static MapDataContainer parse(String input) {
        System.out.println("Parsing input...");
        Road[] roads = null;
        Junction[] junctions = null;




        System.out.println("Finishing parsing...");
        return new MapDataContainer(roads, junctions);
    }

}
