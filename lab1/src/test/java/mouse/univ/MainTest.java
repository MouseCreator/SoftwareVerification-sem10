package mouse.univ;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testRuns() {
       assertEquals(25, 10 + 15);
       assertNotEquals(35, 10 + 15);
    }
}