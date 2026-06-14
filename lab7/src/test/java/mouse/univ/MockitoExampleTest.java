package mouse.univ;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class MockitoExampleTest {
    @Test
    void shouldReturnMockedValue() {
        Calculator calculator = mock(Calculator.class);

        when(calculator.add(2, 3)).thenReturn(5);

        int result = calculator.add(2, 3);

        assertEquals(5, result);
        verify(calculator).add(2, 3);
    }

    interface Calculator {
        int add(int a, int b);
    }
}
