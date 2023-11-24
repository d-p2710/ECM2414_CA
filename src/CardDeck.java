import java.util.ArrayList;
import java.util.List;
//
public class CardDeck {

    private final int deckID;
    private ArrayList<Card> deck;

    public CardDeck(int deckID) {
        this.deckID = deckID;
        this.deck = new ArrayList<Card>();
    }
    public int getDeckID(){return deckID;}

    public ArrayList<Card> getDeck(){return deck;}

    public void addCardtoDeck(Card card) {
        deck.add(card);
    }

}
