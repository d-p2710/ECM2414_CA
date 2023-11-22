public class Card {
    private int cardID;
    private int value;
  

    public Card(int cardID, int value) {
        this.cardID = cardID;
        this.value = value;
    }
    public int getCardID(){return cardID;}
    public int getValue() {return value;}

    public int compareTo(Card o)
    {
        if (this.getValue() == o.getValue()) {
            return 0;
        } else
            return -1;
    }

    public String toString() {
        return "Card [cardNumber=" + cardID + ", cardValue=" + value + "]";
    }

}
