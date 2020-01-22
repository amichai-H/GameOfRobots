package gameClient;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import Arena.Fruit;
import Arena.Fruits;
import Arena.Robot;
import Arena.Robots;
import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.*;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import utils.KmlForGame;
import utils.Point3D;
import utils.Range;
import utils.StdDraw;

import javax.swing.*;

import static java.lang.System.*;

/**
 * This class represents a simple example for using the GameServer API:
 * the main file performs the following tasks:
 * 1. Creates a game_service [0,23] (line 36)
 * 2. Constructs the graph from JSON String (lines 37-39)
 * 3. Gets the scenario JSON String (lines 40-41)
 * 4. Prints the fruits data (lines 49-50)
 * 5. Add a set of robots (line 52-53) // note: in general a list of robots should be added
 * 6. Starts game (line 57)
 * 7. Main loop (should be a thread) (lines 59-60)
 * 8. move the robot along the current edge (line 74)
 * 9. direct to the next edge (if on a node) (line 87-88)
 * 10. prints the game results (after "game over"): (line 63)
 *
 * @author boaz.benmoshe
 *
 */
public class SimpleGameClient {
	private static Queue<Integer>[] myway = new ArrayDeque[5];
	private static boolean auto = false;
	private static HashMap<Double,Integer> myFruitShare = new HashMap<>();
	static long time;
	private static int scenario_num = -1;


	public static void main(String[] a)
	{
		theGame();
	}

