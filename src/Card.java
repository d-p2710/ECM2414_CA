import java.util.ArrayList;
import java.util.List;


public class Card {
    private int cardID;
    private int value;
    private static int totalCardNumber = 0;
  
    public static List<Card> cards = new ArrayList<>();
    public Card(int value) {
        this.cardID = totalCardNumber++;
        this.value = value;
        cards.add(this);
    }
    public int getCardID(){return cardID;}
    public int getValue() {return value;}
}
