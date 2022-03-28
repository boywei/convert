package xodr.data.road.referenceline;

import xodr.util.Position;

public abstract class CurveTrack extends Track {

    public CurveTrack(Position startingPosition, double length) {
        super(startingPosition, length);
    }

    @Override
    public Position getEndPosition() {
        return getPositionAlongTrack(getLength());
    }

}
