package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.CartDto;
import dev.ioannis.anemosparts.domain.PartTransactionDto;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.repositories.PartRepo;
import dev.ioannis.anemosparts.services.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class InventoryServicePartImpl implements InventoryService {
    private final PartRepo partRepo;

    @Transactional(readOnly = true)
    @Override
    public Boolean canSell(CartDto cart) throws EntityNotFoundException {
        for(PartTransactionDto part:cart.getParts()){
            if(!canSell(part.getPartId(), part.getQuantity())) return false;
        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean canSell(Long partId, Long quantity) throws EntityNotFoundException {
        if(!partRepo.existsById(partId)) throw new EntityNotFoundException("Part does not exist");
        return partRepo.getQuantityById(partId) >= quantity;
    }

    @Override
    public void sell(Long partId, Long quantity) throws EntityNotFoundException, IllegalArgumentException {
        if(!partRepo.existsById(partId)) throw new EntityNotFoundException("Part does not exist");

        log.info("Attempting to sell part with id: {}, quantity={}", partId, quantity);
        Long dbQuantity = partRepo.getQuantityById(partId);
        if (dbQuantity >= quantity) {
            partRepo.updateQuantityById(partId, dbQuantity - quantity);
            log.info("Successfully sold {} pieces of part with id: {}", quantity, partId);
        } else {
            throw new IllegalArgumentException("Can't reduce the quantity to be less than the available amount");
        }
    }
}
