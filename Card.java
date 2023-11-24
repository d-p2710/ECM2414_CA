public class Card {
    private final int cardID;
    private final int value;
    public Card(int cardID, int value) {
        this.cardID = cardID;
        this.value = value;
    }
    public int getValue() {return value;}

    public String toString() {
        return "Card [cardNumber=" + cardID + ", cardValue=" + value + "]";
    }

}