package deprecated.xodr2.data.road.referenceline;

import java.util.List;

public class ReferenceLine {

    private List<Track> tracks;

    public ReferenceLine(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<Track> getTracks() {
        return tracks;
    }
}
