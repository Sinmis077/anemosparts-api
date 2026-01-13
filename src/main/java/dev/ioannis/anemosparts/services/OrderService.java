package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.OrderDto;
import dev.ioannis.anemosparts.domain.requests.CheckoutRequest;
import dev.ioannis.anemosparts.domain.requests.ShipOrderRequest;
import dev.ioannis.anemosparts.domain.requests.UpdateOrderStatusRequest;
import dev.ioannis.anemosparts.entities.Order;
import dev.ioannis.anemosparts.entities.PartTransaction;

import java.util.List;

public interface OrderService {
    Order createOrder(CheckoutRequest request, List<PartTransaction> partTransactions, String paymentReference, Long amount);

    List<OrderDto> getAll();

    List<OrderDto> findOrdersByCustomerEmail(String email);

    OrderDto updateOrderStatus(UpdateOrderStatusRequest request);

    OrderDto addTrackingCode(ShipOrderRequest request);

    void cancelOrder(Long orderId);
}
