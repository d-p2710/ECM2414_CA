import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CardGameTest {
    @Test
    public void testLoadAndValidatePackFile_validFile(){

    }
    @Test
    public void testLoadAndValidatePackFile_nonExistentFile() {

    }
    @Test
    public void testLoadAndValidatePackFile_invalidContent(){

    }
    @Test
    public void testLoadAndValidatePackFile_insufficientLines(){

    }
    @Test
    public void roundRobinAllocation(){

    }
    @Test
    public void roundRobinPlayersThenCardDecks(){}
    @Test
    public void cardDecksAppendedToDecks(){}
    @Test
    public void testPlayerAndDeckInitialisation(){
        CardGame game = CardGame.getInstance();
    }
    @Test
    public void testThreadsStartConcurrently(){
        /*game play should start concurrently, initially checks cards dealt
        if a winning hand as all the same value if not threads start to play
        the game drawing & discarding no-preferred denomination cards until
        all hand == preferred denomination.
         */
    }
    @Test
    public void testPlayGame(){}

    @Test
    public void decksArrayList(){
        /* Should be an ArrayList of cardDecks in the format of
        [cardDeckID0: [card1.getValue(), card2.getValue()...card_.getValue()],
        cardDeckID1: [card1.getValue(), card2.getValue()...card_.getValue()],
        cardDeckID2: [card1.getValue(), card2.getValue()...card_.getValue()]]
         */
        }
    }
