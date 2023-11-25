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
    public void setRHSDeck(CardDeck RHSDeck) {this.RHSDeck = RHSDeck;}
    public void setLHSDeck(CardDeck LHSDeck) {this.LHSDeck = LHSDeck;}

    public void addCardtoHand(Card card){
        this.hand.add(card);
    }
    public ArrayList<Card> getHand() {
        return hand;
    }
    
    public void updateOutputFile(){
        try{
            this.outputFile.write("player " + this.playerID + " current hand is ");
            for (Card card : hand){
                if (card != null){
                    outputFile.write(card.getValue()+" ");
                }}
            //Flush ensures data is written immediately
            this.outputFile.write("\n");

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
                    wait();
                }
                //removes the card drawn from the LHSDeck
                Card drawnCard = deck.remove(0);
                System.out.println("\nplayer " + this.playerID + " draws a " + drawnCard.getValue() + " from deck " + deckID + "\n");
                //outputFile.write("player " + this.playerID + " draws a " + drawnCard + " from deck " + deckID + "\n");
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

        System.out.println("\nplayer "+this.playerID + " discards a " + cardToDiscard.getValue() + " to deck " + deckID + "\n");
        //outputFile.write("player "+this.playerID + " discards a " + cardToDiscard + " to deck " + deckID + "\n");
        return cardToDiscard;
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
                drawCard(LHSDeck.getDeck(), LHSDeck.getDeckID());
                if (!checkWinCondition(hand)) {
                    discardToRightDeck(RHSDeck.getDeck(), RHSDeck.getDeckID());
                }
                updateOutputFile();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

// Maybe shuffle cards, otherwise when using th same deck the result of the game could be very predictable & consistent, therefore cards need to be randomised.
