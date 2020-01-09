package dataStructure;


import org.json.JSONArray;
import org.json.JSONObject;
import utils.Point3D;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DGraph implements graph, Serializable {
	private HashMap<Integer, node_data> NMap = new LinkedHashMap<>();
	private HashMap<Integer, HashMap<Integer, edge_data>> EMap = new LinkedHashMap<>();
	private int EdgeZise = 0;
	private int myMc=0;

	/**
	 * default contractor
	 */
	public DGraph(){
	}
	/**
	 * copy contractor
	 */
	public DGraph(graph g){
        Collection<node_data> tempV = g.getV();

		for (dataStructure.node_data node_data : tempV) {
			NodeData nodeData = new NodeData(node_data);
			NMap.put(nodeData.getKey(), nodeData);
			Collection<edge_data> tempE = g.getE(node_data.getKey());
			if(tempE!=null) {
				for (dataStructure.edge_data edge_data : tempE) {
					Edata edata = new Edata(edge_data);
					if (!EMap.containsKey(edata.getSrc())) {
						HashMap<Integer, edge_data> tempHASH = new LinkedHashMap<>();
						tempHASH.put(edata.getDest(), edata);
						EMap.put(edata.getSrc(), tempHASH);
					} else {
						EMap.get(edata.getSrc()).put(edata.getDest(), edata);
					}
				}
			}
			else {
				HashMap<Integer, edge_data> tempHASH = new LinkedHashMap<>();
				EMap.put(nodeData.getKey(), tempHASH);

			}
		}
		EdgeZise = g.edgeSize();

    }

	/**
	 *
	 * @param key - the node_id.
	 * @return node in node_data.
	 */
	@Override
	public node_data getNode(int key) {
		if (NMap.containsKey(key))
			return NMap.get(key);
		return null;
	}

	/**
	 *
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @return edge from src to dest in edge_data.
	 */
	@Override
	public edge_data getEdge(int src, int dest) {
		if (EMap.containsKey(src))
			if (EMap.get(src).containsKey(dest))
				return EMap.get(src).get(dest);
		return null;
	}

	/**
	 * add node to the graph
	 * @param n
	 */
	@Override
	public void addNode(node_data n) {
		if (NodeData.getIDnode()<n.getKey())
			NodeData.setIDnode(n.getKey()+1);
		NMap.put(n.getKey(), n);
		HashMap<Integer,edge_data> temp = new LinkedHashMap<>();
		EMap.put(n.getKey(),temp);
		myMc++;
	}

	/**
	 * Connect an edge with weight w between node src to node dest.
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
	 */
	@Override
	public void connect(int src, int dest, double w) {
		if (!(NMap.containsKey(dest)&&NMap.containsKey(src)))
			throw new RuntimeException("not Exist");
		edge_data edata = new Edata(src,dest,w);
		if (EMap.containsKey(src)){
			EMap.get(src).put(dest,edata);
		}else {
			HashMap<Integer,edge_data> temp = new LinkedHashMap<>();
			temp.put(dest,edata);
			EMap.put(src,temp);
		}
		EdgeZise++;
		myMc++;
		
	}

	/**
	 *
	 * @return all nodes in collection
	 */
	@Override
	public Collection<node_data> getV() {
		return NMap.values();
	}

	/**
	 *
	 * @param node_id
	 * @return All the edge that come from this node (out) in collection
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		if (!NMap.containsKey(node_id)) {
			return null;
		}
		if (!EMap.containsKey(node_id)) {
			return null;
		}
		return EMap.get(node_id).values();
	}

	/**
	 * remove node in the graph and all the edge that associate to this node
	 * @param key
	 * @return the node that removed
	 */
	@Override
	public node_data removeNode(int key) {
		if (NMap.containsKey(key)){
			myMc++;
			for (int i =0; i<NMap.size();i++){
				if (NMap.containsKey(i)){
					removeEdge(i,key);
				}
			}
			return NMap.remove(key);
		}
		else return null;

	}

	/**
	 *
	 * @param src - src of this edge
	 * @param dest - dest of this node
	 * @return edge that removed
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {
		if (!(NMap.containsKey(src)&&NMap.containsKey(dest))){
			return null;
		}
		myMc++;
		if (EMap.containsKey(src)) {
			edge_data temp = EMap.get(src).remove(dest);

			if (temp != null) {
				EdgeZise--;
			}
			return temp;
		}
		return null;
	}

	/**
	 *
	 * @return amount of the node in the graph
	 */
	@Override
	public int nodeSize() {
		return NMap.size();
	}
	/**
	 *
	 * @return amount of the edge in the graph
	 */
	@Override
	public int edgeSize() {
		return EdgeZise;
	}

	/**
	 * the mode change every time when the graph changed.
	 * @return Mode Count
	 */
	@Override
	public int getMC() {
		return myMc;
	}
	public void init(String jsonSTR) {
		try {
			this.init();
			JSONObject graph = new JSONObject(jsonSTR);
			JSONArray nodes = graph.getJSONArray("Nodes");
			JSONArray edges = graph.getJSONArray("Edges");

			int i;
			int s;
			for(i = 0; i < nodes.length(); ++i) {
				s = nodes.getJSONObject(i).getInt("id");
				String pos = nodes.getJSONObject(i).getString("pos");
				Point3D p = new Point3D(pos);
				this.addNode(new NodeData(s, p));
			}

			for(i = 0; i < edges.length(); ++i) {
				s = edges.getJSONObject(i).getInt("src");
				int d = edges.getJSONObject(i).getInt("dest");
				double w = edges.getJSONObject(i).getDouble("w");
				this.connect(s, d, w);
			}
		} catch (Exception var10) {
			var10.printStackTrace();
		}

	}
	private void init() {
		this.NMap = new LinkedHashMap<>();
		this.EMap = new LinkedHashMap<>();
	}


}
