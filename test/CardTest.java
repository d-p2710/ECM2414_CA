import org.junit.Test;
import static org.junit.Assert.*;

public class CardTest {
    @Test
    public void testGetValue(){
        Card card=new Card(0,5);
        assertEquals(5, card.getValue());
    }
    @Test
    public void testComparisonEquals(){
        Card card1=new Card(0,5);
        Card card2=new Card(1,5);
        assertTrue(card1.compareTo(card2));
    }
    @Test
    public void testComparisonNotEqual(){
        Card card1=new Card(0,5);
        Card card2=new Card(1,6);
        assertFalse(card1.compareTo(card2));
    }
}
