package mouse.univ.controller;

import lombok.AllArgsConstructor;
import mouse.univ.dto.ItemCreateDTO;
import mouse.univ.dto.ItemResponseDTO;
import mouse.univ.dto.ItemUpdateDTO;
import mouse.univ.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemService itemService;

    @PostMapping
    public ItemResponseDTO createItem(ItemCreateDTO createDTO) {
        return itemService.createItem(createDTO);
    }

    @GetMapping
    public List<ItemResponseDTO> getAll() {
        return itemService.getAllItems();
    }

    @GetMapping(value = "/{id}")
    public ItemResponseDTO findById(@PathVariable Long id) {
        return itemService.getItem(id);
    }

    @PutMapping
    public ItemResponseDTO updateItem(ItemUpdateDTO updateDTO) {
        return itemService.updateItem(updateDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}
