package Arena;

import utils.Point3D;

public class Fruit {
    private double value;
    private Point3D position;
    private int type;
    private int id;
    private boolean taken = false;

    public Fruit(double value,Point3D point3D,int type,int id){
        this.position = point3D;
        this.type = type;
        this.value = value;
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public Point3D getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public boolean isTaken() {
        return taken;
    }
    public void take(){
        this.taken = true;
    }
}
