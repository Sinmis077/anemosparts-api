package dev.ioannis.anemosparts.daemons;

import dev.ioannis.anemosparts.domain.responses.CheckoutSessionResponse;
import dev.ioannis.anemosparts.domain.responses.PaymentConfirmationResponse;

public interface PaymentService {
    CheckoutSessionResponse createCheckoutSession(Object order);

    PaymentConfirmationResponse processPaymentWebhook(String payload, String signature);
}
