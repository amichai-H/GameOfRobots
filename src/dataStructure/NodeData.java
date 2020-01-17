package dataStructure;

import utils.Point3D;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class NodeData implements node_data, Serializable {
    private static int IDnode=0;
    private int id;
    private Point3D nodeLoaction = null;
    private double nodeWeight = 0;
    private String nodeInfo = "";
    private int nodeTag = -1;
    private HashMap<Integer,Edata> EMap = new LinkedHashMap<>();
    private static double maxY=10, maxX=10 , minX=-10, minY=-10;
    public NodeData(Point3D location){
        maxX = Math.max(location.x(),maxX);
        maxY = Math.max(location.y(),maxY);
        minX = Math.min(location.x(),minX);
        minY = Math.min(location.y(),minY);
        id = IDnode;
        IDnode++;
        nodeLoaction = location;

    }
    public NodeData(int id,Point3D location){
        maxX = Math.max(location.x(),maxX);
        maxY = Math.max(location.y(),maxY);
        minX = Math.min(location.x(),minX);
        minY = Math.min(location.y(),minY);
        this.id= id;
        if (IDnode<=this.getKey())
            IDnode = this.getKey()+1;
        nodeLoaction = location;

    }

    /**
     * Copy constructor
     * @param n
     */
    public NodeData(node_data n){
        id = n.getKey();
        if (IDnode<=n.getKey())
            IDnode = n.getKey()+1;
        nodeLoaction = new Point3D(n.getLocation().x(),n.getLocation().y());
        maxX = Math.max(nodeLoaction.x(),maxX);
        maxY = Math.max(nodeLoaction.y(),maxY);
        minX = Math.min(nodeLoaction.x(),minX);
        minY = Math.min(nodeLoaction.y(),minY);
        nodeWeight = n.getWeight();
        nodeInfo = n.getInfo();
        nodeTag = n.getTag();
    }

    /**
     *
     * @return the key of the node
     */
    @Override
    public int getKey() {
        return id;
    }

    /**
     *
     * @return the location of the node
     */
    @Override
    public Point3D getLocation() {
        return nodeLoaction;
    }

    /**
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(Point3D p) {
        nodeLoaction = new Point3D(p);
    }

    /**
     *
     * @return the weight of this node
     */
    @Override
    public double getWeight() {
        return nodeWeight;
    }

    /**
     * Set weight to this node
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        nodeWeight = w;

    }

    /**
     *
     * @return info of the node
     */
    @Override
    public String getInfo() {
        return nodeInfo;
    }

    /**
     * Set info for the node
     * @param s
     */
    @Override
    public void setInfo(String s) {
        nodeInfo = s;
    }

    /**
     *
     * @return tag in int of this node
     */
    @Override
    public int getTag() {
        return nodeTag;
    }

    /**
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        nodeTag = t;
    }

    /**
     *
     * @return return max of location x of all the nodes
     */
    public static double getMaxX() {
        return maxX;
    }
    /**
     *
     * @return return min of location x of all the nodes
     */
    public static double getMinX() {
        return minX;
    }
    /**
     *
     * @return return max of location y of all the nodes
     */

    public static double getMaxY() {
        return maxY;
    }
    /**
     *
     * @return return min of location y of all the nodes
     */
    public static double getMinY() {
        return minY;
    }

    public static int getIDnode() {
        return IDnode;
    }

    public static void setIDnode(int IDnode) {
        NodeData.IDnode = IDnode;
    }
}
