package dev.ioannis.anemosparts.services;

public interface InventoryService {
    Boolean canSell(Long itemId, Integer quantity);

    void sell(Long itemId, Integer quantity);
}
