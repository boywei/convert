package xodr.data.road.referenceline;

import xodr.util.Position;

public abstract class Track {

    private Position startingPosition;
    private double length;

    public Track(Position startingPosition, double length) {
        this.startingPosition = startingPosition;
        this.length = length;
    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public abstract Position getEndPosition();

    public double getLength() {
        return length;
    }

    public abstract Position getPositionAlongTrack(double l);

}
