package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.AddressDto;
import dev.ioannis.anemosparts.domain.requests.CheckoutAccount;
import dev.ioannis.anemosparts.domain.requests.CheckoutRequest;
import dev.ioannis.anemosparts.domain.requests.ShipOrderRequest;
import dev.ioannis.anemosparts.domain.requests.UpdateOrderStatusRequest;
import dev.ioannis.anemosparts.entities.*;
import dev.ioannis.anemosparts.enums.OrderStatus;
import dev.ioannis.anemosparts.mappers.OrderMapper;
import dev.ioannis.anemosparts.repositories.OrderRepo;
import dev.ioannis.anemosparts.services.impl.OrderServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepo orderRepo;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private InventoryService inventoryService;
    @Mock
    private AccountService accountService;
    @Mock
    private AddressService addressService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private CheckoutRequest checkoutRequest;
    private Account account;
    private Address address;
    private Order order;
    private List<PartTransaction> transactions;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .email("test@example.com")
                .build();

        address = Address.builder()
                .forename("John")
                .surname("Doe")
                .street("Main St")
                .city("City")
                .postalCode("12345")
                .country("Country")
                .account(account)
                .build();

        order = Order.builder()
                .id(1L)
                .orderStatus(OrderStatus.PAID)
                .account(account)
                .shippingAddress(address)
                .paymentReference("pay_123")
                .total(BigDecimal.valueOf(10000))
                .build();

        CheckoutAccount checkoutAccount = new CheckoutAccount("test@example.com");
        AddressDto addressDto = new AddressDto();
        addressDto.setForename("John");
        addressDto.setSurname("Doe");
        addressDto.setStreet("Main St");
        addressDto.setCity("City");
        addressDto.setPostalCode("12345");
        addressDto.setCountry("Country");

        checkoutRequest = new CheckoutRequest();
        checkoutRequest.setAccount(checkoutAccount);
        checkoutRequest.setAddress(addressDto);

        transactions = List.of(
                PartTransaction.builder()
                        .id(1L)
                        .quantity(2L)
                        .build()
        );
    }

    @Test
    void createOrder_succeeds_whenValidRequest() {
        when(accountService.existsByEmail(anyString())).thenReturn(false);
        when(accountService.createGuestAccount(any())).thenReturn(account);
        when(addressService.exists(any())).thenReturn(false);
        when(addressService.createAddress(any())).thenReturn(address);
        when(orderRepo.save(any())).thenReturn(order);

        var result = orderService.createOrder(checkoutRequest, transactions, "pay_123", 10000L);

        assertNotNull(result);
        assertEquals(OrderStatus.PAID, result.getOrderStatus());
        verify(orderRepo).save(any());
        verify(inventoryService).transactionSale(any());
    }

    @Test
    void findOrdersByCustomerEmail_returnsEmptyList_whenNoOrders() {
        when(orderRepo.findAllByAccountEmail(anyString())).thenReturn(Optional.of(List.of()));

        var result = orderService.findOrdersByCustomerEmail("test@example.com");

        assertTrue(result.isEmpty());
        verify(orderRepo).findAllByAccountEmail("test@example.com");
    }

    @Test
    void updateOrderStatus_succeeds_whenValidStatusChange() {
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setOrderId(1L);
        request.setStatus(OrderStatus.SHIPPED);

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepo.save(any())).thenReturn(order);

        assertDoesNotThrow(() -> orderService.updateOrderStatus(request));
        verify(orderRepo).save(any());
    }

    @Test
    void updateOrderStatus_throwsException_whenOrderNotFound() {
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setOrderId(999L);
        request.setStatus(OrderStatus.SHIPPED);

        when(orderRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.updateOrderStatus(request));
        verify(orderRepo, never()).save(any());
    }

    @Test
    void updateOrderStatus_throwsException_whenMarkingPaidAfterUpdate() {
        order.setOrderStatus(OrderStatus.SHIPPED);
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setOrderId(1L);
        request.setStatus(OrderStatus.PAID);

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () -> orderService.updateOrderStatus(request));
        verify(orderRepo, never()).save(any());
    }

    @Test
    void addTrackingCode_succeeds_whenOrderNotDelivered() {
        ShipOrderRequest request = new ShipOrderRequest();
        request.setOrderId(1L);
        request.setTrackingCode("TRACK123");

        when(orderRepo.getReferenceById(1L)).thenReturn(order);
        when(orderRepo.save(any())).thenReturn(order);

        assertDoesNotThrow(() -> orderService.addTrackingCode(request));
        verify(orderRepo).save(any());
    }

    @Test
    void addTrackingCode_throwsException_whenOrderAlreadyDelivered() {
        order.setOrderStatus(OrderStatus.DELIVERED);
        ShipOrderRequest request = new ShipOrderRequest();
        request.setOrderId(1L);
        request.setTrackingCode("TRACK123");

        when(orderRepo.getReferenceById(1L)).thenReturn(order);

        assertThrows(IllegalStateException.class, () -> orderService.addTrackingCode(request));
        verify(orderRepo, never()).save(any());
    }

    @Test
    void cancelOrder_succeeds_whenOrderIsPaid() {
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepo.save(any())).thenReturn(order);

        assertDoesNotThrow(() -> orderService.cancelOrder(1L));
        verify(orderRepo).save(any());
    }

    @Test
    void cancelOrder_throwsException_whenOrderAlreadyShipped() {
        order.setOrderStatus(OrderStatus.SHIPPED);
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () -> orderService.cancelOrder(1L));
        verify(orderRepo, never()).save(any());
    }

    @Test
    void cancelOrder_throwsException_whenOrderNotFound() {
        when(orderRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.cancelOrder(999L));
        verify(orderRepo, never()).save(any());
    }
}
