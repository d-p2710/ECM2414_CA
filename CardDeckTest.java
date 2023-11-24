import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CardDeckTest {
    private CardDeck cardDeck;
    private List<Card> deckCards;
    private String testFileName;

    @Before
    public void setUp() throws IOException{
        cardDeck=new CardDeck(1);
        deckCards=new ArrayList<>();
        testFileName="test_output.txt";
        cardDeck.outputFile=new FileWriter(testFileName);
    }

    @After
    public void tearDown(){
        cardDeck.closeOutputFile();
        Path path =Paths.get(testFileName);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        cardDeck.addCardToDeck(card1);
        cardDeck.addCardToDeck(card2);
        deckCards.add(card1);
        deckCards.add(card2);
        assertEquals(deckCards, cardDeck.getDeck());
    }

    @Test
    public void testRecordFinalState() throws IOException{
        CardDeck cardDeck=new CardDeck(1);
        Card card1=new Card(0,5);
        Card card2=new Card(1,6);
        cardDeck.addCardToDeck(card1);
        cardDeck.addCardToDeck(card2);
        cardDeck.recordFinalState(cardDeck.getDeck());
        cardDeck.closeOutputFile();
        String outputFileContent=new String(Files.readAllBytes(Paths.get("Deck1_output.txt")));
        assertTrue(outputFileContent.contains("deck1 contents: 5,6"));
    }

    @Test
    public void testWriteToOutputFile() throws IOException{
        cardDeck.writeToOutputFile("Test content its me Todd Craines \nI said its me");
        List<String> lines = Files.readAllLines(Paths.get(testFileName));
        assertEquals("Test content its me Todd Craines \nI said its me", String.join("\n", lines));
    }

    @Test
    public void testWriteToOutputFile2() throws IOException{
        //Tests if each rite starts ona  new line
        cardDeck.writeToOutputFile("Test content its me Todd Craines");
        cardDeck.writeToOutputFile("I said its me");
        List<String> lines = Files.readAllLines(Paths.get(testFileName));
        assertEquals("Test content its me Todd Craines\nI said its me", String.join("\n", lines));
    }

    @Test
    public void closeOutputFile() throws IOException {
        CardDeck cardDeck1=new CardDeck(1);
        cardDeck1.closeOutputFile();
        assertNull(cardDeck1.getOutputFile());
    }


    @Test
    public void testToString() {
        Card card1= new Card(0,5);
        Card card2= new Card(1,10);
        cardDeck.addCardToDeck(card1);
        cardDeck.addCardToDeck(card2);
        String expectedToString="CardDeck 1\nDeck: 5,10";
        assertEquals(expectedToString, cardDeck.toString());
    }

}
