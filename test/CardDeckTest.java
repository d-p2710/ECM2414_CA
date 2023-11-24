import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CardDeckTest {
    private CardDeck cardDeck;
    private List<Card> deckCards;

    @Before
    public void setUp() throws IOException{
        cardDeck=new CardDeck(1);
        deckCards=new ArrayList<>();
    }

    @After
    public void tearDown(){
        cardDeck.closeOutputFile();
        File outputFile=new File("Deck1_output.txt");
        outputFile.delete();
    }

    @Test
    public void testGetDeckId(){
        int expectedDeckID=1;
        int actualDeckID= cardDeck.getDeckID();
        assertEquals(expectedDeckID, actualDeckID);
    }

    @Test
    public void testGetDeck(){
        ArrayList<Card> expectedDeckID=new ArrayList<>();
        ArrayList<Card> actualDeckID= cardDeck.getDeck();
        assertEquals(expectedDeckID, actualDeckID);
    }

    @Test
    public void testAddCardToDeck() {
        Card card1=new Card(0,5);
        Card card2=new Card(1,6);
        cardDeck.addCardToDeck(0,card1);
        cardDeck.addCardToDeck(1, card2);
        deckCards.add(card1);
        deckCards.add(card2);
        assertEquals(deckCards, cardDeck.getDeck());
    }

    @Test
    public void testRecordFinalState() throws IOException{
        Card card1=new Card(0,5);
        Card card2=new Card(1,6);
        cardDeck.addCardToDeck(0,card1);
        cardDeck.addCardToDeck(1, card2);
        cardDeck.recordFinalState(cardDeck.getDeck());
        String outputFileContent=new String(Files.readAllBytes(Paths.get("Deck1_output.txt")));
        assertTrue(outputFileContent.contains("deck1 contents: 5,6"));
    }
}
