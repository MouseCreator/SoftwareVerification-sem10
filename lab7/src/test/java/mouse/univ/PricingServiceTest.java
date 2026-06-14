package mouse.univ;

import mouse.univ.exception.ConnectionException;
import mouse.univ.model.Item;
import mouse.univ.repository.DiscountRepository;
import mouse.univ.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sound.midi.InvalidMidiDataException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PricingServiceTest {

    private PricingService pricingService;
    private ItemRepository itemMock;
    private DiscountRepository discountMock;

    @BeforeEach
    void setUp() {
        itemMock = Mockito.mock(ItemRepository.class);
        discountMock = Mockito.mock(DiscountRepository.class);
        pricingService = new PricingService(itemMock, discountMock);
    }

    @Test
    void testCorrectDiscount() {
        Mockito.when(itemMock.findById(1L)).thenReturn(
                Optional.of(new Item(1L, "Cheeseburger", new BigDecimal("125.00"))));
        Mockito.when(discountMock.getDiscount(1L)).thenReturn(Optional.of(new BigDecimal("0.1")));

        BigDecimal price = pricingService.getPrice(1L);
        assertEquals(new BigDecimal("112.50"), price);
    }

    @Test
    void testRoundingDiscount() {
        Mockito.when(itemMock.findById(2L)).thenReturn(
                Optional.of(new Item(2L, "Fries", new BigDecimal("99.25"))));
        Mockito.when(discountMock.getDiscount(2L)).thenReturn(Optional.of(new BigDecimal("0.17")));

        BigDecimal price = pricingService.getPrice(2L);
        assertEquals(new BigDecimal("82.38"), price);
    }

    @Test
    void testNoDiscount() {
        Mockito.when(itemMock.findById(3L)).thenReturn(
                Optional.of(new Item(3L, "Orange Juice", new BigDecimal("80.60"))));
        Mockito.when(discountMock.getDiscount(3L)).thenReturn(Optional.empty());

        BigDecimal price = pricingService.getPrice(3L);
        assertEquals(new BigDecimal("80.60"), price);
    }

    @Test
    void testItemNotFound() {
        Mockito.when(itemMock.findById(3L)).thenReturn(
                Optional.of(new Item(3L, "Orange Juice", new BigDecimal("80.60"))));
        Mockito.when(discountMock.getDiscount(3L)).thenReturn(Optional.empty());

        BigDecimal price = pricingService.getPrice(3L);
        assertEquals(new BigDecimal("80.60"), price);
    }

    @Test
    void testIllegalDiscount() {
        Mockito.when(itemMock.findById(4L)).thenReturn(
                Optional.of(new Item(4L, "Sandwich", new BigDecimal("300.60"))));
        Mockito.when(discountMock.getDiscount(4L)).thenReturn(Optional.of(new BigDecimal("1.1")));
        assertThrows(InvalidMidiDataException.class, ()->pricingService.getPrice(4L));
    }

    @Test
    void testDynamicDiscount() {
        HashMap<Long, BigDecimal> dynamicDiscounts = new HashMap<>();

        Mockito.when(discountMock.getDiscount(Mockito.anyLong()))
                .thenAnswer(invocation -> {
                    Long argument = invocation.getArgument(0, Long.class);
                    return Optional.ofNullable(dynamicDiscounts.get(argument));
                });

        Mockito.when(itemMock.findById(5L)).thenReturn(
                Optional.of(new Item(5L, "Soda", new BigDecimal("104.20"))));
        BigDecimal price = pricingService.getPrice(5L);
        assertEquals(new BigDecimal("104.20"), price);

        dynamicDiscounts.put(5L, new BigDecimal("0.5"));
        price = pricingService.getPrice(5L);
        assertEquals(new BigDecimal("52.10"), price);
    }

    @Test
    void testTransitiveThrow() {
        Mockito.when(itemMock.findById(3L)).thenThrow(new ConnectionException("Cannot connect to database"));

        assertThrows(ConnectionException.class, ()-> pricingService.getPrice(3L));
    }
}