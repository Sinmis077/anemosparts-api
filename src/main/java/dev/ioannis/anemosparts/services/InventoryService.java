package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.CartDto;
import dev.ioannis.anemosparts.entities.Order;
import dev.ioannis.anemosparts.entities.PartTransaction;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface InventoryService {
    List<PartTransaction> hold(CartDto cart);

    void releaseHold(List<PartTransaction> transactions);

    Boolean canSell(Long itemId, Long quantity) throws EntityNotFoundException;

    PartTransaction sell(Long partId, Long quantity, Order order) throws EntityNotFoundException, IllegalArgumentException;

    PartTransaction transactionSale(PartTransaction transaction);
}
