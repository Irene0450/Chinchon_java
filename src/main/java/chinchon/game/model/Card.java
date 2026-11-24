/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chinchon.game.model;

/**
 *
 * @author irene
 */
public class Card {
    private int rank; // Número en la carta, diferente de value (ver abajo método value())
    private Suit suit;

    public Card(int rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public void showCard(){
        System.out.println(rank + " de " + suit.toString().toLowerCase());
    }
    
    // Puntos que suma si no se agrupa. Ejemplo para REY: rank=12 value=10
    public int value() { 
        return rank <= 7 ? rank : 10;
    }
    
    // Para la posición en la escalera (no hay 8 ni 9 así que el 7 y 10 son consecutivos)
    public int order() {
        return rank <= 7 ? rank : rank - 2;
    }
}
