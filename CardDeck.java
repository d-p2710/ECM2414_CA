import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CardDeck {
    private final int deckID;
    protected ArrayList<Card> deck;
    protected FileWriter outputFile;

    public FileWriter getOutputFile() {
        return outputFile;
    }

    public CardDeck(int deckID) throws IOException{
        this.deckID = deckID;
        this.deck = new ArrayList<>();
        String outputFileName = "Deck" + deckID + "_output.txt";
        this.outputFile = new FileWriter(outputFileName);
    }
    public int getDeckID(){return deckID;}
    public ArrayList<Card> getDeck(){return deck;}

    public void addCardToDeck(Card card) {
        deck.add(card);
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

    public void writeToOutputFile(String output) {
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
            outputFile=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString(){
        StringBuilder deckString=new StringBuilder("CardDeck "+deckID+ "\nDeck: ");
        for (Card card: deck){
            deckString.append(card.getValue()).append(",");
        }
        if (!deck.isEmpty()){
            deckString.deleteCharAt(deckString.length()-1);
        }
        return deckString.toString();
    }
}