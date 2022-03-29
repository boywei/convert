package deprecated.xodr2.data.road.referenceline;

import deprecated.xodr2.util.Position;

public class StraightLineTrack extends Track {

    public StraightLineTrack(Position startingPosition, double length) {
        super(startingPosition, length);
    }

    @Override
    public Position getEndPosition() {
        return getPositionAlongTrack(getLength());
    }

    @Override
    public Position getPositionAlongTrack(double l) {
        return getStartingPosition().translateRelativeToHeading(l, 0d);
    }

}
