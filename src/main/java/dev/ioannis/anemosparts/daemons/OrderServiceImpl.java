package dev.ioannis.anemosparts.daemons;

import dev.ioannis.anemosparts.services.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final InventoryService inventoryService;

    @Override
    @Transactional(readOnly = true)
    public boolean isRequestPossible(OrderRequest request) {
        try {
            Set<CartItem> cart = request.getItems().stream().map(cartItem -> {
                try {
                    if(inventoryService.canSell(cartItem.getPartId(), cartItem.getQuantity())) {
                        return cartItem;
                    }
                } catch (EntityNotFoundException e) {
                    log.warn("Item with id: {} doesn't have enough stock", cartItem.getPartId());
                    throw new EntityNotFoundException("Not enough stock to fulfil your order");
                }
                return cartItem;
            }).collect(Collectors.toSet());

            return cart.size() == request.getItems().size();
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    @Override
    public void placeOrder(OrderRequest request) {
        request.getItems().forEach(cartItem -> {
            inventoryService.sell(cartItem.getPartId(), cartItem.getQuantity());
        });
    }
}
