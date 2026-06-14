package mouse.univ;


import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import mouse.univ.exception.InvalidDiscountException;
import mouse.univ.model.Item;
import mouse.univ.repository.DiscountRepository;
import mouse.univ.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PricingService implements PricingServiceContract {

    private ItemRepository itemRepository;
    private DiscountRepository discountRepository;

    public BigDecimal getPrice(Long itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        Item item = itemOptional.orElseThrow(() -> new EntityNotFoundException("Cannot find item with id " + itemId));
        Optional<BigDecimal> discount = discountRepository.getDiscount(itemId);
        if (discount.isEmpty()) {
            return item.getPrice();
        }
        if (discount.get().compareTo(BigDecimal.ONE) >= 0) {
            throw new InvalidDiscountException("Discount cannot be greater than 100%");
        }
        return item.getPrice().multiply(BigDecimal.ONE.subtract(discount.get())).setScale(2, RoundingMode.HALF_UP);
    }
}
