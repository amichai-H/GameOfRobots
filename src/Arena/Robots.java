package Arena;

import Server.game_service;
import dataStructure.graph;
import dataStructure.node_data;
import org.json.JSONObject;
import utils.Point3D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Robots {
    ArrayList<Robot> myRobots = new ArrayList<>();
    graph graph;

    /**
     * init All the Robots
     * @param game - rhe game after start
     * @param g - the graph of the game
     */
    public Robots(game_service game, graph g) {
        graph = g;
        List<String> log = game.getRobots();
        if (log != null) {
            for (int i = 0; i < log.size(); i++) {
                String robot_json = log.get(i);
                try {
                    JSONObject line1 = new JSONObject(robot_json);
                    JSONObject robot = line1.getJSONObject("Robot");
                    int robotId = robot.getInt("id");
                    String pos = robot.getString("pos");
                    String pos3D[] = pos.split(",");
                    Point3D position = new Point3D(Double.parseDouble(pos3D[0]), Double.parseDouble(pos3D[1]));
                    Robot temp = new Robot(robotId,position);
                    myRobots.add(temp);
                } catch (Exception e){

                }
            }
        }
    }

    /**
     * regular Iterator
     * @return Iterator of the robot
     */

    public Iterator<Robot> iterator(){
        return myRobots.iterator();
    }

    /**
     *
     * @return Collection of the Robots
     */
    public Collection<Robot> collection(){
        return myRobots;
    }

    /**
     * return robot by ID
     * @param id - id of the robot
     * @return Robot
     */
    public Robot getRobot(int id){
        Iterator<Robot> robotIterator = iterator();
        while (robotIterator.hasNext()){
            Robot temp = robotIterator.next();
            if (id==temp.getId()){
                return temp;
            }
        }
        return null;
    }

    /**
     *
     * @return amount of Robot in int
     */
    public int zise() {
        return myRobots.size();
    }
}
