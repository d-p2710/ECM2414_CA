public class Card {
    private int cardID;
    private int value;
    public Card(int cardID, int value) {
        this.cardID = cardID;
        this.value = value;
    }
    public int getValue() {return value;}

    public boolean compareTo(Card o) {
        if (this.getValue() == o.getValue()) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "Card [cardNumber=" + cardID + ", cardValue=" + value + "]";
    }

}
