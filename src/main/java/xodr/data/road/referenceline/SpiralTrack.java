package xodr.data.road.referenceline;

import xodr.util.Position;

public class SpiralTrack extends CurveTrack {

    private double curveStart;
    private double curveEnd;

    public SpiralTrack(Position startingPosition, double length, double curveStart, double curveEnd) {
        super(startingPosition, length);
        this.curveStart = curveStart;
        this.curveEnd = curveEnd;
    }

    public double getCurveStart() {
        return curveStart;
    }

    public double getCurveEnd() {
        return curveEnd;
    }

    @Override
    public Position getPositionAlongTrack(double l) {

        double lengthCurve = getLength();
        double lengthSection = l;
        double resoltion = 100;
        double steps = resoltion*lengthCurve;

        //double startHeading = 0.0;
        double cStart = getCurveStart();
        double cEnd = getCurveEnd();
        double cDistance = cEnd - cStart;
        double c = cStart;
        double cStep = cDistance/steps;

        double dx, dy;

        double d = 0;
        double dt = lengthCurve/steps;

        Position currentPosition = getStartingPosition().duplicate();

        int i = 0;
        while(i < steps && i*dt < lengthSection){
            dx = Math.cos(d) * dt;
            dy = Math.sin(d) * dt;

            d += dt * c;
            c += cStep;

            currentPosition = currentPosition.translateRelativeToHeading(dx, dy);

            i++;
        }

        currentPosition = currentPosition.addHdg(d);
        return currentPosition;


    }

}
