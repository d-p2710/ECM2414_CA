import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CardGameTest {
    @Test
    public void testGetInstance(){
        CardGame instance1 = CardGame.getInstance();
        CardGame instance2 = CardGame.getInstance();

        assertSame(instance1, instance2);
    }

}
