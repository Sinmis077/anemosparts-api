package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.CartDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryService {
    @Transactional(readOnly = true)
    Boolean canSell(CartDto cart);

    Boolean canSell(Long itemId, Integer quantity) throws EntityNotFoundException;

    void sell(Long itemId, Integer quantity) throws EntityNotFoundException, IllegalArgumentException;
}
