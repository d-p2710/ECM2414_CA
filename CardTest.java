import org.junit.Test;
import static org.junit.Assert.*;

public class CardTest {
    @Test
    public void testGetValue(){
        Card card=new Card(0,5);
        assertEquals(5, card.getValue());
    }
    @Test
    public void testToString(){
        Card card= new Card(1,10);
        String expectedString="Card [cardNumber=1, cardValue=10]";
        assertEquals(expectedString, card.toString());
    }
}
