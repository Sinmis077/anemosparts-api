package dev.ioannis.anemosparts.daemons;

import dev.ioannis.anemosparts.domain.responses.TransactionKeyResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<TransactionKeyResponse> placeOrder(@RequestBody OrderRequest orderRequest, @RequestHeader @NotNull String paymentMethod) {
        if(orderService.isRequestPossible(orderRequest))
            throw new IllegalStateException("You can't order more than what we have!");

        switch (paymentMethod) {
            case "stripe":
                orderService.placeOrder(orderRequest);
                return ResponseEntity.ok().build();
                // Stripe payment service
            case "paypal":
                // Paypal payment service
                break;
            default:
                throw new IllegalArgumentException("Invalid payment method header");
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }
}
