package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.repositories.PartRepo;
import dev.ioannis.anemosparts.services.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class InventoryServicePartImpl implements InventoryService {
    private final PartRepo partRepo;

    @Override
    @Transactional(readOnly = true)
    public Boolean canSell(Long partId, Integer quantity) {
        if(!partRepo.existsById(partId)) throw new EntityNotFoundException("Part does not exist");
        return partRepo.getQuantityById(partId) >= quantity;
    }

    @Override
    public void sell(Long partId, Integer quantity) {
        if(!partRepo.existsById(partId)) throw new EntityNotFoundException("Part does not exist");

        log.info("Attempting to sell part with id: {}, quantity={}", partId, quantity);
        Integer dbQuantity = partRepo.getQuantityById(partId);
        if (dbQuantity >= quantity) {
            partRepo.updateQuantityById(partId, dbQuantity - quantity);
            log.info("Successfully sold {} pieces of part with id: {}", quantity, partId);
        } else {
            throw new IllegalArgumentException("Can't reduce the quantity to be less than the available amount");
        }
    }
}
