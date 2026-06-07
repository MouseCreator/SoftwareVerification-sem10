package mouse.univ.service;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jakarta.persistence.EntityNotFoundException;
import mouse.univ.dto.ItemCreateDTO;
import mouse.univ.dto.ItemResponseDTO;
import mouse.univ.dto.ItemUpdateDTO;
import mouse.univ.mapper.ItemMapper;
import mouse.univ.model.Notification;
import mouse.univ.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemServiceImplementationTest {

    private ItemServiceImplementation itemService;
    @Autowired
    private ItemRepository itemRepository;

    private MeterRegistry meterRegistry;

    private TestNotificationService notificationService;

    @BeforeEach
    public void init() {
        meterRegistry = new SimpleMeterRegistry();
        notificationService = new TestNotificationService();
        ItemMapper itemMapper = new ItemMapper();
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
        ItemCreateDTO dto = new ItemCreateDTO("butter", "50.00");
        ItemResponseDTO savedItem = itemService.createItem(dto);
        Long id = savedItem.getId();
        ItemResponseDTO item = itemService.getItem(id);
        assertEquals(item, savedItem);
        assertThrows(EntityNotFoundException.class, ()->itemService.getItem(-100L));
    }

    @Test
    void state_getAllItems() {
        ItemCreateDTO dto = new ItemCreateDTO("butter", "50.00");
        ItemResponseDTO savedItem = itemService.createItem(dto);
        List<ItemResponseDTO> allItems = itemService.getAllItems();
        assertFalse(allItems.isEmpty());
        assertTrue(allItems.contains(savedItem));
    }

    @Test
    void state_updateItem() {
        ItemCreateDTO dto = new ItemCreateDTO("bread", "25.00");
        ItemResponseDTO savedItem = itemService.createItem(dto);
        Long id = savedItem.getId();

        ItemUpdateDTO updateDTO = new ItemUpdateDTO(id, "baguette", "30.00");
        itemService.updateItem(updateDTO);

        ItemResponseDTO itemFromDB = itemService.getItem(id);

        assertEquals(id, itemFromDB.getId());
        assertEquals("baguette", itemFromDB.getName());
        assertEquals(new BigDecimal("30.00"), itemFromDB.getPrice());
    }

    @Test
    void state_deleteItem() {
        ItemCreateDTO dto = new ItemCreateDTO("salt", "10.00");
        ItemResponseDTO savedItem = itemService.createItem(dto);
        Long id = savedItem.getId();
        assertNotNull(itemService.getItem(id));
        itemService.deleteItem(id);
        assertThrows(EntityNotFoundException.class, ()->itemService.getItem(id));
    }

    @Test
    void behaviour_createItem() {
        ItemCreateDTO dto = new ItemCreateDTO("carrot", "15.00");
        ItemResponseDTO item = itemService.createItem(dto);
        Long id = item.getId();
        double currentCount = meterRegistry.counter("items_created").count();
        assertEquals(1.0, currentCount);
        List<Notification> allNotifications = notificationService.getAllNotifications();
        assertEquals(1, allNotifications.size());
        assertEquals("Item " + id + " has been created", allNotifications.getFirst().getMessage());
    }

    @Test
    void behaviour_getItem() {
        ItemCreateDTO dto = new ItemCreateDTO("potato", "12.00");
        ItemResponseDTO item = itemService.createItem(dto);
        Long id = item.getId();

        List<Notification> notificationsBefore = notificationService.getAllNotifications();
        itemService.getItem(id);
        List<Notification> notificationsAfter = notificationService.getAllNotifications();
        assertEquals(notificationsBefore, notificationsAfter);
    }

    @Test
    void behaviour_getAllItems() {
        ItemCreateDTO dto = new ItemCreateDTO("grape", "5.25");
        itemService.createItem(dto);

        List<Notification> notificationsBefore = notificationService.getAllNotifications();
        itemService.getAllItems();
        List<Notification> notificationsAfter = notificationService.getAllNotifications();
        assertEquals(notificationsBefore, notificationsAfter);
    }

    @Test
    void behaviour_updateItem() {
        ItemCreateDTO dto = new ItemCreateDTO("orange", "7.00");
        ItemResponseDTO item = itemService.createItem(dto);
        Long id = item.getId();

        ItemUpdateDTO updateDTO = new ItemUpdateDTO(id, "orange", "6.80");
        itemService.updateItem(updateDTO);

        double currentCount = meterRegistry.counter("items_updated").count();
        assertEquals(1.0, currentCount);
        List<Notification> allNotifications = notificationService.getAllNotifications();
        assertEquals(2, allNotifications.size());
        assertEquals("Item " + id + " has been updated", allNotifications.getLast().getMessage());
    }

    @Test
    void behaviour_deleteItem() {
        ItemCreateDTO dto = new ItemCreateDTO("orange", "7.00");
        ItemResponseDTO item = itemService.createItem(dto);
        Long id = item.getId();

        itemService.deleteItem(id);

        double currentCount = meterRegistry.counter("items_deleted").count();
        assertEquals(1.0, currentCount);
        List<Notification> allNotifications = notificationService.getAllNotifications();
        assertEquals(2, allNotifications.size());
        assertEquals("Item " + id + " has been deleted", allNotifications.getLast().getMessage());
    }
}