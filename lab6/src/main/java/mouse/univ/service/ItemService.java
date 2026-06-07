package mouse.univ.service;

import mouse.univ.dto.ItemCreateDTO;
import mouse.univ.dto.ItemResponseDTO;
import mouse.univ.dto.ItemUpdateDTO;

import java.util.List;

public interface ItemService {
    ItemResponseDTO createItem(ItemCreateDTO dto);
    ItemResponseDTO getItem(Long id);
    List<ItemResponseDTO> getAllItems();
    ItemResponseDTO updateItem(ItemUpdateDTO dto);
    void deleteItem(Long id);
}
