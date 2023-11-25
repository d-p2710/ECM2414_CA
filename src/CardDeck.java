import java.util.ArrayList;
import java.util.Collections;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CardDeck {

    private final int deckID;
    private ArrayList<Card> deck;
    private FileWriter outputFile;

    public CardDeck(int deckID) throws IOException {
        this.deckID = deckID;
        this.deck = new ArrayList<Card>();
        String outputFileName = "Deck" + deckID + "_output.txt";
        this.outputFile = new FileWriter(outputFileName);
    }
    public int getDeckID(){return deckID;}

    public ArrayList<Card> getDeck(){return deck;}

    public void addCardtoDeck(Card card) {
        deck.add(card);
    }

    public String toString() {
        StringBuilder deckString = new StringBuilder("CardDeck " +deckID + "\nDeck: ");
        for (Card card: deck) {
            deckString.append(card.getValue()).append(",");
        }
        if (!deck.isEmpty()) {
            deckString.deleteCharAt(deckString.length()-1);
        }
        return deckString.toString();
    }
}

