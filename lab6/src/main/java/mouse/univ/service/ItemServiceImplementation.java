package mouse.univ.service;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mouse.univ.dto.ItemCreateDTO;
import mouse.univ.dto.ItemResponseDTO;
import mouse.univ.dto.ItemUpdateDTO;
import mouse.univ.mapper.ItemMapper;
import mouse.univ.model.Item;
import mouse.univ.model.Notification;
import mouse.univ.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImplementation implements ItemService {

    private final ItemRepository itemRepository;
    private final MeterRegistry meterRegistry;
    private final NotificationService notificationService;
    private final ItemMapper mapper;

    @Override
    public ItemResponseDTO createItem(ItemCreateDTO dto) {
        Item item = mapper.createItem(dto);
        Item saved = itemRepository.save(item);

        meterRegistry.counter("items_created").increment();
        notificationService.notify(new Notification("Item " + saved.getId() + " has been created"));

        return mapper.response(saved);
    }

    @Override
    public ItemResponseDTO getItem(Long id) {
        return itemRepository.findById(id).map(mapper::response).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<ItemResponseDTO> getAllItems() {
        return itemRepository.findAll().stream().map(mapper::response).toList();
    }

    @Override
    public ItemResponseDTO updateItem(ItemUpdateDTO dto) {
        Item item = mapper.updateItem(dto);
        Item saved = itemRepository.save(item);

        meterRegistry.counter("items_updated").increment();
        notificationService.notify(new Notification("Item " + saved.getId() + " has been updated"));

        return mapper.response(saved);
    }

    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
        meterRegistry.counter("items_deleted").increment();
        notificationService.notify(new Notification("Item " + id + " has been deleted"));
    }
}
