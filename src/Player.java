import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player extends Thread{
    public int playerID;
    public int preferredDenomination;
    private ArrayList<Card> hand;
    private FileWriter outputFile;
    private int LHSDeckId;
    private int RHSDeckId;
    private volatile boolean hasWon;
    private CardGame cardGame;


    public Player(int playerID, CardGame cardGame) {
        this.playerID = playerID;
        this.preferredDenomination = playerID;
        this.hand = new ArrayList<>();
        this.hasWon=false;
        this.cardGame=cardGame;
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
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void setCardGame(CardGame cardGame) {
        this.cardGame = cardGame;
    }


    public void addCardToHand(int index, Card card){
        if (index >= hand.size()){
            this.hand.add(index, card);
        } else {
            this.hand.add(card);
        }
    }

    public CardDeck getRHSDeck(int RHSDeckId){
        return cardGame.getDeckById(RHSDeckId);
    }

    public CardDeck getLHSDeck(int LHSDeckId){
        return cardGame.getDeckById(LHSDeckId);
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
            }
            Card drawnCard = deck.remove(0);
            outputFile.write("player " + this.playerID + " draws a " + drawnCard + " from deck " + deckID + "\n");
            int empty_index = -1;
            if (!hand.isEmpty()) {
                empty_index = chooseCardToDiscard(hand);
            }
            Card cardToDiscard = null;
            if (empty_index != -1) {
                cardToDiscard = hand.get(empty_index);
                hand.set(empty_index, drawnCard);
            }

            notifyAll();
            return cardToDiscard;
        }
    }

    public synchronized void discardToRightDeck(ArrayList<Card> deck, Card cardToDiscard, int deckID) throws InterruptedException, IOException {
        deck.add(cardToDiscard);
        outputFile.write("player "+this.playerID + " discards a " + cardToDiscard + " to deck " + deckID + "\n");
        notifyAll();
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
        while (!hasWon) {
            Thread.onSpinWait();
            try {
                CardDeck lhsDeck=getLHSDeck(LHSDeckId);
                CardDeck rhsDeck=getRHSDeck(RHSDeckId);
                Card cardToDiscard = drawCard(lhsDeck.getDeck(), LHSDeckId);
                discardToRightDeck(rhsDeck.getDeck(), cardToDiscard, RHSDeckId);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

// Maybe shuffle cards, otherwise when using th same deck the result of the game could be very predictable & consistent, therefore cards need to be randomised.
