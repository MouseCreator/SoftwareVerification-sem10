package mouse.univ.repository;

import java.math.BigDecimal;
import java.util.Optional;

public interface DiscountRepository {
    Optional<BigDecimal> getDiscount(Long itemId);
}
