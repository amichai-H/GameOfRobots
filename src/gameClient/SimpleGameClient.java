package gameClient;

import java.sql.Time;
import java.util.*;

import Arena.Fruit;
import Arena.Fruits;
import Arena.Robot;
import Arena.Robots;
import algorithms.Graph_Algo;
import dataStructure.*;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
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

	public static void main(String[] a) {
		test1();
	}

	public static void test1() {
		JFrame f = new JFrame();
		int aout = JOptionPane.showConfirmDialog(f,"Do you want auto game?");
		System.out.println(aout);;
		auto = aout==0;
		int scenario_num = Integer.parseInt(JOptionPane.showInputDialog(f, "Enter game 0-23 "));

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
			System.out.println(info);
			//System.out.println(g);
			// the list of fruits should be considered in your solution
			Iterator<String> f_iter = game.getFruits().iterator();
			while (f_iter.hasNext()) {
				System.out.println(f_iter.next());
			}
			System.out.println(rs);
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
				}
				else {
					Robots rr = new Robots(game,gg);
					Fruits ff = new Fruits(game,gg);

					src_node=findStart(gg,game)+a;
				}
				game.addRobot(src_node);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		game.startGame();
		// should be a Thread!!!
		try {

			while (game.isRunning()) {
				moveRobots(game, gg, myg);
			}
		}catch (Exception e){
			while (game.isRunning()) {
				moveRobots(game, gg, myg);
			}
		}
		String results = game.toString();
		JOptionPane.showMessageDialog(f, "Game Over: " + results);

		System.out.println("Game Over: " + results);
		exit(1);
	}

	private static int findStart(DGraph gg, game_service game) {
		Graph_Algo graph_algo = new Graph_Algo(gg);
		Fruits fruts = new Fruits(game,gg);
		Fruit max = fruts.getMaxValue();
		edge_data eData = fruts.getEdge(max.getId());
		if (eData!=null) {
			if (max.getType() == -1) {
				return eData.getSrc();
			} else
				return eData.getDest();

		}else return 0;
	}

	/**
	 * Moves each of the robots along the edge,
	 * in case the robot is on a node the next destination (next edge) is chosen (randomly).
	 *
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveRobots(game_service game, graph gg, MyGameGUI myGameGUI) {
		List<String> log = game.move();
		if (log != null) {
			//long t = game.timeToEnd();
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
						dest = nextNode(gg, src, rid,game);
						int r = (int)(Math.random()*10)+1;
						System.out.println("hfgdftghfdhjkjhgfdghjkfghd45ui"+dest);
						if (dest== -1|| r==2||speed>3){
							dest = goCloser(gg, src, rid,game);
							System.out.println("hfgdftghfdhjkjhgfdghjkfghd45ui"+dest);
						}
						if (dest == -1){
							dest = randomedge(gg, src,rid);
						}
						if (dest==-1){
							dest = randomedge(gg, src,rid);
						}
						game.chooseNextEdge(rid, dest);
						//System.out.println("Turn to node: " + dest + "  time to end:" + (t / 1000));
						System.out.println(ttt);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static int goCloser(graph g, int src, int rid, game_service game) {
		Graph_Algo graph_algo = new Graph_Algo(g);
		Fruits fruts = new Fruits(game,g);
		Robots robots = new Robots(game,g);
		Robot r = robots.getRobot(rid);
		Fruit fruit = fruts.getCloseF(r.getLocation());
		edge_data edgedata = fruts.getEdge(fruit.getId());
		if (edgedata==null){
			return -1;
		}
		if (edgedata.getDest()==src)
			return edgedata.getSrc();
		if (edgedata.getSrc()==src)
			return edgedata.getDest();
		if (fruit.getType() == -1) {

			return graph_algo.shortestPath(src, edgedata.getDest()).get(1).getKey();
		} else return graph_algo.shortestPath(src, edgedata.getSrc()).get(1).getKey();

	}

	private static int randomedge(graph g, int src,int rid) {
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
		while (temp.hasNext()){
			showChoices.add(temp.next().getKey());
		}
		Collection<node_data> ev = g.getV();
			myway[rid] = new ArrayDeque<>();
			int r = (int)(Math.random()*ev.size());
			Iterator<node_data> temp2 = graph_algo.shortestPath(src,showChoices.get(r)).iterator();
			while (temp2.hasNext()) {
				myway[rid].add(temp2.next().getKey());
		}
		//
		if (myway[rid]==null||myway[rid].isEmpty()){
			return -1;
		}
		ans = myway[rid].poll();
		try {
			return graph_algo.shortestPath(src,showChoices.get(r)).get(1).getKey();
		}
		catch (Exception e){
			return -1;
		}
	}

	/**
	 * a very simple random walk implementation!
	 *
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(graph g, int src, int rid,game_service game) {
		if (!auto) {
			if (StdDraw.isMousePressed()) {
				double x = StdDraw.mouseX();
				double y = StdDraw.mouseY();
				src = findsrc(x, y, g);
				return src;
			}
			return -1;
		}
		else {
			return findNextNode(g,game,src);
		}
	}

	private static int findNextNode(graph g, game_service game,int src) {
		Graph_Algo graph_algo = new Graph_Algo(g);
		Fruits fruts = new Fruits(game,g);
		//Robots robots = new Robots(game,g);
		//Robot current = robots.getRobot(rid);
		Fruit max = fruts.getMaxValue();
		edge_data eData = fruts.getEdge(max.getId());
		if (eData!=null) {
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

	private static int findsrc(double x, double y,graph g) {
		Iterator<node_data> allnode =g.getV().iterator();
		while (allnode.hasNext()){
			node_data temp = allnode.next();
			Point3D lTemp = temp.getLocation();
			if (Math.abs(lTemp.x()-x)<0.0003&&Math.abs(lTemp.y()-y)<0.0003)
				return temp.getKey();
		}
		return -1;
	}

}

//
//package gameClient;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Comparator;
//import java.util.Iterator;
//import java.util.List;
//import java.util.PriorityQueue;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import dataStructure.*;
//import Server.Game_Server;
//import Server.game_service;
//import algorithms.Graph_Algo;
//import algorithms.graph_algorithms;
//
//import javax.swing.*;
//
//import static java.lang.System.exit;
//
//
///**
// * This class represents a simple example for using the GameServer API:
// * the main file performs the following tasks:
// * 1. Creates a game_service [0,23] (line 36)
// * 2. Constructs the graph from JSON String (lines 37-39)
// * 3. Gets the scenario JSON String (lines 40-41)
// * 4. Prints the fruits data (lines 44-45)
// * 5. Add a single robot (line 48) // note: in genera a list of robots should be added
// * 6. Starts game (line 49)
// * 7. Main loop (should be a thread)
// * 8. move the robot along the current edge (line 54)
// * 9. direct to the next edge (if on a node) (line 68)
// *
// * @author boaz.benmoshe
// *
// */
//public class SimpleGameClient {
//	public static void main(String[] a) {
////		game_service game = Game_Server.getServer(1); // you have [0,23] games
////		String g = game.getGraph();
////		DGraph gg = new DGraph();
////		gg.init(g);
//
//		test1();
//	}
//	public static void test1() {
//		game_service game = Game_Server.getServer(1); // you have [0,23] games
//		String g = game.getGraph();
//		DGraph gg = new DGraph();
//		MyGameGUI myg = new MyGameGUI(gg, game);
//		gg.init(g);
//		String info = game.toString();
//		System.out.println(info);
//		System.out.println(g);
//		// the list of fruits should be considered in your solution
//		Iterator<String> f_iter = game.getFruits().iterator();
//		while(f_iter.hasNext()) {System.out.println(f_iter.next());}
//		// arbitrary node, you should start at one of the fruits
//		LocateRobots(game,gg);
//		System.out.println(game.getRobots().size());
//		game.startGame();
//		while(game.isRunning()) {
//			long t = game.timeToEnd();
//			//System.out.println("round: "+i+"  seconds to end:"+(t/1000));
//			List<String> log = game.move();
//			int i=0;
//			while(i<log.size()) {
//				String robot_json = log.get(i);
//				//	System.out.println(robot_json);
//				JSONObject line;
//
//				try {
//					line = new JSONObject(robot_json);
//					JSONObject ttt = line.getJSONObject("Robot");
//					int rid = ttt.getInt("id");
//					int src = ttt.getInt("src");
//					int dest = ttt.getInt("dest");
//
//					if(dest==-1) {
//						dest = nextNode(gg, src);
//						game.chooseNextEdge(rid, dest);
//						System.out.println("Turn to node: "+dest);
//						System.out.println(ttt);
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				i++;
//			}
//		}
//		String results = game.toString();
//		JFrame f =new JFrame();
//		JOptionPane.showMessageDialog(f, "Game Over: " + results);
//
//		System.out.println("Game Over: " + results);
//		exit(1);
//	}
//	/**
//	 * a very simple random walk implementation!
//	 * @param g
//	 * @param src
//	 * @return
//	 */
//	private static int nextNode(graph g, int src) {
//		int ans = -1;
//		Collection<edge_data> ee = g.getE(src);
//		Iterator<edge_data> itr = ee.iterator();
//		int s = ee.size();
//		int r = (int)(Math.random()*s);
//		int i=0;
//		while(i<r) {itr.next();i++;}
//		ans = itr.next().getDest();
//		return ans;
//	}
//
//	public static void LocateRobots(game_service game,graph g) {
//		graph_algorithms MyGG=new Graph_Algo(g);
//		int Rsize=getRobots(game.toString());
//		int Fsize=game.getFruits().size();
//		Collection<Integer> FruitsLocation=FindFruitNode(game,g);
//		Object[] arr=FruitsLocation.toArray();
//		int i = 0;
//		if(Rsize<Fsize) {
//			arr=FindShortestPath(FruitsLocation,Rsize,Fsize,MyGG,arr);
//			for (i=0; i <Rsize; i++) {
//				game.addRobot((int)arr[i]);
//			}
//		}
//		else
//			while(i<Rsize) {
//				game.addRobot((int)arr[i]);
//				i++;
//			}
//	}
//
//	public static Collection<Integer> FindFruitNode(game_service game,graph g) {
//		Iterator<String> it =game.getFruits().iterator();
//		Collection<Integer> Ans=new ArrayList<>();
//		Collection<edge_data> MyNodes=new ArrayList<>();
//		double PorN = 0;
//		while(it.hasNext()) {
//			String temp=it.next();
//			temp=getPoints(temp);
//			MyNodes=(InRange(temp,g.getV(),g));
//			Object[] List=MyNodes.toArray();
//			for (int i = 0; i < List.length; i++) {
//				edge_data e=(edge_data)List[i];
//				if(it.hasNext())
//					PorN=getSign(it.next());
//				if(PorN==-1)
//					Ans.add(Math.min(e.getSrc(),e.getDest()));
//				else
//					Ans.add(Math.max(e.getSrc(),e.getDest()));
//			}
//		}
//		return Ans;
//	}
//
//	private static Collection<edge_data> InRange(String temp, Collection<node_data> v,graph g) {
//		Collection<node_data> MyTargetNodes=g.getV();
//		Collection<edge_data> NodesInRange=new ArrayList<edge_data>();
//		for (node_data n : MyTargetNodes) {
//			for (node_data t : MyTargetNodes) {
//				if(t.equals(n))
//					continue;
//				if(isBetweenPoints(temp,n.getLocation().x(),n.getLocation().y(),t.getLocation().x(),t.getLocation().y()) && !NodesInRange.contains(g.getEdge(n.getKey(),t.getKey())) && g.getEdge(n.getKey(),t.getKey())!=null) {
//					NodesInRange.add(g.getEdge(n.getKey(),t.getKey()));
//				}
//			}
//		}
//		return NodesInRange;
//	}
//
//	private static boolean isBetweenPoints(String temp, double x, double y, double x2, double y2) {
//		int i=0;
//		double Eps=DistBetween2Points(x,y,x2,y2)/10;
//		while(i<temp.length()) {
//			if(temp.charAt(i)==',') {
//				break;
//			}
//			i++;
//		}
//		String Tx=temp.substring(0,i);
//		double tx=Double.parseDouble(Tx);
//		String Ty=temp.substring(i+1,temp.length());
//		double ty=Double.parseDouble(Ty);
//		if(tx>Math.max(x,x2) || tx<Math.min(x,x2) || ty>Math.max(y,y2) || ty<Math.min(y,y2))
//			return false;
//		if(Math.abs((DistBetween2Points(tx,ty,x,y)+DistBetween2Points(tx,ty,x2,y2))-DistBetween2Points(x,y,x2,y2))>Eps)
//			return false;
//
//		return true;
//	}
//
//	private static double DistBetween2Points(double x1,double y1,double x2,double y2) {
//		return Math.sqrt(Math.pow((y2-y1),2) + (Math.pow((x2 - x1),2)));
//	}
//
//	private static String getPoints(String str) {
//		int i=7,s=0,e=str.length()-5,count=0;
//		boolean flag=false;
//		while(i<str.length()) {
//			if(str.charAt(i-2)=='p'&& str.charAt(i-1)=='o' && !flag) {
//				flag=true;
//			}
//			if(flag && str.charAt(i-2)==':') {
//				s=i;
//			}
//			if(str.charAt(i)==',') {
//				count++;
//			}
//			if(count==4) {
//				e=i;
//				break;
//			}
//			i++;
//		}
//		return str.substring(s,e);
//	}
//
//	private static double getSign(String next) {
//		int i=0;
//		boolean flag=false;
//		String str=new String(next);
//		while(i<str.length()) {
//			i++;
//			if(str.substring(i,i+4).equals("type") && !flag) {
//				flag=true;
//				i=i+4;
//			}
//			if(str.substring(i,i+2).equals(":1") && flag)
//				return 1;
//			if(str.substring(i,i+2).equals("-1") && flag)
//				return -1;
//		}
//		return -1;
//	}
//
//	private static int getRobots(String info) {
//		int i=0;
//		boolean flag=false;
//		String str=new String(info);
//		while(i<str.length()) {
//			i++;
//			if(str.charAt(i)=='r' && str.substring(i,i+6).equals("robots") && !flag) {
//				flag=true;
//				i=i+5;
//			}
//			if(str.substring(i,i+1).equals(":") && flag)
//				return Integer.parseInt(str.substring(i+1,i+2));
//		}
//		return 0;
//	}
//
//	private static Object[] FindShortestPath(Collection<Integer> fruitsLocation, int rsize, int fsize, graph_algorithms myGG, Object[] arr) {
//		double RandF_Balance=fsize/rsize;
//		Object[] ans=new Object[(int)RandF_Balance];
//		PriorityQueue<Double> minHeap = new PriorityQueue<Double>();
//		int j = 0;
//		while(j < arr.length-1) {
//			minHeap.add(myGG.shortestPathDist((int)arr[j],(int)arr[j+1]));
//			j++;
//		}
//
//		if((int)RandF_Balance==1) {
//			j = 0;
//			double f=minHeap.poll();
//			while(j < arr.length-1) {
//				double path=myGG.shortestPathDist((int)arr[j],(int)arr[j+1]);
//				if(path==f) {
//					ans[0]=j;
//					break;
//				}
//				j++;
//			}
//		}
//
//		if((int)RandF_Balance==2) {
//			j = 0;
//			double f=minHeap.poll(),s=minHeap.poll();
//			while(j < arr.length-1) {
//				double path=myGG.shortestPathDist((int)arr[j],(int)arr[j+1]);
//				if(path==f) {
//					ans[0]=j;
//				}
//				if(path==s) {
//					ans[1]=j;
//					break;
//				}
//				j++;
//			}
//		}
//
//		if((int)RandF_Balance==3) {
//			j = 0;
//			double f=minHeap.poll(),s=minHeap.poll(),th=minHeap.poll();
//			while(j < arr.length-1) {
//				double path=myGG.shortestPathDist((int)arr[j],(int)arr[j+1]);
//				if(path==f) {
//					ans[0]=j;
//				}
//				if(path==s) {
//					ans[1]=j;
//				}
//				if(path==th) {
//					ans[2]=j;
//					break;
//				}
//				j++;
//			}
//		}
//
//		if((int)RandF_Balance==4) {
//			j = 0;
//			double f=minHeap.poll(),s=minHeap.poll(),th=minHeap.poll(),fo=minHeap.poll();
//			while(j < arr.length-1) {
//				double path=myGG.shortestPathDist((int)arr[j],(int)arr[j+1]);
//				if(path==f) {
//					ans[0]=j;
//				}
//				if(path==s) {
//					ans[1]=j;
//				}
//				if(path==th) {
//					ans[2]=j;
//				}
//				if(path==fo) {
//					ans[3]=j;
//					break;
//				}
//				j++;
//			}
//		}
//		return ans;
//	}
//
//}