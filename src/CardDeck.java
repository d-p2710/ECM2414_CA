import java.util.ArrayList;
public class CardDeck {

    private final int deckID;
    private ArrayList<Card> deck;

    public CardDeck(int deckID) {
        this.deckID = deckID;
        this.deck = new ArrayList<>();
    }
    public int getDeckID(){return deckID;}
    public ArrayList<Card> getDeck(){return deck;}

    public void addCardToDeck(int index,Card card) {
        if (index >= deck.size()){
            this.deck.add(card);
        } else {
            this.deck.add(index, card);
        }
    }

}
