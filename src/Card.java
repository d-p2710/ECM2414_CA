public class Card {
    private int cardID;
    private int value;
    //private static int totalCardNumber = 0;
  

    public Card(int cardID, int value) {
        this.cardID = cardID;
        this.value = value;
    }
    public int getCardID(){return cardID;}
    public int getValue() {return value;}


    public String toString()
    {
        return "Card [cardNumber=" + cardID + ", cardValue=" + value + "]";
    }

}

