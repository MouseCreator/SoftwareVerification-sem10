package mouse.univ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateDTO {
    private String name;
    private BigDecimal price;

    public ItemCreateDTO(String name, String price) {
        this.name = name;
        this.price = new BigDecimal(price);
    }
}
