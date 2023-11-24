import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
//

public class Player implements Runnable{
    public int playerID;
    public int preferredDenomination;
    private ArrayList<Card> hand;
    private FileWriter outputFile;
    private int preferredCardCount;
    private CardDeck LHSDeck;
    private CardDeck RHSDeck;

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
    public void setRHSDeck(CardDeck RHSDeckId) {this.RHSDeck = RHSDeck;}
    public void setLHSDeck(CardDeck LHSDeckId) {this.LHSDeck = LHSDeck;}

    public void addCardtoHand(Card card){
        this.hand.add(card);
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
    public static boolean checkWinCondition(ArrayList<Card> hand) {
        if (hand == null || hand.size() == 0) {
            // Handle edge cases, like an empty array or null reference
            return false;
        }

        int firstNumber = hand.get(0).getValue();

        for (int i = 1; i < hand.size(); i++) {
            if (hand.get(i).getValue() != firstNumber) {
                // If any number is different, return false
                return false;
            }
        }

        // All numbers are the same
        return true;
    }
    @Override
    public String toString() {
        StringBuilder handList= new StringBuilder();
        for (int i = 0; i < hand.size(); i++) {
            handList.append(hand.get(i).getValue());
            if (i < hand.size() - 1) {
                handList.append(", ");
            }
        }
        return "Player "+ playerID + "\nPreferred denomination: " + preferredDenomination+"\nHand: "+handList;
    }
    @Override
    public void run() {
        while(!checkWinCondition(hand)) {
            try {
                Card cardToDiscard = drawCard(LHSDeck.getDeck(), LHSDeck.getDeckID());
                discardToRightDeck(RHSDeck.getDeck(), cardToDiscard, RHSDeck.getDeckID());
                updateOutputFile();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
