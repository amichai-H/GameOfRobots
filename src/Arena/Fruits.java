package Arena;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.Edata;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Fruits {
    graph graph;
    ArrayList<Fruit> myFrit = new ArrayList<>();

    public Fruits(game_service game, graph g){
        graph = g;
        int i =0;
        Iterator<String> f_iter = game.getFruits().iterator();
        while (f_iter.hasNext()){
            try {
                JSONObject line1 = new JSONObject(f_iter.next());
                JSONObject frut = line1.getJSONObject("Fruit");
                double value = frut.getDouble("value");
                String pos = frut.getString("pos");
                int type = frut.getInt("type");
                String pos3D[] = pos.split(",");
                Point3D position = new Point3D(Double.parseDouble(pos3D[0]),Double.parseDouble(pos3D[1]));
                Fruit fruit = new Fruit(value,position,type,i);
                i++;
                myFrit.add(fruit);
            }catch (Exception e){
                throw new RuntimeException(e);

            }
        }
    }

    public Collection<Fruit> getMyFrit() {
        return myFrit;
    }
    public Iterator<Fruit> iterator(){
        return myFrit.iterator();
    }
    public edge_data getEdge(int id){
        Iterator<node_data> nodeDataIterator = graph.getV().iterator();
        Fruit current = getFruit(id);
        while (nodeDataIterator.hasNext()){
            node_data temp = nodeDataIterator.next();
            Iterator<edge_data> edataIterator = graph.getE(temp.getKey()).iterator();
            while (edataIterator.hasNext()) {
                edge_data tempE = edataIterator.next();
                int src = tempE.getSrc();
                int des = tempE.getDest();
                node_data srcN = graph.getNode(src);
                node_data desN = graph.getNode(des);
                Point3D middle = new Point3D((srcN.getLocation().x() * 0.5 + desN.getLocation().x() * 0.5), (srcN.getLocation().y() * 0.5 + desN.getLocation().y() * 0.5));
//                if (middle.distance2D(current.getPosition())<0.002){
//                    return tempE;
//                }
                try {

                    double distanceA = srcN.getLocation().distance2D(current.getPosition()) + current.getPosition().distance2D(desN.getLocation());
                    double distanceB = srcN.getLocation().distance2D(desN.getLocation());
                    //middle.distance2D(current.getPosition())<0.001
                    if (Math.abs(distanceA - distanceB) <= 0.000000001) {
                        return tempE;
                    }
                }catch (Exception e){

                }
                }


        }
        return null;

    }
    public Fruit getFruit(int id){
        Iterator<Fruit> fruitIterator = iterator();
        while (fruitIterator.hasNext()){
            Fruit temp = fruitIterator.next();
            if (id==temp.getId()){
                return temp;
            }
        }
        return null;
    }

    public Fruit getMaxValue() {
        Iterator<Fruit> fruitIterator = iterator();
        if (fruitIterator.hasNext()) {
            Fruit max = fruitIterator.next();
//            while (!max.isTaken()&&fruitIterator.hasNext()){
//                max = fruitIterator.next();
//            }
            while (fruitIterator.hasNext()){
                Fruit temp = fruitIterator.next();

                if(max.getValue()<temp.getValue()&&!temp.isTaken()){
                    max = temp;
                }
            }
           // max.take();
            return max;
        }
        return null;
    }
     public Fruit geMinValue() {
        Iterator<Fruit> fruitIterator = iterator();
        if (fruitIterator.hasNext()) {
            Fruit min = fruitIterator.next();

//            while (!min.isTaken()&&fruitIterator.hasNext()){
//                min = fruitIterator.next();
//            }
            while (fruitIterator.hasNext()){
                Fruit temp = fruitIterator.next();

                if(min.getValue()>temp.getValue()&&!temp.isTaken()){
                    min = temp;
                }
            }
            //min.take();
            return min;
        }
        return null;
    }

    public Fruit getCloseF(int src,boolean spd){
        node_data nnode = graph.getNode(src);
        Graph_Algo graph_algo = new Graph_Algo(graph);
        Point3D p = nnode.getLocation();
        Iterator<Fruit> fruitIterator = iterator();
        if (fruitIterator.hasNext()) {
            Fruit close = fruitIterator.next();
//            while (!close.isTaken()&&fruitIterator.hasNext()){
//                close = fruitIterator.next();
//            }
            edge_data closeE = getEdge(close.getId());
            while (fruitIterator.hasNext()) {
                    Fruit temp = fruitIterator.next();
                    edge_data etepm = getEdge(temp.getId());
                    try {
                        if (spd) {
                            if (graph_algo.shortestPathDist(src, closeE.getDest()) > graph_algo.shortestPathDist(src, etepm.getDest())) {
                                close = temp;
                            }
                        }
                        else {
                            if (close.getPosition().distance2D(p)>temp.getPosition().distance2D(p)){
                                close=temp;
                            }
                        }

                    }catch (Exception e){
                        if (close.getPosition().distance2D(p)>temp.getPosition().distance2D(p)){
                            close=temp;
                        }

                    }

            }
            //close.take();
            return close;
        }
        return null;

    }
}
