import dataStructure.Edata;
import dataStructure.edge_data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdataTest {
    edge_data e1;
    edge_data e2;
    edge_data e3;
    edge_data e4;

    @BeforeEach
    void setUp() {
        e1 = new Edata(0,1,0.5);
        e2 = new Edata(1,0,5);
        e3 = new Edata(2,3,0);
        e4 = new Edata(3,2,6);
    }

    @Test
    void getSrc() {
        assertEquals(0,e1.getSrc());
        assertEquals(1,e2.getSrc());
        assertEquals(2,e3.getSrc());
        assertEquals(3,e4.getSrc());
    }

    @Test
    void getDest() {
        assertEquals(1,e1.getDest());
        assertEquals(0,e2.getDest());
        assertEquals(3,e3.getDest());
        assertEquals(2,e4.getDest());
    }

    @Test
    void getWeight() {
        assertEquals(0.5,e1.getWeight());
        assertEquals(5,e2.getWeight());
        assertEquals(0,e3.getWeight());
        assertEquals(6,e4.getWeight());
    }

    @Test
    void getInfo() {
        setInfo();
    }

    @Test
    void setInfo() {
        e1.setInfo("Roundabout");
        e2.setInfo("Road load");
        e3.setInfo("Road Six");
        e4.setInfo("Long traffic light");

        assertEquals("Roundabout",e1.getInfo());
        assertEquals("Road load",e2.getInfo());
        assertEquals("Road Six",e3.getInfo());
        assertEquals("Long traffic light",e4.getInfo());
    }

    @Test
    void getTag() {
        setTag();
    }

    @Test
    void setTag() {
        e1.setTag(-1);
        e2.setTag(0);
        e3.setTag(3);
        e4.setTag(4);

        assertEquals(-1,e1.getTag());
        assertEquals(0,e2.getTag());
        assertEquals(3,e3.getTag());
        assertEquals(4,e4.getTag());
    }
}