package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.OrderDto;
import dev.ioannis.anemosparts.domain.requests.ShipOrderRequest;
import dev.ioannis.anemosparts.domain.requests.UpdateOrderStatusRequest;
import dev.ioannis.anemosparts.domain.responses.FindOrdersResponse;
import dev.ioannis.anemosparts.services.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public FindOrdersResponse getAll() {
        return new FindOrdersResponse(orderService.getAll());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public OrderDto updateOrderStatus(@RequestBody @Valid UpdateOrderStatusRequest request) {
        return orderService.updateOrderStatus(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/ship")
    public OrderDto updateTrackingCode(@RequestBody @Valid ShipOrderRequest request) {
        return orderService.addTrackingCode(request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrderById(@RequestParam @NotNull Long orderId) {
        orderService.cancelOrder(orderId);
    }

}
