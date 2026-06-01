package mouse.univ;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


    @TestMethodOrder(MethodOrderer.DisplayName.class)
    class DisplayNameOrderedTest {

        @Test
        @DisplayName("1 - create user")
        void create() {}

        @Test
        @DisplayName("2 - update user")
        void update() {}
    }
