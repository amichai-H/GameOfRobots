import dataStructure.NodeData;
import dataStructure.node_data;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import static org.junit.jupiter.api.Assertions.*;

class NodeDataTest {

    @Test
    void getKey() {
        Point3D p1= new Point3D(-1,1);
        node_data node1 = new NodeData(p1);
        Point3D p2= new Point3D(-2,2);
        node_data node2 = new NodeData(p2);
        Point3D p3= new Point3D(-3,3);
        node_data node3 = new NodeData(p3);

        assertNotEquals(node1.getKey(),node2.getKey());
        assertNotEquals(node1.getKey(),node3.getKey());
        assertNotEquals(node2.getKey(),node3.getKey());
    }

    @Test
    void getLocation() {
        Point3D p1= new Point3D(-1,1);
        node_data node1 = new NodeData(p1);
        Point3D p2= new Point3D(-2,2);
        node_data node2 = new NodeData(p2);
        Point3D p3= new Point3D(-3,3);
        node_data node3 = new NodeData(p3);
        assertEquals(p1,node1.getLocation());
        assertEquals(p2,node2.getLocation());
        assertEquals(p3,node3.getLocation());
    }

    @Test
    void setLocation() {
        Point3D p1= new Point3D(-1,1);
        node_data node1 = new NodeData(p1);
        Point3D p2= new Point3D(-2,2);
        node_data node2 = new NodeData(p2);
        Point3D p3= new Point3D(-3,3);
        node_data node3 = new NodeData(p3);
        node1.setLocation(p2);
        node2.setLocation(p3);
        node3.setLocation(p1);
        assertEquals(p2,node1.getLocation());
        assertEquals(p3,node2.getLocation());
        assertEquals(p1,node3.getLocation());
    }

    @Test
    void getWeight() {
        Point3D p1= new Point3D(-1,1);
        node_data node1 = new NodeData(p1);
        Point3D p2= new Point3D(-2,2);
        node_data node2 = new NodeData(p2);
        Point3D p3= new Point3D(-3,3);
        node_data node3 = new NodeData(p3);
        node1.setWeight(2);
        node2.setWeight(6);
        node3.setWeight(4);
        assertEquals(2,node1.getWeight());
        assertEquals(6,node2.getWeight());
        assertEquals(4,node3.getWeight());
    }

    @Test
    void setWeight() {
        Point3D p1= new Point3D(-1,1);
        node_data node1 = new NodeData(p1);
        Point3D p2= new Point3D(-2,2);
        node_data node2 = new NodeData(p2);
        Point3D p3= new Point3D(-3,3);
        node_data node3 = new NodeData(p3);

        node1.setWeight(2);
        node2.setWeight(7.5);
        node3.setWeight(8.7);
        assertEquals(2,node1.getWeight());
        assertEquals(7.5,node2.getWeight());
        assertEquals(8.7,node3.getWeight());

    }

    @Test
    void getInfo() {
        Point3D p1= new Point3D(-1,1);
        node_data node1 = new NodeData(p1);
        Point3D p2= new Point3D(-2,2);
        node_data node2 = new NodeData(p2);
        Point3D p3= new Point3D(-3,3);
        node_data node3 = new NodeData(p3);

        node1.setInfo("try1");
        node2.setInfo("try2");

        assertEquals("try1",node1.getInfo());
        assertEquals("try2",node2.getInfo());
        assertEquals("",node3.getInfo());

    }

    @Test
    void setInfo() {
        getInfo();
    }

    @Test
    void getTag() {
        Point3D p1= new Point3D(-1,1);
        node_data node1 = new NodeData(p1);
        Point3D p2= new Point3D(-2,2);
        node_data node2 = new NodeData(p2);
        Point3D p3= new Point3D(-3,3);
        node_data node3 = new NodeData(p3);

        node1.setTag(1);
        node2.setTag(-2);

        assertEquals(1,node1.getTag());
        assertEquals(-2,node2.getTag());
        assertEquals(-1,node3.getTag());

    }

    @Test
    void setTag() {
        getTag();
    }

    @Test
    void getMaxX() {
        Point3D p1= new Point3D(-1,1);
        node_data node1 = new NodeData(p1);
        Point3D p2= new Point3D(-2,2);
        node_data node2 = new NodeData(p2);
        Point3D p3= new Point3D(-3,3);
        node_data node3 = new NodeData(p3);

        //assertEquals(0,NodeData.getMaxX());

    }

    @Test
    void getMinX() {
        Point3D p1= new Point3D(-1,1);
        node_data node1 = new NodeData(p1);
        Point3D p2= new Point3D(-2,2);
        node_data node2 = new NodeData(p2);
        Point3D p3= new Point3D(-3,3);
        node_data node3 = new NodeData(p3);

        //assertEquals(-4,NodeData.getMinX());
    }

    @Test
    void getMaxY() {
        Point3D p1= new Point3D(-1,1);
        node_data node1 = new NodeData(p1);
        Point3D p2= new Point3D(-2,2);
        node_data node2 = new NodeData(p2);
        Point3D p3= new Point3D(-3,3);
        node_data node3 = new NodeData(p3);

        //assertEquals(4,NodeData.getMaxY());

    }

    @Test
    void getMinY() {
        Point3D p1= new Point3D(-1,1);
        node_data node1 = new NodeData(p1);
        Point3D p2= new Point3D(-2,2);
        node_data node2 = new NodeData(p2);
        Point3D p3= new Point3D(-3,3);
        node_data node3 = new NodeData(p3);

        //assertEquals(0,NodeData.getMinY());

    }

}