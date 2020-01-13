package gameClient;

import Server.game_service;
import dataStructure.*;
import org.json.JSONObject;
import utils.Point3D;
import utils.Range;
import utils.StdDraw;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.*;

public class MyGameGUI implements Serializable{
    final String res = "update";


    private boolean inAction = false;

    static class Active extends TimerTask {
        MyGameGUI gui;
        boolean press = false;
        Point3D temp1 = null;
        Point3D temp2 = null;
        Date date;
        long time;
        boolean update = false;
        public  Active(MyGameGUI graphGui){
            gui = graphGui;
        }
        public synchronized void run(){
            gui.robotWalk();

            //newLocation(gui);
            if (StdDraw.isMousePressed()&&!press){
                date = new Date();
                time = date.getTime();
                press = true;
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                temp1 = new Point3D(x,y);
            }
            if (StdDraw.isMousePressed()&&press){
                date = new Date();

            }

            if (!StdDraw.isMousePressed()&&press&&date.getTime()-time>300){
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                temp2 = new Point3D(x,y);

                press = false;
                gui.update();

            }
            else if (!StdDraw.isMousePressed()&&press){
                press = false;

            }
            synchronized (gui.res){
                if (!gui.inAction()&&gui.thisMC()) {
                    gui.update();
                }

            }
        }
    }

    private boolean gameIsRan() {
        return game.isRunning();
    }

    private boolean inAction() {
        return inAction;
    }

    public  int _id = 0;
    private int mc = -1;
    game_service game;

    private DGraph dGraph;
    private double minX = 35.186, maxX =35.21, minY = 32.1, maxY = 32.11;

    public MyGameGUI(graph graph, game_service game){
        dGraph = (DGraph) graph;
        this.game = game;
        draw(1200,800,new Range(minX,maxX),new Range(minY,maxY));
        Timer timer = new Timer();
        if (!inAction)
            timer.schedule(new Active(this), 100, 1);

    }

    public void addE(int src,int dest, double weight){
        dGraph.connect(src,dest,weight);
    }

    private void draw(int width, int height, Range x, Range y){
        StdDraw.setCanvasSize(width,height);
        StdDraw.setXscale(minX,maxX);
        StdDraw.setYscale(minY,maxY);
        update();
    }

    private synchronized void update() {
        try {

            while (inAction) {

            }

            inAction = true;
            StdDraw.setPenRadius(0.01);
            Iterator<node_data> temp = dGraph.getV().iterator();
            while (temp.hasNext()) {
                node_data n = temp.next();
                maxX = Math.max(n.getLocation().x(),maxX);
                maxY = Math.max(n.getLocation().y(),maxY);
                minX = Math.min(n.getLocation().x(),minX);
                minY = Math.min(n.getLocation().y(),minY);
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
                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.point(0.1 * x0 + 0.9 * x1, 0.1 * y0 + 0.9 * y1);
                        StdDraw.setPenRadius(0.01);
                        StdDraw.setPenColor(StdDraw.BLACK);
                    }
                }

                _id = n.getKey();
                StdDraw.setXscale(minX,maxX);
                StdDraw.setYscale(minY,maxY);
            }
        }catch (Exception e){

        }
        inAction = false;
    }
    public void robotWalk() {
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.white);
        StdDraw.picture((minX+maxX)/2,(minY+maxY)/2,"Backround.png");
        List<String> log = game.getRobots();
        addFrut();
        update();
        if (log != null) {
            if (game.isRunning()) {
                long t = game.timeToEnd();
                for (int i = 0; i < log.size(); i++) {
                    String robot_json = log.get(i);
                    try {
                        JSONObject line1 = new JSONObject(robot_json);
                        System.out.println(line1);
                        JSONObject robot = line1.getJSONObject("Robot");
                        int robotId = robot.getInt("id");
                        String pos = robot.getString("pos");
                        String pos3D[] = pos.split(",");
                        Point3D position = new Point3D(Double.parseDouble(pos3D[0]), Double.parseDouble(pos3D[1]));
                        StdDraw.setPenRadius(0.04);
                        //StdDraw.setPenColor(StdDraw.GREEN);
                        double dtx = maxX - minX;
                        double dty = maxY - minY;
                        StdDraw.setPenColor(Color.red);
                        StdDraw.text(minX + dtx / 2, maxY - dty / 10, "Time left: " + t / 1000 + "." + t % 1000);
                        if (robotId == 0) {
                            StdDraw.picture(position.x(), position.y(), "robotP.png", 0.0006, 0.0006);
                        } else if (robotId == 1) {
                            StdDraw.picture(position.x(), position.y(), "robot1.jpg", 0.0004, 0.0004);
                        } else if (robotId == 2) {
                            StdDraw.picture(position.x(), position.y(), "robot2.jpg", 0.0004, 0.0004);

                        } else if (robotId == 3) {
                            StdDraw.picture(position.x(), position.y(), "robot3.jpg", 0.001, 0.0004);

                        } else if (robotId == 4) {
                            StdDraw.picture(position.x(), position.y(), "robot4.jpg", 0.0004, 0.0004);

                        }

                    } catch (Exception e) {
                        throw new RuntimeException(e);

                    }
                }
            }
        }
//            addFrut();
//            update();
            StdDraw.show();

    }
    public void addFrut(){
        Iterator<String> f_iter = game.getFruits().iterator();
        while (f_iter.hasNext()){
            try {
                JSONObject line1 = new JSONObject(f_iter.next());
                System.out.println(line1);
                JSONObject frut = line1.getJSONObject("Fruit");
                double value = frut.getDouble("value");
                String pos = frut.getString("pos");
                int type = frut.getInt("type");
                String pos3D[] = pos.split(",");
                Point3D position = new Point3D(Double.parseDouble(pos3D[0]),Double.parseDouble(pos3D[1]));
                if (type==-1) {
                    StdDraw.picture(position.x(), position.y(), "robotH.png", 0.001, 0.001);
                }
                else{
                    StdDraw.picture(position.x(), position.y(), "fruitH.png", 0.001, 0.001);

                }

            }catch (Exception e){
                throw new RuntimeException(e);

            }
        }
    }



    public boolean thisMC(){
        if (dGraph.getMC()==mc) return false;
        mc = dGraph.getMC();
        return  true;
    }




}
