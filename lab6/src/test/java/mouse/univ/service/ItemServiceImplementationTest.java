package mouse.univ.service;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import mouse.univ.dto.ItemCreateDTO;
import mouse.univ.dto.ItemResponseDTO;
import mouse.univ.mapper.ItemMapper;
import mouse.univ.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemServiceImplementationTest {

    private ItemServiceImplementation itemService;
    @Autowired
    private ItemRepository itemRepository;

    private MeterRegistry meterRegistry;

    private TestNotificationService notificationService;

    private ItemMapper itemMapper;

    @BeforeEach
    public void init() {
        meterRegistry = new SimpleMeterRegistry();
        notificationService = new TestNotificationService();
        itemMapper = new ItemMapper();
        itemService = new ItemServiceImplementation(
                itemRepository,
                meterRegistry,
                notificationService,
                itemMapper);
    }

    @Test
    void state_createItem() {
        ItemCreateDTO dto = new ItemCreateDTO("honey", "100.00");
        ItemResponseDTO savedItem = itemService.createItem(dto);

        Long savedId = savedItem.getId();
        assertNotNull(savedId);

        ItemResponseDTO itemFromDB = itemService.getItem(savedId);

        assertEquals(savedId, itemFromDB.getId());
        assertEquals("honey", itemFromDB.getName());
        assertEquals(new BigDecimal("100.00"), itemFromDB.getPrice());
    }

    @Test
    void state_getItem() {
    }

    @Test
    void state_getAllItems() {
    }

    @Test
    void state_updateItem() {
    }

    @Test
    void state_deleteItem() {
    }

    @Test
    void behaviour_createItem() {
    }

    @Test
    void behaviour_getItem() {
    }

    @Test
    void behaviour_getAllItems() {
    }

    @Test
    void behaviour_updateItem() {
    }

    @Test
    void behaviour_deleteItem() {
    }
}