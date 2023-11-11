import java.util.ArrayList;
import java.util.List;
//
public class CardDeck {

    private final int deckID;
    private List<Card> deck;

    public CardDeck(int deckID) {
        this.deckID = deckID;
        this.deck = new ArrayList<>();
    }

    public void addCardtoDeck(Card card) {
        deck.add(card);
    }
}
