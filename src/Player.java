import java.io.FileWriter;
import java.io.IOException;


public class Player {
    public int playerID;
    public int preferredDenomination;
    private Card[] hand;
    private FileWriter outputFile;
    private int preferredCardCount;

    public Player(int playerID) {
        this.playerID = playerID;
        this.preferredDenomination = playerID;
        this.hand = new Card[CardGame.MAX_HAND_SIZE];
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

    public Card[] getHand() {
        return hand;
    }

    public void updateOutputFile(){
        try{
            for (Card card : hand){
                if (card != null){
                    outputFile.write(card.getValue()+"");
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

    public void addCard(Card card){
        this.hand.add(card);
    }

    private boolean checkWinCondition(){
        return true;
    }

    public synchronized void drawCard(Card card) {
        if (card.getValue() == preferredDenomination) {
            // would need to discard a non preferred card to the right deck before adding a new card to the deck
            for (int i = 0; i < hand.length; i++) {
                if (hand[i] == null) {
                    hand[i] = card;
                    return;
                }
            }
        } else {
            // discard to the right deck
            if (nonPreferredCardCount)
        }
    }

    public synchronized void discardToRightDeck(CardDeck[] decks, int currentPlayerID) {
        // discard a non-preferred card to the right deck
    }

    //
}
