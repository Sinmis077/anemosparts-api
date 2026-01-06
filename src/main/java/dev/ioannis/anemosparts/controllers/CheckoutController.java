package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.requests.CheckoutRequest;
import dev.ioannis.anemosparts.domain.responses.CheckoutResponse;
import dev.ioannis.anemosparts.domain.responses.PaymentConfirmationResponse;
import dev.ioannis.anemosparts.services.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@AllArgsConstructor
public class CheckoutController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CheckoutResponse startCheckout(@RequestBody @Valid CheckoutRequest request) {
        return paymentService.createCheckout(request);
    }

    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.OK)
    public PaymentConfirmationResponse proccessWebhook(
            @RequestBody @NotBlank String payload,
            @RequestHeader("Stripe-Signature") @NotBlank String signature
    ) {
        return paymentService.processWebhook(payload, signature);
    }
}
