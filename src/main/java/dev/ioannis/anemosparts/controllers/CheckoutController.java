package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.requests.CheckoutRequest;
import dev.ioannis.anemosparts.domain.responses.CheckoutResponse;
import dev.ioannis.anemosparts.domain.responses.PaymentConfirmationResponse;
import dev.ioannis.anemosparts.services.InventoryService;
import dev.ioannis.anemosparts.services.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@AllArgsConstructor
public class CheckoutController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<CheckoutResponse> startCheckout(@RequestBody @Valid CheckoutRequest request) {
        return ResponseEntity.ok(paymentService.createCheckout(request));
    }

    @PostMapping("/webhook")
    public ResponseEntity<PaymentConfirmationResponse> proccessWebhook(
            @RequestBody @NotBlank String payload,
            @RequestHeader("Stripe-Signature") @NotBlank String signature
    ) {
        var response = paymentService.processWebhook(payload, signature);

        if (response == null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(response);
    }
}
