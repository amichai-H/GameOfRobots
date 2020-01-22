import Arena.Fruit;
import Arena.Fruits;
import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class FruitsTest {

    @Test
    void getMyFrit() {
        game_service game = Game_Server.getServer(0); // you have [0,23] games
        String g = game.getGraph();
        DGraph gg = new DGraph();
        gg.init(g);

        Fruits fruits = new Fruits(game, gg);
        assertNotNull(fruits.getMyFrit());
        int expected =1;
        assertEquals(expected,fruits.getMyFrit().size());
        game.stopGame();
    }

    @Test
    void iterator() {
        game_service game = Game_Server.getServer(0); // you have [0,23] games
        String g = game.getGraph();
        DGraph gg = new DGraph();
        gg.init(g);

        Fruits fruits = new Fruits(game, gg);
        assertNotNull(fruits.iterator());
        assertEquals(fruits.getMyFrit().iterator().next(),fruits.iterator().next());
        game.stopGame();
    }

    @Test
    void getEdge() {
        game_service game = Game_Server.getServer(0); // you have [0,23] games
        String g = game.getGraph();
        DGraph gg = new DGraph();
        gg.init(g);

        Fruits fruits = new Fruits(game, gg);
        assertNotNull(fruits.getEdge(0));
        //System.out.println(fruits.getEdge(0).getSrc());
        int expected =8;
        assertEquals(expected,fruits.getEdge(0).getSrc());
        game.stopGame();
    }

    @Test
    void getFruit() {
        game_service game = Game_Server.getServer(0); // you have [0,23] games
        String g = game.getGraph();
        DGraph gg = new DGraph();
        gg.init(g);

        Fruits fruits = new Fruits(game, gg);
        assertNotNull(fruits.getMyFrit());
        int expected =1;
        assertEquals(expected,fruits.getMyFrit().size());
        game.stopGame();
    }

    @Test
    void getMaxValue() {
        game_service game = Game_Server.getServer(5); // you have [0,23] games
        String g = game.getGraph();
        DGraph gg = new DGraph();
        gg.init(g);

        Fruits fruits = new Fruits(game, gg);
        Fruit myMax = fruits.getMaxValue();
        Iterator<Fruit> fruitIterator = fruits.iterator();
        Fruit max = fruitIterator.next();
         while (fruitIterator.hasNext()){
             Fruit temp = fruitIterator.next();
             if (max.getValue()<temp.getValue()){
                 max = temp;
             }
         }
        System.out.println(max.getValue()+" "+fruits.getMaxValue().getValue());
        assertEquals(max,myMax);
        game.stopGame();
    }

    @Test
    void geMinValue() {
        game_service game = Game_Server.getServer(5); // you have [0,23] games
        String g = game.getGraph();
        DGraph gg = new DGraph();
        gg.init(g);

        Fruits fruits = new Fruits(game, gg);
        Fruit myMin = fruits.geMinValue(0);
        Iterator<Fruit> fruitIterator = fruits.iterator();
        Fruit min = fruitIterator.next();
        while (fruitIterator.hasNext()){
            Fruit temp = fruitIterator.next();
            if (min.getValue()>temp.getValue()){
                min = temp;
            }
        }
        System.out.println(min.getValue()+" "+fruits.geMinValue(0).getValue());
        assertEquals(min,myMin);
        game.stopGame();
    }

    @Test
    void getCloseF() {
        game_service game = Game_Server.getServer(5); // you have [0,23] games
        String g = game.getGraph();
        DGraph gg = new DGraph();
        gg.init(g);

        Fruits fruits = new Fruits(game, gg);
        Fruit close =fruits.getCloseF(2,false);
        //System.out.println(fruits.getEdge(close.getId()).getSrc());
        assertEquals(gg.getEdge(6,7),fruits.getEdge(close.getId()));


    }
}