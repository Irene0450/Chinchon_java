/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chinchon.game.logic;

import chinchon.game.model.Card;
import chinchon.game.model.Suit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author irene
 */
public class CombinationValidator {

    // Trío
    public static boolean isThreeOfAKind(List<Card> cards) {
        if (cards == null || cards.size() != 3) {
            return false;
        }
        
        int rank = cards.get(0).getRank();
        for (Card card : cards) {
            if (card.getRank() != rank) {
                return false;
            }
        }
        return true;
    }

    // Cuarteto
    public static boolean isFourOfAKind(List<Card> cards) {
        if (cards == null || cards.size() != 4) {
            return false;
        }
        
        int rank = cards.get(0).getRank();
        for (Card card : cards) {
            if (card.getRank() != rank) {
                return false;
            }
        }
        return true;
    }

    // Escalera
    public static boolean isStraight(List<Card> cards) {
        if (cards == null || cards.size() < 3) {
            return false;
        }
        
        Suit suit = cards.get(0).getSuit();
        for (Card card : cards) {
            if (card.getSuit() != suit) {
                return false;
            }
        }

        // Aunque no debería haber cartas repetidas
        Set<Integer> orders = new HashSet<>();
        for (Card card : cards) {
            if (!orders.add(card.order())) {
                return false;
            }
        }

        List<Card> sorted = new ArrayList<>(cards);
        sorted.sort(Comparator.comparingInt(Card::order));

        for (int i = 0; i < sorted.size() - 1; i++) {
            int actual = sorted.get(i).order();
            int next = sorted.get(i + 1).order();

            if (next != actual + 1) {
                return false;
            }
        }

        return true;
    }

    // Chinchón
    public static boolean isChinchon(List<Card> cards) {
        return cards != null && cards.size() == 7 && isStraight(cards);
    }

    // Menos 10: toda la mano casada
    public static boolean isMinusTen(List<Card> hand, List<List<Card>> groups) {
        if (hand == null || hand.isEmpty()) {
            return false;
        }
        if (groups == null || groups.isEmpty()) {
            return false;
        }

        return areValidGroups(hand, groups) && ungroupedCards(hand, groups).isEmpty();
    }

    // Para poder cerrar mínimo 5 puntos sueltos
    public static final int MAX_CLOSE_POINTS = 5;

    public static boolean canClose(List<Card> hand, List<List<Card>> groups) {
        if (hand == null || hand.isEmpty()) {
            return false;
        }
        return areValidGroups(hand, groups) && ungroupedPoints(hand, groups) <= MAX_CLOSE_POINTS;
    }

    public static boolean areValidGroups(List<Card> hand, List<List<Card>> groups) {
        if (groups == null) {
            return true;
        }

        Set<Card> groupedCards = new HashSet<>();
        for (List<Card> group : groups) {
            if (!isValidCombination(group)) {
                return false;
            }
            for (Card card : group) {
                if (!hand.contains(card)) {
                    return false;
                }
                if (!groupedCards.add(card)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Cartas sin casar (asumindo que todos los grupos son válidos)
    public static List<Card> ungroupedCards(List<Card> hand, List<List<Card>> groups) {
        Set<Card> groupedCards = new HashSet<>();
        if (groups != null) {
            for (List<Card> group : groups) {
                groupedCards.addAll(group);
            }
        }

        List<Card> ungrouped = new ArrayList<>();
        for (Card card : hand) {
            if (!groupedCards.contains(card)) {
                ungrouped.add(card);
            }
        }
        return ungrouped;
    }

    public static int ungroupedPoints(List<Card> hand, List<List<Card>> groups) {
        int points = 0;
        for (Card card : ungroupedCards(hand, groups)) {
            points += card.value();
        }
        return points;
    }

    public static boolean isValidCombination(List<Card> cards) {
        return isThreeOfAKind(cards) || isFourOfAKind(cards) || isStraight(cards);
    }
    
}
