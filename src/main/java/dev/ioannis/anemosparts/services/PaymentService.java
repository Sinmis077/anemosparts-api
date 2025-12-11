package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.requests.CheckoutRequest;
import dev.ioannis.anemosparts.domain.responses.CheckoutResponse;
import dev.ioannis.anemosparts.domain.responses.PaymentConfirmationResponse;
import dev.ioannis.anemosparts.enums.PaymentProvider;

public interface PaymentService {
    CheckoutResponse createCheckout(CheckoutRequest checkoutRequest);

    PaymentConfirmationResponse processWebhook(String payload, String signature);

    PaymentProvider getProvider();
}
