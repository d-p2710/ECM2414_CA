public class Card {
    private int cardID;
    private int value;

    public static CardList<Card> cards = new CardList<>();
    public Card(int value) {
        this.cardID = cards.size();
        this.value = value;

    }
    public int getValue() {return value;}

}
