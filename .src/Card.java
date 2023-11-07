public class Card {
    private int value;

    public static CardList<Card> cards = new CardList<>();
    public Card(int value) {
        this.value = value;
    }
    public int getValue() {return value;}

}
