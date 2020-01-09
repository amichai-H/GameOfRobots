package gameClient;

import java.util.*;

import algorithms.Graph_Algo;
import dataStructure.*;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;

import javax.swing.*;

import static java.lang.System.exit;

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

	public static void main(String[] a) {
		test1();
	}

	public static void test1() {
		JFrame f = new JFrame();
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
			System.out.println(g);
			// the list of fruits should be considered in your solution
			Iterator<String> f_iter = game.getFruits().iterator();
			while (f_iter.hasNext()) {
				System.out.println(f_iter.next());
			}

			for (int a = 0; a < rs; a++) {
				int src_node = Integer.parseInt(JOptionPane.showInputDialog(f, "Where to start? "));
				game.addRobot(src_node);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		game.startGame();
		// should be a Thread!!!
		while (game.isRunning()) {
			moveRobots(game, gg, myg);
		}
		String results = game.toString();
		JOptionPane.showMessageDialog(f,"Game Over: " + results);

		System.out.println("Game Over: " + results);
		exit(1);
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
			long t = game.timeToEnd();
			for (int i = 0; i < log.size(); i++) {
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");

					if (dest == -1) {
						dest = nextNode(gg, src, rid);
						game.chooseNextEdge(rid, dest);
						System.out.println("Turn to node: " + dest + "  time to end:" + (t / 1000));
						System.out.println(ttt);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * a very simple random walk implementation!
	 *
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(graph g, int src, int rid) {
		int ans = -1;
		Graph_Algo graph_algo = new Graph_Algo(g);
		Collection<edge_data> ee = g.getE(src);

		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		boolean notSucced = true;
			Iterator<node_data> temp = g.getV().iterator();
			ArrayList<Integer> showChoices = new ArrayList<>();
			while (temp.hasNext()){
				showChoices.add(temp.next().getKey());
			}
			Collection<node_data> ev = g.getV();
			JFrame f = new JFrame();
			if (myway[rid] == null||myway[rid].isEmpty()) {
				myway[rid] = new ArrayDeque<>();
				int r = Integer.parseInt(JOptionPane.showInputDialog(f,"where to go?"));
				Iterator<node_data> temp2 = graph_algo.shortestPath(src,r).iterator();
				while (temp2.hasNext()) {
					myway[rid].add(temp2.next().getKey());
				}
			}
//
			ans = myway[rid].poll();
		return ans;
	}

}
