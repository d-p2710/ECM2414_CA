import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player extends Thread{
    public int playerID;
    public int preferredDenomination;
    private ArrayList<Card> hand;
    private FileWriter outputFile;
    private CardDeck LHSDeck;
    private CardDeck RHSDeck;

    public boolean getHasWon() {
        return hasWon;
    }

    private volatile boolean hasWon;


    public Player(int playerID) {
        this.playerID = playerID;
        this.preferredDenomination = playerID;
        this.hand = new ArrayList<>();
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

    public ArrayList<Card> getHand() {
        return hand;
    }
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void addCardToHand(Card card){
        this.hand.add(card);
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
        StringBuilder output = new StringBuilder("player" + playerID + " final hand: ");
        int cardCount = finalCards.size();

        for (int i = 0; i < cardCount; i++) {
            output.append(finalCards.get(i).getValue());
            if (i < cardCount - 1) {
                output.append(", ");
            }
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
    int chooseCardToDiscard(ArrayList<Card> hand) {
        for (int i = 0; i< hand.size();i++){
            if (hand.get(i).getValue() != preferredDenomination) {
                return i;
            }
        }
        //has to check win condition before this is called, so shouldn't return -1
        return -1;
    }

    public Card drawCard(ArrayList<Card> deck, int deckID) throws InterruptedException, IOException {
        synchronized (this) {
            while (deck.isEmpty()) {
                wait();
                if (deck.isEmpty()){
                    return null;
                }
            }
            Card drawnCard = deck.remove(0);
            outputFile.write("player " + this.playerID + " draws a " + drawnCard + " from deck " + deckID + "\n");
            //adds the card drawn to the hand
            hand.add(drawnCard);
            //returns drawn card
            return drawnCard;
        }
    }

    public synchronized Card discardToRightDeck(ArrayList<Card> deck, int deckID) throws InterruptedException, IOException {
        //chooses a card to discard
        int discardCardIndex = chooseCardToDiscard(hand);
        if(discardCardIndex!=-1){
            Card cardToDiscard = hand.get(discardCardIndex);
            //removes the card from hand
            hand.remove(discardCardIndex);
            //adds the discarded card to RHSDeck
            deck.add(cardToDiscard);
            outputFile.write("player "+this.playerID + " discards a " + cardToDiscard + " to deck " + deckID + "\n");
            // Add debug print
            System.out.println("Player " + this.playerID + " discarded a card. Deck size: " + deck.size());
            return cardToDiscard;
        } else{
            return null;
        }
    }
    public static boolean checkWinCondition(ArrayList<Card> hand) {
        if (hand == null || hand.size() == 0) {
            // Handle edge cases, like an empty array or null reference
            return false;
        }
        int firstNumber = hand.get(0).getValue();
        for (int i = 1; i < hand.size(); i++) {
            if (hand.get(0).getValue() != firstNumber) {
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
        while (!checkWinCondition(hand)) {
            Thread.onSpinWait();
            try {
                drawCard(LHSDeck.getDeck(), LHSDeck.getDeckID());
                discardToRightDeck(RHSDeck.getDeck(), RHSDeck.getDeckID());
                updateOutputFile();
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

// Maybe shuffle cards, otherwise when using th same deck the result of the game could be very predictable & consistent, therefore cards need to be randomised.