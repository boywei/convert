package deprecated.xodr2.data.road.referenceline;

import deprecated.xodr2.util.Position;

public abstract class CurveTrack extends Track {

    public CurveTrack(Position startingPosition, double length) {
        super(startingPosition, length);
    }

    @Override
    public Position getEndPosition() {
        return getPositionAlongTrack(getLength());
    }

}
