package xodr.data.road.referenceline;

import xodr.util.Position;

public class StraightCurveTrack extends CurveTrack {

    private double curvature;

    public StraightCurveTrack(Position startingPosition, double length, double curvature) {
        super(startingPosition, length);
        this.curvature = curvature;
    }

    public double getCurvature() {
        return curvature;
    }

    public void reverseCurvature(){
        System.out.println("Curvature before: " + curvature);
        curvature = -0.5;
        System.out.println("Curvature after: " + curvature);
    }

    @Override
    public Position getPositionAlongTrack(double l) {
        if(l == 0d) {
            return getStartingPosition().duplicate();
        } else {
            double angle = curvature*l;
            double radius = 1 / curvature;
            return getStartingPosition().translateRelativeToHeading(radius * Math.sin(angle), radius * (1 - Math.cos(angle))).addHdg(angle);
        }
    }

}
