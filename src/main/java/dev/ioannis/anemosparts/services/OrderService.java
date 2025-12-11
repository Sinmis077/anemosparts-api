package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.OrderDto;
import dev.ioannis.anemosparts.domain.requests.CheckoutRequest;
import dev.ioannis.anemosparts.domain.requests.ShipOrderRequest;
import dev.ioannis.anemosparts.domain.requests.UpdateOrderStatusRequest;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(CheckoutRequest checkoutRequest);

    List<OrderDto> getOrdersByCustomerEmail(String email);

    OrderDto updateOrderStatus(UpdateOrderStatusRequest request);

    OrderDto addTrackingCode(ShipOrderRequest request);

}
