import java.util.ArrayList;
import java.util.List;

public class CardDeck {

    private final int deckID;
    private List<Card>cards;

    public CardDeck(int deckID) {
        this.deckID = deckID;
        this.cards=new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }
}