	/**
	 * That is the main thing. When everything connected.
	 * The heart and the brain of the system.
	 */
	private static void theGame() {
		JFrame f = new JFrame();
        int id = 315149500;
        Game_Server.login(id);
		KmlForGame kmlForGame = new KmlForGame();
		try {
			auto =  JOptionPane.showConfirmDialog(f, "Do you want auto game?", "Start Game",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
		} catch (Exception e){
			exit(1);
		}
		while (!(scenario_num <=23&&scenario_num>=0)) {
			try {
				scenario_num = Integer.parseInt(JOptionPane.showInputDialog(f, "Pls select level between 0-23 "));

			} catch (Exception e){

				exit(1);
			}
        }
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		String g = game.getGraph();
		DGraph gg = new DGraph();
		MyGameGUI myg = new MyGameGUI(gg, game);
		gg.init(g);
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			if(!auto)
				JOptionPane.showMessageDialog(f,"The game is about to start.\n" +
						"Pls select where to put the robots by clicking on node.\n" +
						"You have "+ rs+ " robots.");
			for (int a = 0; a < rs; a++) {
				int src_node = -1;
				if (!auto) {
					while (src_node == -1) {
						if (StdDraw.isMousePressed()) {
							double x = StdDraw.mouseX();
							double y = StdDraw.mouseY();
							src_node = findsrc(x, y, gg);
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
						//do stuff
					}
				} else {
					src_node = findStart(gg, game) + a+1;
					if (a==1&& scenario_num == 13)
						src_node = 40;
					if (scenario_num==16||scenario_num==23) {
						if (a == 0) {
							src_node = 10;
						}
						else
							src_node = 40;
					}
					if (scenario_num == 23){
						if (a==0){
							src_node =  13;
						}
						else if(a==1){
							src_node = 19;
						}
						else {
							src_node = 40;
						}
					}
//					if (scenario_num==20) {
//						if (a == 1) {
//							src_node = 10;
//						}
//						else if(a==2)
//							src_node = 15;
//						else if(a==0)
//							src_node = 19;
//					}

				}
				game.addRobot(src_node);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			kmlForGame.addGraph(gg);
			if (!auto)
				JOptionPane.showMessageDialog(f,"START GAME");
			game.startGame();
			Long l = game.timeToEnd();
			try {
				time = game.timeToEnd();
				Fruits fruits = new Fruits(game, gg);

				while (game.isRunning()) {
					if (l-game.timeToEnd()>52L) {
                        kmlForGame.writeMyRnF(gg, game);
                        l = game.timeToEnd();

                    moveRobots(game, gg, myg, fruits);
                    }


                }
			} catch (Exception e) {
				Fruits fruits = new Fruits(game, gg);
				while (game.isRunning()) {
					moveRobots(game, gg, myg, fruits);
				}
			}
		} catch (Exception ignored) {
		}
		String results = game.toString();
		System.out.println("Game Over: " + results);
		try {
			JSONObject endGame;
			endGame = new JSONObject(results);
			JSONObject ttt = endGame.getJSONObject("GameServer");
			int rs = ttt.getInt("grade");
			int moves = ttt.getInt("moves");
			JOptionPane.showMessageDialog(f, "Game Over \n" +
					"Game scenario num: "+scenario_num+"\n"+
					"your grade is:     " + rs+"\n" +
					"amount of moves:   "+ moves);

		int save = JOptionPane.showConfirmDialog(f, "Do you want to save this game in kml?\n" +
				"your score was: "+rs+"\n" +
				"Game scenario num: "+scenario_num);
			if (save == 0) {

				String filename = JOptionPane.showInputDialog(f, "Enter name to file save game REMEMBER: grade was: "+ rs+"\n" +
						"Game scenario num: "+scenario_num);
				kmlForGame.saveToFile(filename);
				game.sendKML(kmlForGame.getLgerOfGame()+"");
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//exit(0);
	}

	/**
	 *
	 * @param gg -  graph of the game "map"
	 * @param game - the server game that running
	 * @return src to start on the graph
	 */
	private static int findStart(DGraph gg, game_service game) {
		//Graph_Algo graph_algo = new Graph_Algo(gg);
		Fruits fruts = new Fruits(game, gg);
		Fruit max = fruts.getMaxValue();
		edge_data eData = fruts.getEdge(max.getId());
		if (eData != null) {
			if (max.getType() == -1) {
				return eData.getSrc();
			} else
				return eData.getDest();

		}
		System.out.println("PROBLEM");
		return 0;
	}

	/**
	 * Moves each of the robots along the edge,
	 * in case the robot is on a node the next destination (next edge) is chosen.
	 * Not randomly.
	 * We have AI (Kind Of) that separate the fruit between the robot and strategy to each one Robot.
	 *
	 * @param game - server
	 * @param gg - Map (graph)
	 */
	private static void moveRobots(game_service game, graph gg, MyGameGUI myGameGUI,Fruits fruits) {
		List<String> log = game.move();
		if (log != null) {
			long t = game.timeToEnd();
			for (int i = 0; i < log.size(); i++) {

				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					int speed = ttt.getInt("speed");

					if (dest == -1) {
						if (auto) {
							fruits =  new Fruits(game,gg);
							Robots list = new Robots(game,gg);
							if (rid==0) {
								boolean b = scenario_num ==19;
								//dest = goCloser(gg, src, rid, game,fruits,b);
//								if (list.collection().size()<3 && speed<4){
								dest = nextNode(gg, src, fruits);

//								}
//								else if (speed==1){
//									dest = getFF(gg,game,rid,src,fruits,1);
//								}
//								if (speed>=4){
////									dest = goCloser(gg, src, rid, game,fruits,true);
////									dest = getFF(gg,game,rid,src,fruits,0);
//									dest = nextNode(gg, src, fruits);
////									if (list.zise()) {
////										dest = nextNode(gg, src, rid, game, fruits);
////									}
//									}
//								}
								if (scenario_num == 23){
									dest = getAtAREA(gg,0,13,src,fruits);


								}
								if (speed>3&&scenario_num!=23){
									dest = getMinf(gg, src,fruits,3);

								}
							}

							if (rid==1) {
								if (scenario_num!=22&&scenario_num!=23&&scenario_num!=16) {
//								dest = getMinf(gg, src, rid, game,fruits);
//								dest = getFF(gg,game,rid,src,fruits,4);
//									dest = nextNode(gg, src, fruits);
								}
								else if (scenario_num==22||scenario_num==16||scenario_num==23) {
//									dest = goCloser(gg, src, rid, game, fruits, true);
									//dest = nextNode(gg, src, fruits);
									dest = getAtAREA(gg,16,32,src,fruits);
									if (game.timeToEnd()<5000){
										dest = getFF(gg,src,fruits,1);
									}




//								dest = goCloser(gg, src, rid, game,fruits,true);
//									dest = getFF(gg,src,fruits,1);
								}
                                else {
                                    dest = getFF(gg,src,fruits,3);

                                }

							}
//							if ((dest == -1 ||speed > 3)&&log.size()!=1){
//
//								dest = nextNode(gg, src, rid, game,fruits);
//							}
							if ((dest == -1 && rid != 2 )) {
//								dest = nextNode(gg, src, fruits);
								dest = getMinf(gg, src,fruits,2);

//								dest = goCloser(gg, src, rid, game,fruits,true);

							}
							if (dest==-1){
								//dest = goCloser(gg, src, rid, game,fruits,false);
//								dest = getMinf(gg, src,fruits);
								//dest = getFF(gg,src,fruits,2);
//								dest = getMinf(gg, src,fruits,5);
								dest = getAtAREA(gg,23,50,src,fruits);




								if (speed>2&& scenario_num != 23)
								dest = getMinf(gg, src,fruits,0);
//								dest = goCloser(gg, src, rid, game,fruits,true);


							}


							if (dest == -1) {
								//dest = randomedge(gg, src, rid);
							}
						} else {
							dest = nextNode(gg, src,null);
						}
						game.chooseNextEdge(rid, dest);
						//System.out.println("Turn to node: " + dest + "  time to end:" + (t / 1000));
						//System.out.println(ttt);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * return the src the the minimum fruit is on
	 * @param g - graph of the game "Map"
	 * @param src - where is the robot now
	 * @param fruts - all the fruits currently
	 * @return next node in int(id)
	 */

	private static int getMinf(graph g, int src,Fruits fruts,int fromEND) {
		Graph_Algo graph_algo = new Graph_Algo(g);
		Fruit fruit = fruts.geMinValue(fromEND);
			edge_data edgedata = fruts.getEdge(fruit.getId());

			if (edgedata == null) {
				return -1;
			}
			if (edgedata.getDest() == src)
				return edgedata.getSrc();
			if (edgedata.getSrc() == src)
				return edgedata.getDest();
			if (fruit.getType() == -1) {

				return graph_algo.shortestPath(src, edgedata.getDest()).get(1).getKey();
			} else return graph_algo.shortestPath(src, edgedata.getSrc()).get(1).getKey();
	}

	private static int goCloser(graph g, int src, int rid, game_service game,Fruits fruts,boolean spd) {
		Graph_Algo graph_algo = new Graph_Algo(g);
		Robots robots = new Robots(game, g);
		Robot r = robots.getRobot(rid);
		Fruit fruit = fruts.getCloseF(src,spd);

			edge_data edgedata = fruts.getEdge(fruit.getId());
			if (edgedata == null) {
				return -1;
			}
			if (edgedata.getDest() == src)
				return edgedata.getSrc();
			if (edgedata.getSrc() == src)
				return edgedata.getDest();
			if (fruit.getType() == -1) {

				return graph_algo.shortestPath(src, edgedata.getDest()).get(1).getKey();
			} else return graph_algo.shortestPath(src, edgedata.getSrc()).get(1).getKey();

	}

	/**
	 *
	 * @param g - Map (graph)
	 * @param src - where the robot is now
	 * @param rid - robot id
	 * @return next node in id
	 */

	private static int randomedge(graph g, int src, int rid) {
//		int ans = -1;
//		Collection<edge_data> ee = g.getE(src);
//		Iterator<edge_data> itr = ee.iterator();
//		int s = ee.size();
//		int r = (int)(Math.random()*s);
//		int i=0;
//		while(i<r) {itr.next();i++;}
//		ans = itr.next().getDest();
//		return ans;
		int ans = -1;
		Graph_Algo graph_algo = new Graph_Algo(g);

		Iterator<node_data> temp = g.getV().iterator();
		ArrayList<Integer> showChoices = new ArrayList<>();
		while (temp.hasNext()) {
			showChoices.add(temp.next().getKey());
		}
		Collection<node_data> ev = g.getV();
		myway[rid] = new ArrayDeque<>();
		int r = (int) (Math.random() * ev.size());
		Iterator<node_data> temp2 = graph_algo.shortestPath(src, showChoices.get(r)).iterator();
		while (temp2.hasNext()) {
			myway[rid].add(temp2.next().getKey());
		}
		//
		if (myway[rid] == null || myway[rid].isEmpty()) {
			return -1;
		}
		ans = myway[rid].poll();
		try {
			return graph_algo.shortestPath(src, showChoices.get(r)).get(1).getKey();
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * a very simple choose next edge
	 * if this is manual game - we only find next node by x,y of the mouse
	 * else  so we need to active the AI that we crated
	 *
	 * @param g - Map (graph)
	 * @param src - where the robot is now
	 * @return next node in id
	 */
	private static int nextNode(graph g, int src,Fruits fruits) {
		if (!auto) {
			if (StdDraw.isMousePressed()) {
				double x = StdDraw.mouseX();
				double y = StdDraw.mouseY();
				src = findsrc(x, y, g);
				return src;
			}
			return -1;
		} else {
			return findNextNode(g, src,fruits);
		}
	}

	/**
	 * this method uses the Ai in Fruits and solve bugs that the robot stay in place
	 * @param g - graph of the game "Map"
	 * @param src - where is the robot now
	 * @param fruts - all the fruits currently
	 * @return id of the next node
	 */
	private static int findNextNode(graph g,int src,Fruits fruts) {
		Graph_Algo graph_algo = new Graph_Algo(g);
		//Robots robots = new Robots(game,g);
		//Robot current = robots.getRobot(rid);
		Fruit max = fruts.getMaxValue();
		edge_data eData = fruts.getEdge(max.getId());
			if (eData != null) {
				if (eData.getDest() == src) {
					return eData.getSrc();
				}
				if (eData.getSrc() == src) {
					return eData.getDest();
				}
				if (max.getType() == -1) {

					return graph_algo.shortestPath(src, eData.getDest()).get(1).getKey();
				} else return graph_algo.shortestPath(src, eData.getSrc()).get(1).getKey();
			}
			return -1;
	}

	/**
	 * where is the user click on the screen
	 * @param x - mouse x
	 * @param y - mouse y
	 * @param g - Map (graph)
	 * @return id of the node
	 */

	private static int findsrc(double x, double y, graph g) {
		for (node_data temp : g.getV()) {
			Point3D lTemp = temp.getLocation();
			if (Math.abs(lTemp.x() - x) < 0.0003 && Math.abs(lTemp.y() - y) < 0.0003)
				return temp.getKey();
		}
		return -1;
	}

	/**
	 * one of the nice method tha help to separate the fruits
	 * but be careful because there is no always a lot of fruits
	 *
	 * @param g - Map (graph)
	 * @param src - where is the robot now
	 * @param fruts - all the fruits currently
	 * @param id - of the fruit
	 * @return id of the next node
	 */
	private static int getFF(graph g,int src,Fruits fruts,int id){
		Graph_Algo algo =new Graph_Algo(g);
		edge_data dde = fruts.getEdge(id);
		if (dde!=null)
		if (dde.getDest()==src)
			return dde.getSrc();
		else if (dde.getSrc()==src){
			return dde.getDest();

		}
		else return algo.shortestPath(src,dde.getDest()).get(1).getKey();
		return -1;

	}
	private static int getAtAREA(graph g,int rX,int rY,int src,Fruits fruits){
		for (Iterator<Fruit> it = fruits.iterator(); it.hasNext(); ) {
			Fruit fruit = it.next();
			 if (fruit.inRange(rX,rY)) {
				 return getFF(g, src, fruits, fruit.getId());
			 }
			 if (fruit.getMyEdge().getDest()==21||fruit.getMyEdge().getSrc()==21){

			 }

		}
		return -1;
	}

}
