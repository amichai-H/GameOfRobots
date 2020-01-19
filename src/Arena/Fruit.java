package Arena;

import utils.Point3D;

public class Fruit {
    private double value;
    private Point3D position;
    private int type;
    private int id;
    private boolean taken = false;

    /**
     * init fruit
     * @param value - value of the fruit
     * @param point3D - location
     * @param type - from high to low (node id) --> type = -1, or from low to high type = 1.
     * @param id - id of the fruit
     */

    public Fruit(double value,Point3D point3D,int type,int id){
        this.position = point3D;
        this.type = type;
        this.value = value;
        this.id = id;
    }

    /**
     * the value of the fruit
     * @return value in double
     */
    public double getValue() {
        return value;
    }

    /**
     * -1 the src --> des
     * 1 the des --> src
     * @return return the type of the fruit (-1/1)
     */

    public int getType() {
        return type;
    }

    /**
     *
     * @return location in Point3D
     */
    public Point3D getPosition() {
        return position;
    }

    /**
     *
     * @return id of the fruit
     */
    public int getId() {
        return id;
    }

    /**
     * false if Not Taken and true if Taken
     * @return boolean
     */
    public boolean isTaken() {
        return taken;
    }

    /**
     * if you take the fruit
     */
    public void take(){
        this.taken = true;
    }
}
