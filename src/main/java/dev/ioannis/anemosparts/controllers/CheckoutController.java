package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.requests.CheckoutRequest;
import dev.ioannis.anemosparts.domain.responses.CheckoutResponse;
import dev.ioannis.anemosparts.services.InventoryService;
import dev.ioannis.anemosparts.services.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final InventoryService inventoryService;
    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<CheckoutResponse> startCheckout(@RequestBody @Valid CheckoutRequest request) {
        if(inventoryService.canSell(request.getCart())) {
            throw new RuntimeException("Unimplemented");
        } else throw new IllegalArgumentException("Can not proceed with checkout, not enough items in stock");
    }
}
