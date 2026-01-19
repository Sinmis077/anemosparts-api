package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.AddressDto;
import dev.ioannis.anemosparts.domain.OrderDto;
import dev.ioannis.anemosparts.domain.requests.CheckoutAccount;
import dev.ioannis.anemosparts.domain.requests.CheckoutRequest;
import dev.ioannis.anemosparts.domain.requests.ShipOrderRequest;
import dev.ioannis.anemosparts.domain.requests.UpdateOrderStatusRequest;
import dev.ioannis.anemosparts.entities.*;
import dev.ioannis.anemosparts.enums.OrderStatus;
import dev.ioannis.anemosparts.mappers.OrderMapper;
import dev.ioannis.anemosparts.repositories.OrderRepo;
import dev.ioannis.anemosparts.services.AccountService;
import dev.ioannis.anemosparts.services.AddressService;
import dev.ioannis.anemosparts.services.InventoryService;
import dev.ioannis.anemosparts.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderMapper mapper;
    private final OrderRepo orderRepo;
    private final InventoryService inventoryService;
    private final AccountService accountService;
    private final AddressService addressService;

    @Override
    public List<OrderDto> getAll() {
        return mapper.toDtos(orderRepo.findAll());
    }

    @Override
    public Order createOrder(CheckoutRequest request, List<PartTransaction> partTransactions, String paymentReference, Long amount) {
        var order = new Order();

        var userAccount = buildAccount(request.getAccount());

        order.setShippingAddress(buildAddress(request.getAddress(), userAccount));

        order.setOrderStatus(OrderStatus.PAID);

        order.setAccount(userAccount);

        order.setPaymentReference(paymentReference);

        order.setTotal(BigDecimal.valueOf(amount));

        var savedOrder = orderRepo.save(order);

        updateTransactions(partTransactions, savedOrder);

        log.debug("Order created: {}", savedOrder.getId());

        return savedOrder;
    }

    private Address buildAddress(AddressDto addressRequest, Account account) {
        var address = Address.builder()
                        .forename(addressRequest.getForename())
                        .surname(addressRequest.getSurname())
                        .houseNumber(addressRequest.getHouseNumber())
                        .street(addressRequest.getStreet())
                        .city(addressRequest.getCity())
                        .postalCode(addressRequest.getPostalCode())
                        .state(addressRequest.getState())
                        .country(addressRequest.getCountry())
                        .extras(addressRequest.getExtras())
                        .account(account)
                        .build();

        return addressService.exists(address)
                ? addressService.findByExample(address)
                : addressService.createAddress(address);
    }

    private void updateTransactions(List<PartTransaction> transactions, Order order) {
        for (PartTransaction t : transactions) {
            t.setOrder(order);
            inventoryService.transactionSale(t);
        }
    }

    private Account buildAccount(CheckoutAccount account) {
        return accountService.existsByEmail(account.getEmail())
                ? accountService.findByEmail(account.getEmail()).orElseThrow(() -> new EntityNotFoundException("Couldn't find an account associated with this email"))
                : accountService.createGuestAccount(Account.builder()
                .email(account.getEmail())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findOrdersByCustomerEmail(String email) {
        return mapper.toDtos(orderRepo.findAllByAccountEmail(email).orElse(List.of()));
    }

    @Override
    public OrderDto updateOrderStatus(UpdateOrderStatusRequest request) {
        var order = orderRepo.findById(request.getOrderId()).orElseThrow(() -> new EntityNotFoundException("Could not find order"));
        if(order.getOrderStatus() != OrderStatus.PAID && request.getStatus() == OrderStatus.PAID) {
            throw new IllegalStateException("Order cannot be marked as paid if it's been updated");
        }
        if(order.getOrderStatus() == request.getStatus()) {
            throw new IllegalArgumentException("New status can't be the same as the old status");
        }

        order.setOrderStatus(request.getStatus());
        return mapper.toDto(orderRepo.save(order));
    }

    @Override
    public OrderDto addTrackingCode(ShipOrderRequest request) {
        var order = orderRepo.getReferenceById(request.getOrderId());
        if(order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Order has already been delivered");
        }

        order.setOrderStatus(OrderStatus.SHIPPED);
        order.setTrackingCode(request.getTrackingCode());
        return mapper.toDto(orderRepo.save(order));
    }

    @Override
    public void cancelOrder(Long orderId) {
        var order = orderRepo.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Could not find order"));

        if(order.getOrderStatus() == OrderStatus.DELIVERED ||  order.getOrderStatus() == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Order is already being or has been delivered");
        }

        order.setOrderStatus(OrderStatus.CANCELED);

        orderRepo.save(order);
    }
}
