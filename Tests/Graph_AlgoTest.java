import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.DGraph;
import dataStructure.NodeData;
import dataStructure.graph;
import dataStructure.node_data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Graph_AlgoTest {
    static graph dGraph;
    graph_algorithms algorithms = new Graph_Algo();
    static node_data n1;
    static node_data n2;
    static node_data n3;
    static node_data n4;

    static node_data n5;
    static node_data n6;
    static node_data n7;
    static node_data n8;

    static node_data n9;

    @BeforeEach
    void setUp() {
        dGraph = new DGraph();

        n1 = new NodeData(new Point3D(-1,1));
        n2 = new NodeData(new Point3D(-5,6));
        n3 = new NodeData(new Point3D(-4,3));
        n4 = new NodeData(new Point3D(-6,7));

        n5 = new NodeData(new Point3D(-32,54));
        n6 = new NodeData(new Point3D(23,65));
        n7 = new NodeData(new Point3D(-19,33));
        n8 = new NodeData(new Point3D(-45,72));

        n9 = new NodeData(new Point3D(-12,85));



        dGraph.addNode(n1);
        dGraph.addNode(n2);
        dGraph.addNode(n3);
        dGraph.addNode(n4);

        dGraph.addNode(n5);
        dGraph.addNode(n6);
        dGraph.addNode(n7);
        dGraph.addNode(n8);
        dGraph.addNode(n9);

        dGraph.connect(n1.getKey(), n2.getKey(),1);
        dGraph.connect(n1.getKey(), n3.getKey(),4);
        dGraph.connect(n1.getKey(), n4.getKey(),9);
        dGraph.connect(n2.getKey(), n1.getKey(),1);
        dGraph.connect(n2.getKey(), n4.getKey(),2);
        dGraph.connect(n3.getKey(), n2.getKey(),1);
        dGraph.connect(n3.getKey(), n4.getKey(),3);
        dGraph.connect(n4.getKey(), n3.getKey(),10);

        dGraph.connect(n5.getKey(), n6.getKey(),1);
        dGraph.connect(n5.getKey(), n7.getKey(),2);
        dGraph.connect(n5.getKey(), n8.getKey(),3);
        dGraph.connect(n6.getKey(), n5.getKey(),1);
        dGraph.connect(n6.getKey(), n8.getKey(),5);
        dGraph.connect(n7.getKey(), n6.getKey(),6);
        dGraph.connect(n7.getKey(), n8.getKey(),7);
        dGraph.connect(n8.getKey(), n7.getKey(),9);

        dGraph.connect(n1.getKey(),n9.getKey(),10);
        dGraph.connect(n9.getKey(),n1.getKey(),1);
        dGraph.connect(n5.getKey(),n9.getKey(),1);
        dGraph.connect(n9.getKey(),n5.getKey(),1);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void init() {
        algorithms.init(dGraph);
    }

    @Test
    void init1() {
        save();
        algorithms.init("test.txt");
    }

    @Test
    void save() {
        algorithms.save("test.txt");
        init();
    }

    @Test
    void isConnected() {
        init();
        assertTrue(algorithms.isConnected());
        dGraph.removeNode(n9.getKey());
        init();
        dGraph.addNode(n9);
        dGraph.connect(n1.getKey(),n9.getKey(),10);
        dGraph.connect(n9.getKey(),n1.getKey(),1);

    }

    @Test
    void shortestPathDist() {
        init();
        assertEquals(2,algorithms.shortestPathDist(n9.getKey(),n2.getKey()));
        assertEquals(4,algorithms.shortestPathDist(n9.getKey(),n4.getKey()));
        assertEquals(12,algorithms.shortestPathDist(n1.getKey(),n6.getKey()));
    }

    @Test
    void shortestPath() {
        init();
        List<node_data> l1 = algorithms.shortestPath(n9.getKey(),n2.getKey());
        List<node_data> l2 = algorithms.shortestPath(n9.getKey(),n4.getKey());
        List<node_data> l3 = algorithms.shortestPath(n1.getKey(),n6.getKey());
        List<Integer> l1int = new LinkedList<>();
        List<Integer> l2int = new LinkedList<>();
        List<Integer> l3int = new LinkedList<>();

        for (node_data node:l1){
            l1int.add(node.getKey());
        }
        for (node_data node:l2){
            l2int.add(node.getKey());
        }
        for (node_data node:l3){
            l3int.add(node.getKey());
        }
        List<Integer> l1result = new LinkedList<>();
        List<Integer> l2result = new LinkedList<>();
        List<Integer> l3result = new LinkedList<>();

        l1result.add(8);
        l1result.add(0);
        l1result.add(1);

        l2result.add(8);
        l2result.add(0);
        l2result.add(1);
        l2result.add(3);

        l3result.add(0);
        l3result.add(8);
        l3result.add(4);
        l3result.add(5);

        assertEquals(l1result,l1int);
        assertEquals(l2result,l2int);
        assertEquals(l3result,l3int);


    }

    @Test
    void TSP() {
        List<Integer> targets = new LinkedList<>();
        targets.add(n1.getKey());
        targets.add(n4.getKey());
        targets.add(n5.getKey());
        targets.add(n7.getKey());
        targets.add(n9.getKey());
        init();
        List<node_data> temp = algorithms.TSP(targets);
        List<Integer> tmpToInt = new LinkedList<>();
        Iterator<node_data> t = temp.iterator();
        while (t.hasNext())
            tmpToInt.add(t.next().getKey());
        List<Integer> results = new LinkedList<>();
        results.add(n1.getKey());
        results.add(n2.getKey());
        results.add(n4.getKey());
        results.add(n3.getKey());
        results.add(n2.getKey());
        results.add(n1.getKey());
        results.add(n9.getKey());
        results.add(n5.getKey());
        results.add(n7.getKey());
        assertEquals(results,tmpToInt);
    }

    @Test
    void copy() {
        init();
        graph copyGraph = algorithms.copy();
        assertNotEquals(dGraph.getNode(n1.getKey()),copyGraph.getNode(n1.getKey()));
        assertNotEquals(dGraph.getNode(n2.getKey()),copyGraph.getNode(n2.getKey()));
        assertNotEquals(dGraph.getE(n1.getKey()),copyGraph.getE(n1.getKey()));
        Graph_Algo copyG = new Graph_Algo();
        copyG.init(copyGraph);
        assertEquals(algorithms.isConnected(),copyG.isConnected());
    }
}