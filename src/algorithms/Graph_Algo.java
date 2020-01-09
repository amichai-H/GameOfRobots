package algorithms;

import dataStructure.*;

import java.io.*;
import java.util.*;

/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author
 *
 */
public class Graph_Algo implements graph_algorithms, Serializable {
	public graph myGraph;
	public boolean Ok = true;

	/**
	 * init
	 * @param graph - the graph for the algorithms.
	 */
	public Graph_Algo(graph graph) {
		myGraph = graph;
	}
	public Graph_Algo() {
	}

	/**
	 * init
	 * @param g - the graph for the algorithms.
	 */
	@Override
	public void init(graph g) {
		myGraph =new DGraph(g);
	}

	/**
	 * init from file
	 * @param file_name - include path to the file
	 */
	@Override
	public  void init(String file_name) {
		try {
			FileInputStream file = new FileInputStream(file_name);
			ObjectInputStream objectInputStream = new ObjectInputStream(file);
			graph g = (graph) objectInputStream.readObject();
			init(g);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * save to file
	 * @param file_name - the file name that will be save in
	 */
	@Override
	public void save(String file_name) {
		try {
			FileOutputStream file = new FileOutputStream(file_name);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
			objectOutputStream.writeObject(this.myGraph);
			objectOutputStream.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Returns true if and only if (iff) there is a valid path from EVREY node to each
	 * other node. NOTE: assume directional graph - a valid path (a-->b) does NOT imply a valid path (b-->a).
	 * @return true if the graph is connected and not if not
	 */
	@Override
	public boolean isConnected() {
		resetTag();
		if (myGraph.nodeSize() == 0|| myGraph.nodeSize()==1) return true;
		Collection<node_data> temp = myGraph.getV();
		Iterator<node_data> nodeIter = temp.iterator();
		while (nodeIter.hasNext()) {
			nodeIter.next().setTag(-1);
		}
		boolean FLAG = true;
		Queue<node_data> myQue = new LinkedList<>();
		Queue<node_data> finish = new LinkedList<>();
		node_data current;
		nodeIter = myGraph.getV().iterator();
		if (nodeIter.hasNext()) {
			current = nodeIter.next();
		} else {
			System.out.println("BUG");
			return false;
		}
		boolean second = false;
		int k = 0;
		for (; FLAG; k++) {
			while (FLAG) {
				FLAG = false;
				if (myGraph.getE(current.getKey()) == null) return false;
				List<edge_data> list = new LinkedList<>(myGraph.getE(current.getKey()));
				if (list.isEmpty()) return false;
				for (edge_data i : list) {
					if (myGraph.getNode(i.getDest()).getTag() != k) {
						myGraph.getNode(i.getDest()).setTag(k);
						myQue.add(myGraph.getNode(i.getDest()));
					} else if (!second) {
						finish.add(current);
					}
				}
				if (!myQue.isEmpty()) {
					current = myQue.poll();
					FLAG = true;
				}
			}
			if (!finish.isEmpty()) {
				current = finish.poll();
				second = true;
				FLAG = true;
			}
			nodeIter = temp.iterator();
			while (nodeIter.hasNext()) {
				node_data tempNode = nodeIter.next();
				if (tempNode.getTag() != k) {
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * returns the length of the shortest path between src to dest
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return Distance of the src to dest in double
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		resetTag();
		if(myGraph.getNode(src)==null || myGraph.getNode(dest)==null)
			return Integer.MIN_VALUE;

		Queue<Edata> PQdist = new LinkedList<Edata>();
		Queue<Edata> PQnode = new LinkedList<Edata>();
		HashMap<Integer, Double> dist=new HashMap<Integer, Double>(myGraph.nodeSize());
		NodeData Runner=(NodeData)myGraph.getNode(src);
		Edata CurNode= null;

		dist.put(src,(double)0);
		int i=0;

		while(i<=myGraph.nodeSize() || !PQnode.isEmpty()) {
			Collection<edge_data> Col=new ArrayList<edge_data>(myGraph.getE(Runner.getKey()));
			Collection<edge_data> Col2=new ArrayList<edge_data>(myGraph.getE(Runner.getKey()));
			AddEdgesToPriorityQueue(PQdist,Col);
			AddNodesToPriorityQueue(PQnode,Col2);
			GetNewDist(PQdist,dist);
			CurNode=(Edata)PQnode.poll();
			if(CurNode!=null)
				Runner=(NodeData)myGraph.getNode(CurNode.getDest());
			i++;
		}

		return dist.get(dest).doubleValue();
	}




	/**
	 * returns the the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * see: https://en.wikipedia.org/wiki/Shortest_path_problem
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return the path of the way in list of node_data
	 */
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		resetTag();
		if(myGraph.getNode(src)==null || myGraph.getNode(dest)==null)
			return null;
		Queue<Edata> PQdist = new LinkedList<Edata>();
		Queue<Edata> PQnode = new LinkedList<Edata>();
		HashMap<Integer, Double> dist=new HashMap<Integer, Double>(myGraph.nodeSize());
		HashMap<Integer, ArrayList<Integer>> Paths=new HashMap<Integer, ArrayList<Integer>>(myGraph.nodeSize());
		Edata CurNode = null;
		NodeData Runner=(NodeData)myGraph.getNode(src);

		dist.put(src,(double)0);
		Paths.put(src, new ArrayList<Integer>());
		Paths.get(src).add(src);
		int i=0;

		while(i<=myGraph.nodeSize() || !PQnode.isEmpty()) {
			Collection<edge_data> Col=new ArrayList<edge_data>(myGraph.getE(Runner.getKey()));
			Collection<edge_data> Col2=new ArrayList<edge_data>(myGraph.getE(Runner.getKey()));
			AddEdgesToPriorityQueue(PQdist,Col);
			AddNodesToPriorityQueue(PQnode,Col2);
			GetNewDist(PQdist,dist,Paths);
			CurNode=(Edata)PQnode.poll();
			if(CurNode!=null)
				Runner=(NodeData)myGraph.getNode(CurNode.getDest());
			i++;
		}
		List<node_data> Ans=new LinkedList<node_data>();
		int j=0;
		while(j<Paths.get(dest).size()) {
			Ans.add(myGraph.getNode(Paths.get(dest).get(j)));
			System.out.println(Paths.get(dest).get(j));
			j++;
		}
		return Ans;
	}
	/**
	 * computes a relatively short path which visit each node in the targets List.
	 * Note: this is NOT the classical traveling salesman problem,
	 * as you can visit a node more than once, and there is no need to return to source node -
	 * just a simple path going over all nodes in the list.
	 * @param targets
	 * @return the path of the way in list of node_data
	 */
	@Override
	public List<node_data> TSP(List<Integer> targets) {
		resetTag();
		if (targets == null || targets.isEmpty()) return null;
		int i = 0;
		List<node_data> ans = new LinkedList<node_data>();

		HashMap<Integer, Boolean> hashMap = new LinkedHashMap<>();
		while (i<targets.size()){
			if(!hashMap.containsKey(targets.get(i))){
				hashMap.put(targets.get(i),true);
				i++;
			}
			else {
				targets.remove(i);
			}
		}
		i=0;
		int temp = 0;
		int temp2;
		List<node_data> tempN;
		if (!targets.isEmpty()) {
			temp = targets.remove(0);
		}
		while (!targets.isEmpty()){
			temp2 = targets.remove(0);
			tempN = shortestPath(temp,temp2);
			if(tempN == null) return null;
			for (node_data nk: tempN){
				if (targets.contains(nk.getKey())){
					targets.remove((Integer)nk.getKey());
				}
			}
			ans.addAll(tempN);
			temp = temp2;
		}

		i = 0;
		while (i < ans.size() - 1) {
			if (ans.get(i).equals(ans.get(i + 1)))
				ans.remove(i);
			else
				i++;
		}
		//System.out.println( );
		//for(node_data n:ans){
		//System.out.print(" "+(n.getKey()+1));
		//}
		return ans;
	}

	/**
	 * Compute a deep copy of this graph.
	 * @return
	 */
	@Override
	public graph copy() {
		return new DGraph(myGraph);//
	}
	private void resetTag(){
		if (myGraph.getV()!= null) {
			for (node_data n : myGraph.getV()) {
				n.setTag(0);
			}
		}
	}
	private void GetNewDist(Queue<Edata> PQdist, HashMap<Integer, Double> dist) {
		while(!PQdist.isEmpty()) {
			Edata CurNode=PQdist.poll();
			if(!dist.containsKey(CurNode.getDest()) || dist.get(CurNode.getDest()).doubleValue()>dist.get(CurNode.getSrc()).doubleValue()+CurNode.getWeight()) {
				dist.put(CurNode.getDest(),dist.get(CurNode.getSrc())+CurNode.getWeight());
			}
		}
	}
	private void GetNewDist(Queue<Edata> PQdist, HashMap<Integer, Double> dist, HashMap<Integer, ArrayList<Integer>> Paths) {
		while(!PQdist.isEmpty()) {
			Edata CurNode=PQdist.poll();
			if(!dist.containsKey(CurNode.getDest()) || dist.get(CurNode.getDest()).doubleValue()>dist.get(CurNode.getSrc()).doubleValue()+CurNode.getWeight()) {
				Paths.put(CurNode.getDest(), new ArrayList<Integer>());
				dist.put(CurNode.getDest(),dist.get(CurNode.getSrc())+CurNode.getWeight());
				int k=0;
				while(k<Paths.get(CurNode.getSrc()).size()) {
					Paths.get(CurNode.getDest()).add(Paths.get(CurNode.getSrc()).get(k));
					k++;
				}
				Paths.get(CurNode.getDest()).add(CurNode.getDest());
			}
		}
	}

	private void AddEdgesToPriorityQueue(Queue<Edata> Pqueue, Collection<edge_data> Col) {
		PriorityQueue<edge_data> minHeap = new PriorityQueue<edge_data>(new Comparator<edge_data>() {
			@Override
			public int compare(edge_data o1, edge_data o2) {
				return - Double.compare(o2.getWeight(),o1.getWeight());
			}
		});
		Object[] temp=Col.toArray();
		int i=0;
		while(i<temp.length) {
			minHeap.add((edge_data)temp[i]);
			i++;
		}
		while(minHeap.iterator().hasNext()) {
			Pqueue.add((Edata)minHeap.poll());
		}
	}

	private void AddNodesToPriorityQueue(Queue<Edata> Pqueue, Collection<edge_data> Col) {
		PriorityQueue<edge_data> minHeap = new PriorityQueue<edge_data>(new Comparator<edge_data>() {
			@Override
			public int compare(edge_data o1, edge_data o2) {
				return - Double.compare(o2.getWeight(),o1.getWeight());
			}
		});
		int i=0;
		Object[] temp=Col.toArray();
		while(i<temp.length) {
			Edata cur=(Edata)temp[i];
			if(myGraph.getNode(cur.getDest()).getTag()!=2) {
				minHeap.add((edge_data)temp[i]);
			}
			i++;
		}
		i=0;
		while(i<minHeap.size()) {
			if(!Pqueue.contains((Edata)minHeap.peek()) && !minHeap.isEmpty())
				Pqueue.add((Edata)minHeap.poll());
			if(Pqueue.contains((Edata)minHeap.peek()))
				minHeap.poll();
		}
		i=0;
		while(i<Col.size()) {
			Edata cur=(Edata)temp[i];
			this.myGraph.getNode(cur.getDest()).setTag(2);
			i++;
		}
		Col=null;
	}

}
