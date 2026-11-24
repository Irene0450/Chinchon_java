package chinchon.game.logic;

import chinchon.game.model.Card;
import chinchon.game.model.Player;
import chinchon.game.model.Suit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Round {
    private final List<Player> players;
    private final List<Card> deck = new ArrayList<>();
    private final List<Card> discardPile = new ArrayList<>();
    private int currentPlayerIndex;
    private boolean finished;
    private Player winner;
    private CloseType closeType;

    public Round(List<Player> players) {
        this.players = new ArrayList<>(players);
    }

    public void start() {
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores.");
        }
        
        // Reset
        deck.clear();
        discardPile.clear();
        for (Player player : players) {
            player.clearHand();
        }
        createDeck();
        Collections.shuffle(deck);

        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                player.getHand().add(drawFromDeck());
            }
        }

        discardPile.add(drawFromDeck());
        currentPlayerIndex = 0;
        finished = false;
        winner = null;
        closeType = null;
    }

    private void createDeck() {
        Suit[] suits = Suit.values();

        // Baraja de 40 cartas:
        int[] ranks = {1, 2, 3, 4, 5, 6, 7, 10, 11, 12};

        for (Suit suit : suits) {
            for (int rank : ranks) {
                deck.add(new Card(rank, suit));
            }
        }
    }

    private Card drawFromDeck() {
        if (deck.isEmpty()) {
            mixDiscardPile();
        }
        if (deck.isEmpty()) {
            throw new IllegalStateException("No quedan cartas.");
        }
        return deck.remove(deck.size() - 1);
    }

    private void mixDiscardPile() {
        if (discardPile.size() <= 1) {
            return;
        }
        Card topCard = discardPile.remove(discardPile.size() - 1);
        deck.addAll(discardPile);
        discardPile.clear();
        discardPile.add(topCard);
        Collections.shuffle(deck);
    }

    public Player getCurrentPlayer() {
        if (players.isEmpty()) {
            return null;
        }
        return players.get(currentPlayerIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public boolean isFinished() {
        return finished;
    }

    public Player getWinner() {
        return winner;
    }

    public CloseType getCloseType() {
        return closeType;
    }

    // Robar carta del mazo
    public Card drawCard(Player player) {
        checkTurn(player);

        if (player.getHand().size() >= 8) {
            throw new IllegalStateException("El jugador ya tiene 8 cartas.");
        }

        Card card = drawFromDeck();
        player.getHand().add(card);
        return card;
    }

    // Robar la carta boca arriba de los descartes
    public Card drawDiscard(Player player) {
        checkTurn(player);

        if (discardPile.isEmpty()) {
            throw new IllegalStateException("No hay cartas en los descartes.");
        }

        if (player.getHand().size() >= 8) {
            throw new IllegalStateException("El jugador ya tiene 8 cartas.");
        }

        Card card = discardPile.remove(discardPile.size() - 1);
        player.getHand().add(card);
        return card;
    }

    // Descartar y pasar el turno
    public void discardCard(Player player, Card card) {
        checkTurn(player);
        if (player.getHand().size() != 8) {
            throw new IllegalStateException("Debes robar antes de descartar.");
        }

        if (!player.getHand().remove(card)) {
            throw new IllegalArgumentException("La carta no está en la mano del jugador.");
        }

        discardPile.add(card);
        nextTurn();
    }

    private void checkTurn(Player player) {
        if (finished) {
            throw new IllegalStateException("La ronda ya terminó.");
        }

        if (player != getCurrentPlayer()) {
            throw new IllegalStateException("No es el turno de este jugador.");
        }
    }

    private void nextTurn() {
        currentPlayerIndex++;

        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }
    }

    public void finishRound(Player player, Card closingCard, List<List<Card>> grupos) {
        if (finished) {
            throw new IllegalStateException("La ronda ya terminó.");
        }

        if (player != getCurrentPlayer()) {
            throw new IllegalStateException("No es el turno de este jugador.");
        }

        if (player.getHand().size() != 8) {
            throw new IllegalStateException("Debes robar antes de cerrar.");
        }

        if (!player.getHand().contains(closingCard)) {
            throw new IllegalArgumentException("La carta de cierre no está en la mano del jugador.");
        }

        List<Card> remaining = new ArrayList<>(player.getHand());
        remaining.remove(closingCard);

        CloseType type;
        if (grupos != null && grupos.size() == 1 && CombinationValidator.isChinchon(grupos.get(0))
                && CombinationValidator.isMinusTen(remaining, grupos)) {
            type = CloseType.CHINCHON;
        } else if (CombinationValidator.isMinusTen(remaining, grupos)) {
            type = CloseType.MINUS_TEN;
        } else if (CombinationValidator.canClose(remaining, grupos)) {
            type = CloseType.NORMAL;
        } else {
            throw new IllegalArgumentException(
                    "No se puede cerrar: grupos no válidos o las cartas sin casar suman más de "
                    + CombinationValidator.MAX_CLOSE_POINTS);
        }

        // La carta de cierre sale de la mano y queda en el descarte
        player.getHand().remove(closingCard);
        discardPile.add(closingCard);

        switch (type) {
            case CHINCHON -> {
                // No se reparten puntos: el chinchón gana la partida (lo aplica Game)
            }
            case MINUS_TEN -> addRoundPoints(player, -10);
            case NORMAL -> addRoundPoints(player, CombinationValidator.ungroupedPoints(remaining, grupos));
        }

        closeType = type;
        winner = player;
        finished = true;
    }

    private void addRoundPoints(Player closer, int closerPoints) {
        for (Player p : players) {
            if (p == closer) {
                p.addPoints(closerPoints);
            } else {
                // Se suman todas las cartas de su mano
                int points = 0;
                for (Card card : p.getHand()) {
                    points += card.value();
                }
                p.addPoints(points);
            }
        }
    }
}