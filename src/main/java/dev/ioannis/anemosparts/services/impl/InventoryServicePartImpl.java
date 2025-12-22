package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.CartDto;
import dev.ioannis.anemosparts.entities.Order;
import dev.ioannis.anemosparts.entities.PartTransaction;
import dev.ioannis.anemosparts.enums.TransactionStatus;
import dev.ioannis.anemosparts.repositories.PartRepo;
import dev.ioannis.anemosparts.repositories.TransactionRepo;
import dev.ioannis.anemosparts.services.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class InventoryServicePartImpl implements InventoryService {
    private final PartRepo partRepo;
    private final TransactionRepo transactionRepo;

    @Override
    public List<PartTransaction> hold(CartDto cart) throws EntityNotFoundException {
        return cart.getParts().stream().map(transaction -> {
            if (!canSell(transaction.getPartId(), transaction.getQuantity())) {
                throw new IllegalArgumentException("Cannot sell part with id " + transaction.getPartId() + " with quantity " + transaction.getQuantity());
            }

            var transactionEntity = PartTransaction.builder()
                    .part(partRepo.getReferenceById(transaction.getPartId()))
                    .quantity(transaction.getQuantity())
                    .status(TransactionStatus.HOLD)
                    .build();

            return transactionRepo.save(transactionEntity);
        }).toList();
    }

    @Override
    public void releaseHold(List<PartTransaction> transactions) {
        for (PartTransaction transaction : transactions) {
            if (transactionRepo.existsById(transaction.getId()) && transaction.getStatus() == TransactionStatus.HOLD) {
                transaction.setStatus(TransactionStatus.CANCELLED);

                transactionRepo.save(transaction);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean canSell(Long partId, Long quantity) throws EntityNotFoundException {
        if(!partRepo.existsById(partId)) throw new EntityNotFoundException("Part does not exist");
        return partRepo.findQuantityById(partId)
                - transactionRepo.sumOfQuantityOnHoldById(partId)
                >= quantity;
    }

    @Override
    public PartTransaction sell(Long partId, Long quantity, Order order) throws EntityNotFoundException, IllegalArgumentException {
        updatePartQuantity(partId, quantity);
        log.debug("Successfully sold {} pieces of part with id: {}", quantity, partId);
        return PartTransaction.builder()
                .part(partRepo.getReferenceById(partId))
                .quantity(quantity)
                .order(order)
                .build();
    }

    @Override
    public PartTransaction transactionSale(PartTransaction transaction) {
        if (transactionRepo.existsById(transaction.getId())) {
            var transactionEntity = transactionRepo.findById(transaction.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Transaction does not exist"));
            transactionEntity.setStatus(TransactionStatus.COMPLETED);
            transactionEntity.setOrder(transaction.getOrder());

            updatePartQuantity(transactionEntity.getPart().getId(), transactionEntity.getQuantity());

            return transactionRepo.save(transactionEntity);
        } else {
            return sell(transaction.getPart().getId(), transaction.getQuantity(), transaction.getOrder());
        }
    }

    private void updatePartQuantity(Long partId, Long quantity) {
        if(!partRepo.existsById(partId)) throw new EntityNotFoundException("Part does not exist");

        log.debug("Attempting to sell part with id: {}, quantity={}", partId, quantity);

        var dbQuantity = partRepo.findQuantityById(partId);

        if (dbQuantity >= quantity) {
            partRepo.updateQuantityById(partId, (dbQuantity - quantity));
        } else throw new IllegalArgumentException("Cannot reduce the quantity to be less than the available amount");
    }
}
