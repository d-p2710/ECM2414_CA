public class Card {
    private int cardID;
    private int value;
    private static int totalCardNumber = 0;

    public static CardList<Card> cards = new CardList<>();
    public Card(int value) {
        this.cardID = totalCardNumber++;
        this.value = value;
    }
    public int getCardID(){return cardID;}
    public int getValue() {return value;}
}
