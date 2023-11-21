import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
//

public class Player{
    public int playerID;
    public int preferredDenomination;
    private ArrayList<Card> hand;
    private FileWriter outputFile;
    private int preferredCardCount;
    private int LHSDeckId;
    private int RHSDeckId;

    public Player(int playerID) {
        this.playerID = playerID;
        this.preferredDenomination = playerID;
        this.hand = new ArrayList<Card>();
        try{
            String outputFileName = "Player" +playerID+ "_output.txt";
            this.outputFile=new FileWriter(outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getPreferredDenomination() {
        return preferredDenomination;
    }
    public void setRHSDeckId(int RHSDeckId) {this.RHSDeckId = RHSDeckId;}
    public void setLHSDeckId(int LHSDeckId) {this.LHSDeckId = LHSDeckId;}

    public void addCardtoHand(int index, Card card){
        this.hand.set(index, card);
    }
    public ArrayList<Card> getHand() {
        return hand;
    }

    public void updateOutputFile(){
        try{
            outputFile.write("player " + this.playerID + " current hand is ");
            for (Card card : hand){
                if (card != null){
                    outputFile.write(card.getValue()+" ");
                }}
            //Flush ensures data is written immediately
            outputFile.write("\n");
            outputFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeOutputFile(){
        try{
            outputFile.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //returns the index of the card to be discarded.
    private int chooseCardToDiscard(ArrayList<Card> hand) {
        for (int i = 0; i< hand.size();i++){
            if (hand.get(i).getValue() != preferredDenomination) {
                return i;
            }
        }
        //has to check win condition before this is called, so shouldnt return -1
        return -1;
    }


    public Card drawCard(ArrayList<Card> deck, int deckID) throws InterruptedException, IOException {
        while (true) {
            synchronized (this) {
                while (deck.isEmpty()) {
                    wait();
                }
                Card drawnCard = deck.remove(0);
                outputFile.write("player " + this.playerID + " draws a " + drawnCard + " from deck " + deckID + "\n");
                int empty_index = chooseCardToDiscard(hand);
                Card cardToDiscard = hand.get(empty_index);
                hand.set(empty_index, drawnCard);
                return cardToDiscard;

            }
        }
    }

    public synchronized void discardToRightDeck(ArrayList<Card> deck, Card cardToDiscard, int deckID) throws InterruptedException, IOException {
        deck.add(cardToDiscard);
        outputFile.write("player "+this.playerID + " discards a " + cardToDiscard + " to deck " + deckID + "\n");
    }

}
