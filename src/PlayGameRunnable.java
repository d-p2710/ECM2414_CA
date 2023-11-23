import java.io.IOException;
import java.util.ArrayList;



public class PlayGameRunnable implements Runnable {
    private final Player player;
    private final ArrayList<CardDeck> decks;
    public PlayGameRunnable(Player player, ArrayList<CardDeck> decks) {
        this.player = player;
        this.decks = decks;
    }
    @Override
    public void run() {
        try {
            Card cardToDiscard = player.drawCard(decks.get(player.getLHSDeckId()).getDeck(), player.getLHSDeckId());
            player.discardToRightDeck(decks.get(player.getRHSDeckId()).getDeck(), cardToDiscard, player.getRHSDeckId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}