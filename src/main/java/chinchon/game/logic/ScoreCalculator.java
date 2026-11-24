/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chinchon.game.logic;

import chinchon.game.model.Card;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author irene
 */
public class ScoreCalculator {
    public static int calculateScore(List<Card> hand, List<List<Card>> groups) {
        if (hand == null || hand.isEmpty()) {
            return 0;
        }

        if (CombinationValidator.isMinusTen(hand, groups)) {
            return -10;
        }

        Set<Card> groupedCards = new HashSet<>();

        if (groups != null) {
            for (List<Card> group : groups) {
                if (!CombinationValidator.isValidCombination(group)) {
                    continue;
                }
                for (Card card : group) {
                    if (hand.contains(card)) {
                        groupedCards.add(card);
                    }
                }
            }
        }

        int points = 0;
        
        for (Card card : hand) {
            if (!groupedCards.contains(card)) {
                points += card.value();
            }
        }

        return points;
    }
}
