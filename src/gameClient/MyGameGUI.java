package gameClient;

import Server.game_service;
import dataStructure.*;
import org.json.JSONObject;
import utils.*;

import java.util.*;

import java.awt.*;
import java.io.Serializable;
import java.util.List;


public class MyGameGUI implements Serializable{
    private final String res = "update";


    private volatile boolean inAction = false;

    /**
     * Thread that will repaint the gui.
     */
    static class Active extends TimerTask {
        MyGameGUI gui;
        private   Active(MyGameGUI graphGui){
            gui = graphGui;
        }
        public synchronized void run(){
            gui.robotWalk();
            synchronized (gui.res){
                if (!gui.inAction()&&gui.thisMC()) {
                    gui.update();
                }

            }
        }
    }


    private boolean inAction() {
        return inAction;
    }

    private int _id = 0;
    private int mc = -1;
    private game_service game;

    private DGraph dGraph;
    private double minX = 35.186, maxX =35.21, minY = 32.1, maxY = 32.11;

    MyGameGUI(graph graph, game_service game){
        try {
            Thread thread = new Thread(() -> {
                try {
                    MakeSound makeSound = new MakeSound();
                    makeSound.playSound("res/theme.wav");

                } catch (Exception ignored){

                }
            });
            thread.start();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        dGraph = (DGraph) graph;
        this.game = game;
        draw();
        Timer timer = new Timer();
        if (!inAction)
            timer.schedule(new Active(this), 70, 1);

    }


    /**
     * first draw
     */
    private void draw(){
        StdDraw.setCanvasSize(1200, 800);
        StdDraw.setXscale(minX,maxX);
        StdDraw.setYscale(minY,maxY);
        update();
    }

    /**
     * repaint the graph
     */
    private synchronized void update() {
        try {

            while (inAction) {
                Thread.onSpinWait();
            }

            inAction = true;
            StdDraw.setPenRadius(0.01);
            for (node_data n : dGraph.getV()) {
                //                if (first){
//                    first=false;
//                    maxX = n.getLocation().x();
//                    maxY = n.getLocation().y();
//                    minX = n.getLocation().x();
//                    minY = n.getLocation().y();
//                    second = true;
//                }
                maxX = Math.max(n.getLocation().x(), maxX);
                maxY = Math.max(n.getLocation().y(), maxY);
                minX = Math.min(n.getLocation().x(), minX);
                minY = Math.min(n.getLocation().y(), minY);
//                System.out.println(minX + " " + maxX);
                StdDraw.setPenColor(Color.blue);
                StdDraw.setPenRadius(0.04);
                StdDraw.point(n.getLocation().x(), n.getLocation().y());
                StdDraw.setPenColor(Color.green);
                StdDraw.setPenRadius(0.02);
                StdDraw.text(n.getLocation().x(), n.getLocation().y(), n.getKey() + "");
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                if (dGraph.getE(n.getKey()) != null) {
                    List<edge_data> myE = new LinkedList<>(dGraph.getE(n.getKey()));
                    for (edge_data edge : myE) {
                        double x0 = n.getLocation().x();
                        double y0 = n.getLocation().y();
                        double y1 = dGraph.getNode(edge.getDest()).getLocation().y();
                        double x1 = dGraph.getNode(edge.getDest()).getLocation().x();
                        StdDraw.setPenRadius(0.003);
                        StdDraw.line(x0, y0, x1, y1);
                        StdDraw.setPenColor(Color.RED);
                        // StdDraw.text(0.3 * x0 + 0.7 * x1, 0.3 * y0 + 0.7 * y1, edge.getWeight() + "");
                        StdDraw.setPenRadius(0.03);
                        //StdDraw.setPenColor(StdDraw.RED);
                        //StdDraw.point(0.1 * x0 + 0.9 * x1, 0.1 * y0 + 0.9 * y1);
                        StdDraw.setPenRadius(0.01);
                        StdDraw.setPenColor(StdDraw.BLACK);
                    }
                }

                _id = n.getKey();
                StdDraw.setXscale(minX, maxX);
                StdDraw.setYscale(minY, maxY);
            }
        }catch (Exception ignored){

        }
        inAction = false;
    }

    /**
     * repaint the robots
     */
    private void robotWalk() {
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.white);
        StdDraw.picture((minX+maxX)/2,(minY+maxY)/2,"res/Backround.png");
        List<String> log = game.getRobots();
        addFrut();
        update();
        if (log != null) {
            try {

                if (game.isRunning()) {
                    long t = game.timeToEnd();
                    for (String robot_json : log) {
                        try {
                            JSONObject line1 = new JSONObject(robot_json);
                            JSONObject robot = line1.getJSONObject("Robot");
                            int robotId = robot.getInt("id");
                            String pos = robot.getString("pos");
                            String[] pos3D = pos.split(",");
                            Point3D position = new Point3D(Double.parseDouble(pos3D[0]), Double.parseDouble(pos3D[1]));
                            StdDraw.setPenRadius(0.04);
                            //StdDraw.setPenColor(StdDraw.GREEN);
                            double dtx = maxX - minX;
                            double dty = maxY - minY;
                            StdDraw.picture(minX + dtx / 2, maxY - dty / 10, "res/headelin.png");
                            Font font = new Font("Arial", Font.BOLD, 20);
                            StdDraw.setFont(font);
                            StdDraw.setPenColor(Color.BLACK);
                            StdDraw.text(minX + dtx / 2, maxY - dty / 10, "Time left: " + t / 1000 + "." + t % 1000);
                            StdDraw.setFont();
                            if (robotId == 0) {
                                StdDraw.picture(position.x(), position.y(), "res/harryR01.png");
                            } else if (robotId == 1) {
                                StdDraw.picture(position.x(), position.y(), "res/robotP.png", 0.001, 0.001);
                            } else if (robotId == 2) {
                                StdDraw.picture(position.x(), position.y(), "res/wolf.png");

                            } else if (robotId == 3) {
                                StdDraw.picture(position.x(), position.y(), "res/robot3.jpg", 0.001, 0.0004);

                            } else if (robotId == 4) {
                                StdDraw.picture(position.x(), position.y(), "res/robot4.jpg", 0.0004, 0.0004);

                            }

                        } catch (Exception e) {
                            throw new RuntimeException(e);

                        }
                    }
                }
            }catch (Exception ignored){

            }
        }
            StdDraw.show();

    }

    /**
     * repaint the fruit
     */

    private void addFrut(){
        for (String s : game.getFruits()) {
            try {
                JSONObject line1 = new JSONObject(s);
                JSONObject frut = line1.getJSONObject("Fruit");
                double value = frut.getDouble("value");
                String pos = frut.getString("pos");
                int type = frut.getInt("type");
                String[] pos3D = pos.split(",");
                Point3D position = new Point3D(Double.parseDouble(pos3D[0]), Double.parseDouble(pos3D[1]));
                if (type == -1) {
                    StdDraw.picture(position.x(), position.y(), "res/robotH.png", 0.001, 0.001);
                } else {
                    StdDraw.picture(position.x(), position.y(), "res/mapHarry.png");

                }

            } catch (Exception e) {
                throw new RuntimeException(e);

            }
        }
    }



    private boolean thisMC(){
        if (dGraph.getMC()==mc) return false;
        mc = dGraph.getMC();
        return  true;
    }




}
