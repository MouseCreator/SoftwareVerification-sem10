package mouse.univ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateDTO {
    private Long id;
    private String name;
    private BigDecimal price;

    public ItemUpdateDTO(Long id, String name, String price) {
        this.id = id;
        this.name = name;
        this.price = new BigDecimal(price);
    }
}
