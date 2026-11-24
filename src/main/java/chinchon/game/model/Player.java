/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chinchon.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author irene
 */
public class Player {

    private final String id;
    private final String name;
    private final List<Card> hand = new ArrayList<>();
    private int points;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getPoints() {
        return points;
    }
    
    public void showHand() {
        for (Card c : hand) {
            c.showCard();
        }
    }
    
    public void clearHand() {
        hand.clear();
    }
    
    public void addPoints(int newPoints) {
        points += newPoints;
        if (points > 100) {
            System.out.println("Eliminado");
        }
    }
}
