package dev.ioannis.anemosparts.domain.responses;

public record PaymentConfirmationResponse(String paymentReference, String email, String metadata) {
}
