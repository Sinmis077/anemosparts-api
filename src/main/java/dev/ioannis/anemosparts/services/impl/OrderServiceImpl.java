package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.OrderDto;
import dev.ioannis.anemosparts.domain.requests.CheckoutRequest;
import dev.ioannis.anemosparts.domain.requests.ShipOrderRequest;
import dev.ioannis.anemosparts.domain.requests.UpdateOrderStatusRequest;
import dev.ioannis.anemosparts.enums.OrderStatus;
import dev.ioannis.anemosparts.mappers.OrderMapper;
import dev.ioannis.anemosparts.repositories.OrderRepo;
import dev.ioannis.anemosparts.services.OrderService;
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

    @Override
    public OrderDto createOrder(CheckoutRequest checkoutRequest) {
        return null;
    }

    @Override
    public List<OrderDto> getOrdersByCustomerEmail(String email) {
        return mapper.toDtos(orderRepo.getOrdersByAccountEmail(email));
    }

    @Override
    public OrderDto updateOrderStatus(UpdateOrderStatusRequest request) {
        var order = orderRepo.getReferenceById(request.getOrderId());
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
