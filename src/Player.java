import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//

public class Player implements Runnable{
    public int playerID;
    public int preferredDenomination;
    private ArrayList<Card> hand;
    private FileWriter outputFile;
    CardDeck LHSDeck;
    CardDeck RHSDeck;
    private volatile boolean gameOver;


    public boolean isGameOver() {
        return gameOver;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }
    public Player(int playerID, boolean gameOver) {
        this.playerID = playerID;
        this.preferredDenomination = playerID;
        this.hand = new ArrayList<Card>();
        this.gameOver = gameOver;
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
    public void setRHSDeck(CardDeck RHSDeck) {this.RHSDeck = RHSDeck;}
    public void setLHSDeck(CardDeck LHSDeck) {this.LHSDeck = LHSDeck;}

    public void addCardToHand(Card card){
        this.hand.add(card);
    }
    public ArrayList<Card> getHand() {
        return hand;
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

    public synchronized void recordWin() {
        String output = "player " + playerID + " wins";
        System.out.println(output);
        writeToOutputFile(output);
        gameOver = true;
    }

    public void recordFinalState(List<Card> finalCards) {
        StringBuilder output = new StringBuilder("player" + playerID + " final hand: ");
        int cardCount = finalCards.size();

        for (int i = 0; i < cardCount; i++) {
            output.append(finalCards.get(i).getValue());
            if (i < cardCount - 1) {
                output.append(", ");
            }
        }
        writeToOutputFile("player" + playerID + " exits");
        writeToOutputFile(output.toString());
    }

    void writeToOutputFile(String output) {
        try {
            outputFile.write(output + "\n");
            outputFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //returns the index of the card to be discarded.
    int chooseCardToDiscard(ArrayList<Card> hand) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getValue() != preferredDenomination) {
                return i;
            }
        }
        return -1;
        //has to check win condition before this is called, so shouldnt return -1
    }
    //takes in the LHSDeck and its deckID
    public Card drawCard(ArrayList<Card> deck, int deckID) throws InterruptedException, IOException {
        while (true) {
            synchronized (this) {
                //doesnt draw a card while the LHSDeck is empty
                while (deck.isEmpty()) {
                    wait(100);
                }
                //removes the card drawn from the LHSDeck
                Card drawnCard = deck.remove(0);
                writeToOutputFile("\nplayer " + this.playerID + " draws a " + drawnCard.getValue() + " from deck " + deckID + "\n");
                //adds the card drawn to the hand
                hand.add(drawnCard);
                //returns drawn card
                return drawnCard;

            }
        }
    }
    //takes in the RHSDeck and its deckID
    public synchronized Card discardToRightDeck(ArrayList<Card> deck, int deckID) throws InterruptedException, IOException {
        //chooses a card to discard
        int discardCardIndex = chooseCardToDiscard(hand);
        Card cardToDiscard = hand.get(discardCardIndex);
        //removes the card from hand
        hand.remove(discardCardIndex);
        //adds the discarded card to RHSDeck
        deck.add(cardToDiscard);
        notifyAll();
        writeToOutputFile("\nplayer "+this.playerID + " discards a " + cardToDiscard.getValue() + " to deck " + deckID + "\n");
        if(checkWinCondition(hand)) {
            notifyAll();
        }
        return cardToDiscard;
    }
    public boolean checkWinCondition(ArrayList<Card> hand) {
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
        recordWin();
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
    public void run(){
        try{
            while(!gameOver && !Thread.interrupted()){
                synchronized (this){
                    if (checkWinCondition(hand)){
                        recordWin();
                    }
                }
                if(!gameOver){
                    try{
                        drawCard(LHSDeck.getDeck(), LHSDeck.getDeckID());
                        if(!checkWinCondition(hand)){
                            discardToRightDeck(RHSDeck.getDeck(), RHSDeck.getDeckID());
                        }
                        updateOutputFile();
                    } catch (IOException e){
                        throw new RuntimeException(e);
                    }
                }
            }
            gameOver=true;
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
