package Arena;

import utils.Point3D;

public class Robot {
    private int id;
    private Point3D location;

    public Robot(int id, Point3D point3D){
        this.id = id;
        this.location = point3D;
    }

    public int getId() {
        return id;
    }

    public Point3D getLocation() {
        return location;
    }
}
