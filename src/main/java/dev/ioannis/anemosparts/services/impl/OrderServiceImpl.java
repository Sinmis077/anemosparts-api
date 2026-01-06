package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.AddressDto;
import dev.ioannis.anemosparts.domain.OrderDto;
import dev.ioannis.anemosparts.domain.requests.CheckoutAccount;
import dev.ioannis.anemosparts.domain.requests.CheckoutRequest;
import dev.ioannis.anemosparts.domain.requests.ShipOrderRequest;
import dev.ioannis.anemosparts.domain.requests.UpdateOrderStatusRequest;
import dev.ioannis.anemosparts.entities.Account;
import dev.ioannis.anemosparts.entities.Address;
import dev.ioannis.anemosparts.entities.Order;
import dev.ioannis.anemosparts.entities.PartTransaction;
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
    public Order createOrder(CheckoutRequest request, List<PartTransaction> partTransactions, String paymentReference) {
        var order = new Order();

        var userAccount = buildAccount(request.getAccount());

        order.setShippingAddress(buildAddress(request.getAddress(), userAccount));

        order.setOrderStatus(OrderStatus.PAID);

        order.setAccount(userAccount);

        order.setPaymentReference(paymentReference);

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
        order.setOrderStatus(request.getStatus());
        return mapper.toDto(orderRepo.save(order));
    }

    @Override
    public OrderDto addTrackingCode(ShipOrderRequest request) {
        var order = orderRepo.getReferenceById(request.getOrderId());
        order.setOrderStatus(OrderStatus.SHIPPED);
        order.setTrackingCode(request.getTrackingCode());
        return mapper.toDto(orderRepo.save(order));
    }
}
