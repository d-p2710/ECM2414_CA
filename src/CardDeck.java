import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CardDeck {
    private final int deckID;
    private ArrayList<Card> deck;
    private FileWriter outputFile;

    public CardDeck(int deckID) throws IOException{
        this.deckID = deckID;
        this.deck = new ArrayList<>();
        String outputFileName = "Deck" + deckID + "_output.txt";
        this.outputFile = new FileWriter(outputFileName);
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
    public void recordFinalState(List<Card> finalCards) {
        StringBuilder output = new StringBuilder("deck" + deckID + " contents: ");
        for (Card card : finalCards) {
            output.append(card.getValue()).append(",");
        }
        if(!finalCards.isEmpty()){
            output.deleteCharAt(output.length()-1);
        }
        writeToOutputFile(output.toString());
    }

    private void writeToOutputFile(String output) {
        try {
            outputFile.write(output + "\n");
            outputFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeOutputFile() {
        try {
            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString(){
        return "CardDeck [cardDeck Number "+deckID+"\nDeck: "+deck+"]";
    }
}
