import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PlayerTest {
    private CardGame cardGame;

    private void assertOutputFileContains(String expectedContent) throws IOException{
        List<String> lines = Files.readAllLines((Paths.get("Player1_output.txt")));
        assertEquals(2,lines.size());
        assertEquals(expectedContent, lines.get(1));

    }

    @Before
    public void setup(){
        cardGame=cardGame.getInstance();
    }

    @Test
    public void testGetPlayerID(){
        Player player=new Player(1,false);
        assertEquals(1,player.getPlayerID());
    }

    @Test
    public void testGetPreferredDenomination(){
        Player player=new Player(1,false);
        assertEquals(1,player.getPreferredDenomination());
    }

    @Test
    public void testSetRHSDeck() throws IOException {
        Player player=new Player(1,false);
        CardDeck rhsDeck=new CardDeck(2);
        assertEquals(rhsDeck,player.RHSDeck);
    }

    @Test
    public void testSetLHSDeck() throws IOException {
        Player player=new Player(1,false);
        CardDeck lhsDeck=new CardDeck(1);
        assertEquals(lhsDeck,player.LHSDeck);
    }

    @Test
    public void testAddCardToHand() throws IOException {
        Player player=new Player(1,false);
        Card card= new Card(0,5);
        player.addCardToHand(card);
        assertEquals(1,player.getHand().size());
        assertEquals(card, player.getHand().get(0));
    }

    @Test
    public void testGetHand(){
        Player player = new Player(1, false);
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(0,3));
        hand.add(new Card(0,3));
        hand.add(new Card(0,3));
        player.setHand(hand);
        assertEquals(hand, player.getHand());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    /* This test creates a card but doesn't add it to the player hand
    therefore we would be attempting to discard from an empty hand which
    should throw an IndexOutOfBound exception. */
    public void testDiscardToRightDeck() throws InterruptedException, IOException {
        Player player = new Player(1, false);
        player.setRHSDeck(new CardDeck(2));
        ArrayList<Card> deck = new ArrayList<>();
        Card cardToDiscard = new Card(0, 3);

        player.discardToRightDeck(deck, 2);
        assertEquals(1, deck.size());
        assertEquals(3, deck.get(0).getValue());
    }

    @Test
    public void testDiscardToRightDeck2() throws InterruptedException, IOException {
        /*
        Tests normal functionality of discardToRight method
         */
        Player player = new Player(1, false);
        player.setRHSDeck(new CardDeck(2));
        ArrayList<Card> deck = new ArrayList<>();
        Card cardInHand = new Card(0, 5);
        player.addCardToHand(cardInHand);
        player.discardToRightDeck(deck, 2);
        assertEquals(1, deck.size());
        assertEquals(cardInHand, deck.get(0));
    }

    @Test
    public void testDrawCardAndDiscardToRightDeck() throws InterruptedException, IOException {
        /* Tests that the size before & after the atomic action
        of drawing & discarding cards is always 4 */
        Player player = new Player(1, false);
        player.setLHSDeck(new CardDeck(1));
        player.setRHSDeck(new CardDeck(2));

        ArrayList<Card> lhsDeck = new ArrayList<>();
        ArrayList<Card> rhsDeck = new ArrayList<>();

        player.addCardToHand(new Card(1, 1));
        player.addCardToHand(new Card(2, 2));
        player.addCardToHand(new Card(3, 3));
        player.addCardToHand(new Card(4, 4));
        lhsDeck.add(new Card(5,5));

        assertEquals(4, player.getHand().size());
        player.drawCard(lhsDeck, 1);
        player.discardToRightDeck(rhsDeck, 2);
        assertEquals(4, player.getHand().size());
    }

    @Test
    public void testRecordWin() {
        // Create a player and ensure the win is recorded
        Player player = new Player(1, false);
        assertFalse(player.isGameOver());
        player.recordWin();
        assertTrue(player.isGameOver());
    }

    @Test
    public void testRecordFinalState() throws IOException{
        Player player= new Player(1, false);
        List<Card> finalCards=new ArrayList<>();
        finalCards.add(new Card(0,1));
        finalCards.add(new Card(1,7));
        finalCards.add(new Card(2,6));
        player.recordFinalState(finalCards);
        String expectedOutput = "player1 final hand: 1, 7, 6";
        assertOutputFileContains(expectedOutput);
    }

    @Test
    public  void testCheckWinCondition_AllSame(){
        Player player = new Player(1, false);
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(0, 1));
        hand.add(new Card(1, 1));
        hand.add(new Card(2, 1));
        hand.add(new Card(3, 1));
        assertTrue(player.checkWinCondition(hand));
        assertEquals(4,hand.size());
        assertTrue(player.isGameOver());
    }

    @Test
    public  void testCheckWinCondition_EmptyHand(){
        Player player = new Player(1, false);
        ArrayList<Card> hand = new ArrayList<>();
        assertFalse(player.checkWinCondition(hand));
        assertEquals(0,hand.size());
    }

    @Test
    public  void testCheckWinCondition_DifferentValues(){
        Player player = new Player(1, false);
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(0, 1));
        hand.add(new Card(1, 2));
        hand.add(new Card(2, 3));
        hand.add(new Card(3, 4));
        assertFalse(player.checkWinCondition(hand));
        assertEquals(4,hand.size());
        assertTrue(!player.isGameOver());
    }

    @Test
    public void testChooseCardToDiscard() {
        Player player = new Player(1, false);
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(0, 1));
        hand.add(new Card(1, 3));
        hand.add(new Card(2, 1));
        hand.add(new Card(3, 8));
        player.setHand(hand);
        int index = player.chooseCardToDiscard(hand);
        assertEquals(1, index);
    }

    @Test
    public void testUpdateOutputFile() {
        Player player = new Player(1, false);
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(0, 5));
        hand.add(new Card(1, 6));
        player.addCardToHand(new Card(0, 5));
        player.setHand(hand);
        player.updateOutputFile();
    }

    @Test
    public void testToString() {
        Player player = new Player(1, false);
        player.addCardToHand(new Card(0, 5));
        player.addCardToHand(new Card(1, 6));
        assertEquals("Player 1\nPreferred denomination: 1\nHand: 5, 6", player.toString());
    }

    @Test
    public void testRun_WinCondition() throws InterruptedException, IOException {
        // Test that the player stops running when it wins
        Player player = new Player(1, false);
        player.setLHSDeck(new CardDeck(1));
        player.setRHSDeck(new CardDeck(2));

        // Add cards to the player's hand with the same value
        player.addCardToHand(new Card(1, 1));
        player.addCardToHand(new Card(1, 1));
        player.addCardToHand(new Card(1, 1));
        player.addCardToHand(new Card(1, 1));

        // Start the player thread
        Thread playerThread = new Thread(player);
        playerThread.start();

        // Wait for the player to finish (win)
        playerThread.join(5000); // Adjust the timeout as needed

        assertTrue(player.isGameOver());
    }

    @After
    public void cleanup() {
        try {
            Files.deleteIfExists(Paths.get("Player1_output.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
