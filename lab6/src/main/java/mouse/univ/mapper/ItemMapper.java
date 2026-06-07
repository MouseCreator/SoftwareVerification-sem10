package mouse.univ.mapper;

import mouse.univ.dto.ItemCreateDTO;
import mouse.univ.dto.ItemResponseDTO;
import mouse.univ.dto.ItemUpdateDTO;
import mouse.univ.model.Item;
import org.springframework.stereotype.Service;

@Service
public class ItemMapper {
    public Item createItem(ItemCreateDTO dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        return item;
    }

    public Item updateItem(ItemUpdateDTO dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        return item;
    }

    public ItemResponseDTO response(Item item) {
        ItemResponseDTO responseDTO = new ItemResponseDTO();
        responseDTO.setId(item.getId());
        responseDTO.setName(item.getName());
        responseDTO.setPrice(item.getPrice());
        return responseDTO;
    }
}
