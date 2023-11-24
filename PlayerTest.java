import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;


public class PlayerTest {
    private CardGame cardGame;

    @Before
    public void setup(){
        CardGame.getInstance(3);
    }

    @Test
    public void testDrawCard() throws InterruptedException, IOException {
        Player player = new Player(1);
        ArrayList<Card> deck = new ArrayList<>();
        deck.add(new Card(0, 1));
        deck.add(new Card(1, 2));

        CountDownLatch drawStarted = new CountDownLatch(1);
        Thread drawThread = new Thread(() -> {
            try {
                drawStarted.countDown(); // Signal that drawing has started
                player.drawCard(deck, 2);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });

        drawThread.start();
        // Wait for the draw thread to start
//        drawStarted.await();
        // Add a card to the deck after the draw thread has started waiting
        deck.add(new Card(2, 3));
        // Wait for the draw thread to complete
        drawThread.join();
        // Additional assertions as needed
        assertEquals(1, player.getHand().size());
        assertEquals(3, player.getHand().get(0).getValue());
    }

    @Test
    public void testDiscardToRightDeck() throws InterruptedException, IOException {
        Player player = new Player(1);
        ArrayList<Card> deck = new ArrayList<>();
        ArrayList<Card> hand = new ArrayList<>();

        Card cardToDiscard = new Card(0, 3);
        hand.add(cardToDiscard);
        assertEquals(3, hand.get(0).getValue());

        player.discardToRightDeck(deck, 2);
        System.out.println(deck);
        assertEquals(0, deck.size());
    }

    @Test
    public void testDiscardToRightDeck2() throws InterruptedException, IOException {
        Player player = new Player(1);
        ArrayList<Card> deck = new ArrayList<>();
        Card cardInHand = new Card(0, 5);
        player.addCardToHand(cardInHand);
        player.setRHSDeck(new CardDeck(2));  // Assuming RHSDeckId is set
        player.discardToRightDeck(deck, 2);
        assertEquals(1, deck.size());
        assertEquals(cardInHand, deck.get(0));
    }

    @Test
    public void testDiscardToRightDeckEmptyHand() throws InterruptedException, IOException {
        Player player = new Player(1);
        ArrayList<Card> deck = new ArrayList<>();
        player.discardToRightDeck(deck, 2);
        assertEquals(0, deck.size());
    }
    @Test
    public void testRecordWin() {
        // Create a player and ensure the win is recorded
        Player player = new Player(1);
        assertFalse(player.getHasWon());
        player.recordWin();
        assertTrue(player.getHasWon());
    }

    @Test
    public void testRecordExit() throws IOException {
        Player player = new Player(1);
        ArrayList<Card> finalHand = new ArrayList<>();
        finalHand.add(new Card(0, 1));
        finalHand.add(new Card(1, 2));

        player.recordExit(finalHand);
        String expectedOutput = "player1 exits\n" +
                "player1 final hand: 1, 2\n";
        try {
            String actualOutput = Files.readString(Paths.get("Player1_output.txt"));
            assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            fail("Error reading the output file: " + e.getMessage());
        }
    }

    @Test
    public void testChooseCardToDiscard() {
        Player player = new Player(1);
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
        Player player = new Player(1);
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(0, 5));
        hand.add(new Card(1, 6));
        player.addCardToHand(new Card(0, 5));
        player.setHand(hand);
        player.updateOutputFile();
        // Manually verify the output in the output file
    }

    @Test
    public void testCloseOutputFile() {
        Player player = new Player(1);
        player.closeOutputFile();
        // Manually verify that the output file is closed
    }

    @Test
    public void testToString() {
        Player player = new Player(1);
        player.addCardToHand(new Card(0, 5));
        player.addCardToHand(new Card(1, 6));
        assertEquals("Player 1\nPreferred denomination: 1\nHand: 5, 6", player.toString());
    }

    @After
    public void cleanup() {
        try {
            Files.deleteIfExists(Paths.get("Player1_output.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckWinCondition (){}

    @Test
    public void testRunUntilWin() throws InterruptedException, IOException {
        Player player =new Player(1);
        player.setRHSDeck(new CardDeck(1));
        player.setRHSDeck(new CardDeck(2));
        ArrayList<Card> winningHand= new ArrayList<>();
        winningHand.add(new Card(0,5));
        winningHand.add(new Card(0,5));
        winningHand.add(new Card(0,5));
        winningHand.add(new Card(0,5));
        player.setHand(winningHand);
        player.start();
        player.join();
        assertTrue(player.getHasWon());
    }
    @Test
    public void testRunWithEmptyHand() throws InterruptedException, IOException {
        Player player = new Player(1);
        player.setLHSDeck(new CardDeck(1));
        player.setRHSDeck(new CardDeck(2));

        // Simulate an empty hand
        player.setHand(new ArrayList<>());

        // Run the player's logic
        player.run();

        // Verify that the player has not won (empty hand)
        assertFalse(player.getHasWon());
    }

    @Test
    public void run() {

    }
}
