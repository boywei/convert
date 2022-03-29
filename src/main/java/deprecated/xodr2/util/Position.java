package deprecated.xodr2.util;

public class Position {

    private final double x;
    private final double y;
    private final double z;
    private final double hdg;

    public Position(double x, double y, double z, double hdg) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.hdg = hdg;
    }

    public Position(double x, double y) {
        this(x, y, 0d, 0d);
    }

    public Position duplicate(){
        return new Position(x, y, z, hdg);
    }

    public Position translateRelativeToHeading(double x2, double y2) {

        double newX = x + x2*Math.cos(hdg) - y2*Math.sin(hdg);
        double newY = y + x2*Math.sin(hdg) + y2*Math.cos(hdg);

        return new Position(newX, newY, z, hdg);
    }

    public Position addRelativeToHeading(Position pos) {
        return translateRelativeToHeading(pos.getX(), pos.getY()).addZ(pos.getZ()).addHdg(pos.getHdg());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Position addZ(double z) {
        return new Position(x, y, this.z + z, hdg);
    }

    public double getHdg() {
        return hdg;
    }

    public Position addHdg(double hdg) {
        return new Position(x, y, z, this.hdg + hdg);
    }

    public Position setHdg(double hdg) {
        return new Position(x, y, z, hdg);
    }

    public Position mirronOnXZ(){
        return new Position(x, -y, z, hdg);
    }

}
