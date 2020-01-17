import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import org.junit.jupiter.api.Test;
import utils.KmlForGame;
import utils.Point3D;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class KmlForGameTest {
    Server.game_service game_service =Game_Server.getServer(0);
    DGraph graph = new DGraph();



    @Test
    void addGraph() {
        graph.init(game_service.getGraph());
        KmlForGame kmlForGame = new KmlForGame();
        kmlForGame.addGraph(graph);
        assertTrue(kmlForGame.getLgerOfGame().toString().contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://earth.google.com/kml/2.2\">\n" +
                "  <Document>\n" +
                "    <name>Points with TimeStamps</name>\n" +
                "    <Style id=\"paddle-a\">\n" +
                "      <IconStyle>\n" +
                "        <Icon>\n" +
                "          <href>http://maps.google.com/mapfiles/kml/paddle/A.png</href>\n" +
                "        </Icon>\n" +
                "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n" +
                "      </IconStyle>\n" +
                "    </Style>\n" +
                "    <Style id=\"paddle-b\">\n" +
                "      <IconStyle>\n" +
                "        <Icon>\n" +
                "          <href>http://maps.google.com/mapfiles/kml/paddle/B.png</href>\n" +
                "        </Icon>\n" +
                "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n" +
                "      </IconStyle>\n" +
                "    </Style>\n" +
                "    <Style id=\"hiker-icon\">\n" +
                "      <IconStyle>\n" +
                "        <Icon>\n" +
                "          <href>http://maps.google.com/mapfiles/ms/icons/hiker.png</href>\n" +
                "        </Icon>\n" +
                "        <hotSpot x=\"0\" y=\".5\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
                "      </IconStyle>\n" +
                "    </Style>\n" +
                "    <Style id=\"check-hide-children\">\n" +
                "      <ListStyle>\n" +
                "        <listItemType>checkHideChildren</listItemType>\n" +
                "      </ListStyle>\n" +
                "    </Style>"));
    }

    @Test
    void writeMyRnF() {
        graph.init(game_service.getGraph());
        KmlForGame kmlForGame = new KmlForGame();
        game_service.addRobot(0);
        game_service.startGame();
        game_service.move();
        kmlForGame.writeMyRnF(graph,game_service);
        //System.out.println(kmlForGame.getLgerOfGame());
        assertTrue(kmlForGame.getLgerOfGame().toString().contains("<Point>\n" +
                "        <coordinates>35.197656770719604,32.10191878639921,0</coordinates>\n" +
                "      </Point>"));
        assertTrue(kmlForGame.getLgerOfGame().toString().contains("/TimeStamp>\n" +
                "      <styleUrl>#hiker-icon</styleUrl>\n" +
                "      <Point>\n" +
                "        <coordinates>35.18753053591606,32.10378225882353,0</coordinates>"));

    }

    @Test
    void saveToFile() {
        graph.init(game_service.getGraph());
        KmlForGame kmlForGame = new KmlForGame();
        game_service.addRobot(0);
        game_service.startGame();
        game_service.move();
        kmlForGame.writeMyRnF(graph,game_service);
        boolean ok = true;
        try {
            kmlForGame.saveToFile("testing");
        } catch (IOException e) {
            ok = false;
        }
        assertTrue(ok);

    }
}