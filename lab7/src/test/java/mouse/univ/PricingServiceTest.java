package mouse.univ;

import mouse.univ.model.Item;
import mouse.univ.repository.DiscountRepository;
import mouse.univ.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
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
}