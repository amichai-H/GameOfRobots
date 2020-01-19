import dataStructure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DGraphTest {
    static graph dGraph;
    static Point3D p1;
    static Point3D p3;
    static Point3D p2;
    static Point3D p4;

    static node_data n1;
    static node_data n2;
    static node_data n3;
    static node_data n4;

    static edge_data e1;
    static edge_data e2;
    static edge_data e3;
    static edge_data e4;
    static edge_data e5;
    @BeforeEach
    void setUp() {
        dGraph = new DGraph();
        p1 = new Point3D(-1,1);
        p3 = new Point3D(-5,6);
        p2 = new Point3D(-4,3);
        p4 = new Point3D(-6,7);

        n1 = new NodeData(p1);
        n2 = new NodeData(p2);
        n3 = new NodeData(p3);
        n4 = new NodeData(p4);

        e1 = new Edata(n1.getKey(), n2.getKey(),1);
        e2 = new Edata(n1.getKey(),n3.getKey(),4);
        e3 = new Edata(n1.getKey(),n4.getKey(),9);
        e4 = new Edata(n2.getKey(),n3.getKey(),1);
        e5 = new Edata(n2.getKey(),n4.getKey(),16);

        dGraph.addNode(n1);
        dGraph.addNode(n2);
        dGraph.addNode(n3);
        dGraph.addNode(n4);
        dGraph.connect(n1.getKey(), n2.getKey(),1);
        dGraph.connect(n1.getKey(), n3.getKey(),4);
        dGraph.connect(n1.getKey(), n4.getKey(),9);
        dGraph.connect(n2.getKey(), n1.getKey(),1);
        dGraph.connect(n2.getKey(), n4.getKey(),16);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getNode() {
        assertEquals(n1.getKey(),dGraph.getNode(n1.getKey()).getKey());
        assertEquals(n1.getTag(),dGraph.getNode(n1.getKey()).getTag());
        assertEquals(n1.getInfo(),dGraph.getNode(n1.getKey()).getInfo());
        assertEquals(n1.getWeight(),dGraph.getNode(n1.getKey()).getWeight());
        assertEquals(n1.getLocation().x(),dGraph.getNode(n1.getKey()).getLocation().x());
        assertEquals(n1.getLocation().y(),dGraph.getNode(n1.getKey()).getLocation().y());

    }

    @Test
    void getEdge() {
        assertEquals(1,dGraph.getEdge(n1.getKey(),n2.getKey()).getWeight());
        assertEquals(n1.getKey(),dGraph.getEdge(n1.getKey(),n2.getKey()).getSrc());
        assertEquals(n2.getKey(),dGraph.getEdge(n1.getKey(),n2.getKey()).getDest());

    }

    @Test
    void addNode() {
        Point3D temPoint =new Point3D(-8,9);
        node_data tempN = new NodeData(temPoint);

        dGraph.addNode(tempN);
        assertEquals(tempN.getKey(),dGraph.getNode(tempN.getKey()).getKey());
        assertEquals(tempN.getTag(),dGraph.getNode(tempN.getKey()).getTag());
        assertEquals(tempN.getInfo(),dGraph.getNode(tempN.getKey()).getInfo());
        assertEquals(tempN.getWeight(),dGraph.getNode(tempN.getKey()).getWeight());
        assertEquals(tempN.getLocation().x(),dGraph.getNode(tempN.getKey()).getLocation().x());
        assertEquals(tempN.getLocation().y(),dGraph.getNode(tempN.getKey()).getLocation().y());

    }

    @Test
    void connect() {
        dGraph.connect(n4.getKey(),n3.getKey(),1);
        edge_data tempE = new Edata(n4.getKey(),n3.getKey(),1);
        assertEquals(tempE.getSrc(),dGraph.getEdge(n4.getKey(),n3.getKey()).getSrc());
        assertEquals(tempE.getDest(),dGraph.getEdge(n4.getKey(),n3.getKey()).getDest());
        assertEquals(tempE.getWeight(),dGraph.getEdge(n4.getKey(),n3.getKey()).getWeight());
    }

    @Test
    void getV() {
       Collection<node_data> collection = dGraph.getV();
       Collection<node_data> expection = new LinkedList<>();
       Collection<Integer> expectedInt = new LinkedList<>();
       Collection<Double> expectedWeight = new LinkedList<>();
       Collection<Double> expectedPx = new LinkedList<>();
       Collection<Double> expectedPy = new LinkedList<>();
       Collection<Integer> collectionInt = new LinkedList<>();
       Collection<Double> collectionWeight = new LinkedList<>();
       Collection<Double> collectionPx = new LinkedList<>();
       Collection<Double> collectionPy = new LinkedList<>();
       expection.add(n1);
       expection.add(n2);
       expection.add(n3);
       expection.add(n4);
        for (node_data temp : collection) {
            collectionInt.add(temp.getKey());
            collectionWeight.add(temp.getWeight());
            collectionPx.add(temp.getLocation().x());
            collectionPy.add(temp.getLocation().y());
        }
        for (node_data temp : expection) {
           expectedInt.add(temp.getKey());
            expectedWeight.add(temp.getWeight());
            expectedPx.add(temp.getLocation().x());
            expectedPy.add(temp.getLocation().y());
        }

       assertEquals(expectedInt,collectionInt);
       assertEquals(expectedWeight,collectionWeight);
       assertEquals(expectedPx,collectionPx);
       assertEquals(expectedPy,collectionPy);
    }

    @Test
    void getE() {

        assertEquals(0,dGraph.getE(n4.getKey()).size());

        Collection<edge_data> collection = dGraph.getE(n1.getKey());
        Collection<edge_data> expection = new LinkedList<>();
        Collection<Double> expectedWeight = new LinkedList<>();
        Collection<Integer> expectedSrc = new LinkedList<>();
        Collection<Integer> expectedDes = new LinkedList<>();
        Collection<Double> collectionWeight = new LinkedList<>();
        Collection<Integer> collectionSrc = new LinkedList<>();
        Collection<Integer> collectionDes = new LinkedList<>();
        expection.add(e1);
        expection.add(e2);
        expection.add(e3);
        for (edge_data temp : collection) {
            collectionWeight.add(temp.getWeight());
            collectionSrc.add(temp.getSrc());
            collectionDes.add(temp.getDest());
        }
        for (edge_data temp : expection) {
            expectedWeight.add(temp.getWeight());
            expectedSrc.add(temp.getSrc());
            expectedDes.add(temp.getDest());
        }

        assertEquals(expectedWeight,collectionWeight);
        assertEquals(expectedSrc,collectionSrc);
        assertEquals(expectedDes,collectionDes);


    }


    @Test
    void removeNode() {
        dGraph.removeNode(n1.getKey());

        assertNull(dGraph.getNode(n1.getKey()));
        assertNull(dGraph.getE(n1.getKey()));
        assertNull(dGraph.getE(n1.getKey()));
        assertNull(dGraph.getEdge(1,0));

    }

    @Test
    void removeEdge() {
        dGraph.removeEdge(1,0);
        assertNull(dGraph.getEdge(1,0));

    }

    @Test
    void nodeSize() {
        assertEquals(4,dGraph.nodeSize());
    }

    @Test
    void edgeSize() {
        assertEquals(5,dGraph.edgeSize());

    }

    @Test
    void getMC() {
        assertEquals(9,dGraph.getMC());
        node_data temp = new NodeData(new Point3D(-80,2));
        dGraph.addNode(temp);
        assertEquals(10,dGraph.getMC());
        dGraph.removeNode(temp.getKey());
        assert(10<dGraph.getMC());

    }
    @Test
    void DgrapfromJson(){
        DGraph jd = new DGraph();
        jd.init("{\"Edges\":[{\"src\":0,\"w\":1.4,\"dest\":1},{\"src\":0,\"w\":1.4620268165085584,\"dest\":10},{\"src\":1,\"w\":1.8884659521433524,\"dest\":0},{\"src\":1,\"w\":1.7646903245689283,\"dest\":2},{\"src\":2,\"w\":1.7155926739282625,\"dest\":1},{\"src\":2,\"w\":1.1435447583365383,\"dest\":3},{\"src\":3,\"w\":1.0980094622804095,\"dest\":2},{\"src\":3,\"w\":1.4301580756736283,\"dest\":4},{\"src\":4,\"w\":1.4899867265011255,\"dest\":3},{\"src\":4,\"w\":1.9442789961315767,\"dest\":5},{\"src\":5,\"w\":1.4622464066335845,\"dest\":4},{\"src\":5,\"w\":1.160662656360925,\"dest\":6},{\"src\":6,\"w\":1.6677173820549975,\"dest\":5},{\"src\":6,\"w\":1.3968360163668776,\"dest\":7},{\"src\":7,\"w\":1.0176531013725074,\"dest\":6},{\"src\":7,\"w\":1.354895648936991,\"dest\":8},{\"src\":8,\"w\":1.6449953452844968,\"dest\":7},{\"src\":8,\"w\":1.8526880332753517,\"dest\":9},{\"src\":9,\"w\":1.4575484853801393,\"dest\":8},{\"src\":9,\"w\":1.022651770039933,\"dest\":10},{\"src\":10,\"w\":1.1761238717867548,\"dest\":0},{\"src\":10,\"w\":1.0887225789883779,\"dest\":9}],\"Nodes\":[{\"pos\":\"35.18753053591606,32.10378225882353,0.0\",\"id\":0},{\"pos\":\"35.18958953510896,32.10785303529412,0.0\",\"id\":1},{\"pos\":\"35.19341035835351,32.10610841680672,0.0\",\"id\":2},{\"pos\":\"35.197528356739305,32.1053088,0.0\",\"id\":3},{\"pos\":\"35.2016888087167,32.10601755126051,0.0\",\"id\":4},{\"pos\":\"35.20582803389831,32.10625380168067,0.0\",\"id\":5},{\"pos\":\"35.20792948668281,32.10470908739496,0.0\",\"id\":6},{\"pos\":\"35.20746249717514,32.10254648739496,0.0\",\"id\":7},{\"pos\":\"35.20319591121872,32.1031462,0.0\",\"id\":8},{\"pos\":\"35.19597880064568,32.10154696638656,0.0\",\"id\":9},{\"pos\":\"35.18910131880549,32.103618700840336,0.0\",\"id\":10}]}");
        assertEquals(1.4,jd.getEdge(0,1).getWeight(),0.001);
        assertEquals(0,jd.getEdge(0,1).getSrc(),0);



    }


}