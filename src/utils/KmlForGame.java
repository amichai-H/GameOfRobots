package utils;

import Arena.Fruit;
import Arena.Fruits;
import Arena.Robot;
import Arena.Robots;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.node_data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class KmlForGame {
    private StringBuffer lgerOfGame = new StringBuffer();

    public KmlForGame(){
        lgerOfGame.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
                "    </Style>\n" +
                " ");

    }

    public void addGraph(DGraph gg) {
        for (node_data n : gg.getV()) {
            lgerOfGame.append("<Placemark>\n" + "    <description>" + "place num:").append(n.getKey()).append("</description>\n").append("    <Point>\n").append("      <coordinates>").append(n.getLocation().x()).append(",").append(n.getLocation().y()).append(",0</coordinates>\n").append("    </Point>\n").append("  </Placemark>\n");
        }
    }

    public void writeMyRnF(DGraph gg, game_service game) {
        Robots robots = new Robots(game,gg);
        Date date =new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        String timeStr = df.format(date);
        String timeStr2 = df2.format(date);
        String finalDate = timeStr+"T"+timeStr2+"Z";
        Fruits fruits = new Fruits(game,gg);
        Iterator<Robot> robotsII = robots.iterator();
        Iterator<Fruit> fruitIterator = fruits.iterator();
        while (robotsII.hasNext()){
            Robot temp = robotsII.next();
            lgerOfGame.append("<Placemark>\n" + "      <TimeStamp>\n" + "        <when>").append(finalDate).append("</when>\n").append("      </TimeStamp>\n").append("      <styleUrl>#hiker-icon</styleUrl>\n").append("      <Point>\n").append("        <coordinates>").append(temp.getLocation().x()).append(",").append(temp.getLocation().y()).append(",0</coordinates>\n").append("      </Point>\n").append("    </Placemark>");
        }
        while (fruitIterator.hasNext()){
            Fruit temp = fruitIterator.next();
            String typer = "#paddle-a";
            if (temp.getType()==1){
                typer = "#paddle-b";
            }
            lgerOfGame.append("<Placemark>\n" + "      <TimeStamp>\n" + "        <when>").append(finalDate).append("</when>\n").append("      </TimeStamp>\n").append("      <styleUrl>").append(typer).append("</styleUrl>\n").append("      <Point>\n").append("        <coordinates>").append(temp.getPosition().x()).append(",").append(temp.getPosition().y()).append(",0</coordinates>\n").append("      </Point>\n").append("    </Placemark>");

        }

    }
    public void saveToFile(String fileName) throws IOException {
        lgerOfGame.append("  </Document>\n" +
                "</kml>");
        File file = new File(fileName+".kml");
        FileWriter writer = new FileWriter(file);
        writer.write(String.valueOf(lgerOfGame));
        writer.close();


    }

    public StringBuffer getLgerOfGame() {
        return lgerOfGame;
    }
}
