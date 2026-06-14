package mouse.univ;

import java.math.BigDecimal;

public interface PricingServiceContract {
    BigDecimal getPrice(Long itemId);
}


