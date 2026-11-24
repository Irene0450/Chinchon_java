package chinchon.game.logic;

import chinchon.game.model.Card;
import chinchon.game.model.Player;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private final List<Player> players = new ArrayList<>();
    private final List<Round> rounds = new ArrayList<>();
    private Round currentRound;
    private boolean finished;
    private Player chinchonWinner;

    public Game(List<Player> players) {
        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException("La partida necesita mínimo 2 jugadores");
        }
        this.players.addAll(players);
    }

    public void start() {
        if (finished) {
            throw new IllegalStateException("La partida ya terminó.");
        }
        startNextRound();
    }

    public void startNextRound() {
        if (finished) {
            throw new IllegalStateException("La partida ya terminó.");
        }
        if (getActivePlayers().size() < 2) {
            finished = true;
            return;
        }
        currentRound = new Round(getActivePlayers());
        currentRound.start();
        rounds.add(currentRound);
    }

    public void finishCurrentRound(Player player, Card closingCard, List<List<Card>> grupos) {
        if (currentRound == null) {
            throw new IllegalStateException("No hay ninguna ronda empezada.");
        }
        currentRound.finishRound(player, closingCard, grupos);

        // Chinchón: el jugador gana la partida directamente
        if (currentRound.getCloseType() == CloseType.CHINCHON) {
            chinchonWinner = player;
            finished = true;
            return;
        }
        checkGameFinished();
    }

    private void checkGameFinished() {
        if (getActivePlayers().size() <= 1) {
            finished = true;
        }
    }

    public List<Player> getActivePlayers() {
        List<Player> activePlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getPoints() <= 100) {
                activePlayers.add(player);
            }
        }
        return activePlayers;
    }

    public Player getWinner() {
        if (!finished) {
            return null;
        }

        if (chinchonWinner != null) {
            return chinchonWinner;
        }

        if (players == null || players.isEmpty()) {
            return null;
        }

        Player minPlayer = players.get(0);
        
        for (Player player : players) {
            if (player.getPoints() < minPlayer.getPoints()) {
                minPlayer = player;
            }
        }

        return minPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public boolean isFinished() {
        return finished;
    }
}
