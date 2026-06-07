package mouse.univ.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Item {
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
}
