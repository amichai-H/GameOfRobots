package dataStructure;

import java.io.Serializable;

public class Edata implements edge_data, Serializable {
	private int Src,Dest,tag;
	double Weight;
	private String info;

	/**
	 * copy contractor
	 * @param e
	 */
	public Edata(edge_data e){
		this.Dest = e.getDest();
		this.Src = e.getSrc();
		this.Weight = e.getWeight();
	}

	/**
	 * connect src to dest in some weight
	 * @param src
	 * @param dest
	 * @param weight
	 */
	public Edata(int src,int dest, double weight){
		if (weight<0) throw new RuntimeException("ERR: weight cant be negetive");
		if (src==dest) throw new RuntimeException("ERR: the destination can't be equals to the source ");
		this.Dest = dest;
		this.Src = src;
		this.Weight = weight;
	}

	/**
	 *
	 * @return key of src in the graph
	 */
	@Override
	public int getSrc() {
		return this.Src;
	}

	/**
	 *
	 * @return key of dest in the graph
	 */
	@Override
	public int getDest() {
		// TODO Auto-generated method stub
		return this.Dest;
	}

	/**
	 *
	 * @return weight of the edge
	 */
	@Override
	public double getWeight() {
		// TODO Auto-generated method stub
		return this.Weight;
	}

	/**
	 *
	 * @return info of the edge
	 */
	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return this.info;
	}

	/**
	 *  set info of the edge
	 * @param s
	 */
	@Override
	public void setInfo(String s) {
		this.info=s;
		
	}

	/**
	 *
	 * @return tag of the edge
	 */
	@Override
	public int getTag() {
		// TODO Auto-generated method stub
		return tag;
		}

	/**
	 *
	 * @param t - the new value of the tag
	 */
	@Override
	public void setTag(int t) {
		// TODO Auto-generated method stub
		this.tag=t;	
	}

}
