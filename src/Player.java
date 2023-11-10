public class Player {
    private int playerID;
    private int preferredDenomination;
    private Card[] hand = new Card[4];
    private int preferredCardCount;
    public Player(int playerID) {
        this.playerID = playerID;
        this.hand = new Card[4];
        this.preferredDenomination = playerID;
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

    public void addCard(Card card){
        this.hand.add(card);
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
