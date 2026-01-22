package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.CartDto;
import dev.ioannis.anemosparts.domain.PartTransactionDto;
import dev.ioannis.anemosparts.entities.Order;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.entities.PartTransaction;
import dev.ioannis.anemosparts.enums.TransactionStatus;
import dev.ioannis.anemosparts.repositories.PartRepo;
import dev.ioannis.anemosparts.repositories.TransactionRepo;
import dev.ioannis.anemosparts.services.impl.InventoryServicePartImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServicePartImplTest {

    @Mock
    private PartRepo partRepo;
    @Mock
    private TransactionRepo transactionRepo;

    @InjectMocks
    private InventoryServicePartImpl inventoryService;

    private Part part;
    private CartDto cart;
    private PartTransaction transaction;
    private Order order;

    @BeforeEach
    void setUp() {
        part = Part.builder()
                .id(1L)
                .name("Gearbox")
                .quantity(10L)
                .build();

        PartTransactionDto cartItem = new PartTransactionDto();
        cartItem.setPartId(1L);
        cartItem.setQuantity(2L);

        cart = new CartDto();
        cart.setParts(List.of(cartItem));

        transaction = PartTransaction.builder()
                .id(1L)
                .part(part)
                .quantity(2L)
                .status(TransactionStatus.HOLD)
                .build();

        order = Order.builder()
                .id(1L)
                .build();
    }

    @Test
    void hold_succeeds_whenStockAvailable() {
        when(partRepo.findQuantityById(1L)).thenReturn(10L);
        when(transactionRepo.sumOfQuantityOnHoldById(1L)).thenReturn(0);
        when(partRepo.existsById(1L)).thenReturn(true);
        when(partRepo.getReferenceById(1L)).thenReturn(part);
        when(transactionRepo.save(any())).thenReturn(transaction);

        var result = inventoryService.hold(cart);

        assertEquals(1, result.size());
        verify(transactionRepo).save(any());
    }

    @Test
    void hold_throwsException_whenInsufficientStock() {
        when(partRepo.existsById(1L)).thenReturn(true);
        when(partRepo.findQuantityById(1L)).thenReturn(1L);
        when(transactionRepo.sumOfQuantityOnHoldById(1L)).thenReturn(1);

        assertThrows(IllegalArgumentException.class, () -> inventoryService.hold(cart));
        verify(transactionRepo, never()).save(any());
    }

    @Test
    void releaseHold_succeeds_whenTransactionExists() {
        when(transactionRepo.existsById(1L)).thenReturn(true);
        when(transactionRepo.save(any())).thenReturn(transaction);

        assertDoesNotThrow(() -> inventoryService.releaseHold(List.of(transaction)));
        verify(transactionRepo).save(any());
    }

    @Test
    void releaseHold_doesNothing_whenTransactionDoesNotExist() {
        when(transactionRepo.existsById(1L)).thenReturn(false);

        assertDoesNotThrow(() -> inventoryService.releaseHold(List.of(transaction)));
        verify(transactionRepo, never()).save(any());
    }

    @Test
    void canSell_returnsTrue_whenStockAvailable() {
        when(partRepo.existsById(1L)).thenReturn(true);
        when(partRepo.findQuantityById(1L)).thenReturn(10L);
        when(transactionRepo.sumOfQuantityOnHoldById(1L)).thenReturn(0);

        var result = inventoryService.canSell(1L, 5L);

        assertTrue(result);
    }

    @Test
    void canSell_returnsFalse_whenInsufficientStock() {
        when(partRepo.existsById(1L)).thenReturn(true);
        when(partRepo.findQuantityById(1L)).thenReturn(10L);
        when(transactionRepo.sumOfQuantityOnHoldById(1L)).thenReturn(8);

        var result = inventoryService.canSell(1L, 5L);

        assertFalse(result);
    }

    @Test
    void canSell_throwsException_whenPartDoesNotExist() {
        when(partRepo.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> inventoryService.canSell(1L, 5L));
    }

    @Test
    void sell_succeeds_whenStockAvailable() {
        when(partRepo.existsById(1L)).thenReturn(true);
        when(partRepo.findQuantityById(1L)).thenReturn(10L);
        when(partRepo.getReferenceById(1L)).thenReturn(part);

        var result = inventoryService.sell(1L, 5L, order);

        assertNotNull(result);
        assertEquals(5L, result.getQuantity());
        verify(partRepo).updateQuantityById(1L, 5L);
    }

    @Test
    void sell_throwsException_whenInsufficientStock() {
        when(partRepo.existsById(1L)).thenReturn(true);
        when(partRepo.findQuantityById(1L)).thenReturn(3L);

        assertThrows(IllegalArgumentException.class, () -> inventoryService.sell(1L, 5L, order));
        verify(partRepo, never()).updateQuantityById(any(), any());
    }

    @Test
    void transactionSale_succeeds_whenTransactionExists() {
        when(transactionRepo.existsById(1L)).thenReturn(true);
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(transaction));
        when(partRepo.existsById(1L)).thenReturn(true);
        when(partRepo.findQuantityById(1L)).thenReturn(10L);
        when(transactionRepo.save(any())).thenReturn(transaction);

        var result = inventoryService.transactionSale(transaction);

        assertNotNull(result);
        verify(transactionRepo).save(any());
        verify(partRepo).updateQuantityById(1L, 8L);
    }

    @Test
    void transactionSale_createsNewTransaction_whenTransactionDoesNotExist() {
        transaction.setId(null);
        when(transactionRepo.existsById(any())).thenReturn(false);
        when(partRepo.existsById(1L)).thenReturn(true);
        when(partRepo.findQuantityById(1L)).thenReturn(10L);
        when(partRepo.getReferenceById(1L)).thenReturn(part);

        var result = inventoryService.transactionSale(transaction);

        assertNotNull(result);
        verify(partRepo).updateQuantityById(1L, 8L);
    }
}
