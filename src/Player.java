import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player extends Thread{
    public int playerID;
    public int preferredDenomination;
    private ArrayList<Card> hand;
    private FileWriter outputFile;
    private int preferredCardCount;
    private int LHSDeckId;
    private int RHSDeckId;
    private volatile boolean hasWon;


    public Player(int playerID) {
        this.playerID = playerID;
        this.preferredDenomination = playerID;
        this.hand = new ArrayList<>();
        this.hasWon=false;
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

    public boolean getHasWon() {
        return hasWon;
    }
    public ArrayList<Card> getHand() {
        return hand;
    }
    public int getLHSDeckId() {
        return LHSDeckId;
    }
    public int getRHSDeckId() {
        return RHSDeckId;
    }

    public void addCardToHand(int index, Card card){
        if (index >= hand.size()){
            this.hand.add(index, card);
        } else {
            this.hand.add(card);
        }
    }

    public synchronized void updateOutputFile(){
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

    public synchronized void recordWin() {
        String output = "player" + playerID + " wins";
        System.out.println(output);
        writeToOutputFile(output);
        hasWon = true;
    }

    public void recordExit(List<Card> finalHand) {
        String output = "player" + playerID + " exits";
        System.out.println(output);
        writeToOutputFile(output);
        recordFinalState(finalHand);
    }

    public void recordFinalState(List<Card> finalCards) {
        StringBuilder output = new StringBuilder("deck" + playerID + " final hand: ");
        for (Card card : finalCards) {
            output.append(card.getValue());
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

    //returns the index of the card to be discarded.
    private int chooseCardToDiscard(ArrayList<Card> hand) {
        for (int i = 0; i< hand.size();i++){
            if (hand.get(i).getValue() != preferredDenomination) {
                return i;
            }
        }
        //has to check win condition before this is called, so shouldn't return -1
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
                notifyAll();
                return cardToDiscard;
            }
        }
    }

    public synchronized void discardToRightDeck(ArrayList<Card> deck, Card cardToDiscard, int deckID) throws InterruptedException, IOException {
        deck.add(cardToDiscard);
        outputFile.write("player "+this.playerID + " discards a " + cardToDiscard + " to deck " + deckID + "\n");
        notifyAll();
    }

    /*
    public synchronized void discardToRightDeck(Card card, int RHSDeckId) {
        // discard a non-preferred card to the right deck
        hand.remove(card);
        writeToOutputFile("player " + playerID + "discards a ");
        recordDiscard(card, playerID, RHSDeckId);
    }
    public synchronized void drawCard(Card card, int LHSDeckId) {
        synchronized (this) {
            if (card.getDenomination() == preferredDenomination) {
                for (int i = 0; i < hand.size(); i++) {
                    if (hand.get(i) == null) {
                        hand.set(i, card);
                        recordDraw(card, playerID, LHSDeckId);
                        return;
                    }
                }
            } else {
                discardToRightDeck(card, RHSDeckId);
            }
        }
    }

    */
//    @Override
//    public void run() {
//        while (!hasWon) {
//            Thread.onSpinWait();
//        }
//    }
}

// Maybe shuffle cards, otherwise when using th same deck the result of the game could be very predictable & consistent, therefore cards need to be randomised.