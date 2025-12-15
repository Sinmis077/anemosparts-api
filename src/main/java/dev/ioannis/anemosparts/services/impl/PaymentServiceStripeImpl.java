package dev.ioannis.anemosparts.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import dev.ioannis.anemosparts.domain.CartDto;
import dev.ioannis.anemosparts.domain.requests.CheckoutRequest;
import dev.ioannis.anemosparts.domain.responses.CheckoutResponse;
import dev.ioannis.anemosparts.domain.responses.PaymentConfirmationResponse;
import dev.ioannis.anemosparts.entities.PartTransaction;
import dev.ioannis.anemosparts.enums.PaymentProvider;
import dev.ioannis.anemosparts.repositories.PartRepo;
import dev.ioannis.anemosparts.services.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceStripeImpl implements PaymentService {
    private final PartRepo partRepo;

    private String webhookSecret;

    private Long shippingRate;
    private String successUrl;
    private String cancelUrl;

    @Override
    public CheckoutResponse createCheckout(CheckoutRequest checkoutRequest) {
        List<PartTransaction> cart = buildPartTransactions(checkoutRequest.getCart());

        List<LineItem> lineItems = buildLineItems(cart);
        lineItems.add(buildShippingRateItemLine());

        var metaData = buildMetadata(cart);

        try {
            var sessionParams = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCustomerEmail(checkoutRequest.getEmail())
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addAllLineItem(lineItems)
                    .putMetadata("cart_data", metaData)
                    .setPaymentIntentData(
                            SessionCreateParams.PaymentIntentData.builder()
                                    .putMetadata("payment_intent_data", metaData)
                                    .build()
                    )
                    .build();

            Session session = Session.create(sessionParams);

            return new CheckoutResponse(session.getUrl(), session.getId());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    private List<PartTransaction> buildPartTransactions(CartDto cart) {
        return cart.getParts().stream().map((part) -> PartTransaction.builder()
                        .part(partRepo
                                .findById(part.getPartId())
                                .orElseThrow(() -> new EntityNotFoundException("Could not find part with id: " + part.getPartId()))
                        )
                        .quantity(part.getQuantity())
                        .build()).toList();
    }

    private List<LineItem> buildLineItems(List<PartTransaction> cart) {
        return cart.stream().map(partTransaction -> LineItem.builder()
                .setQuantity(partTransaction.getQuantity())
                .setPriceData(
                        LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmount(partTransaction.getPart().getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                                .setProductData(
                                        LineItem.PriceData.ProductData.builder()
                                                .setName(partTransaction.getPart().getName())
                                                .setDescription(partTransaction.getPart().getDescription())
                                                .build()
                                )
                                .build()
                )
                .build()).collect(Collectors.toList());
    }

    private LineItem buildShippingRateItemLine() {
        return LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmount(shippingRate * 100)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Shipping")
                                                .setDescription("Flat rate shipping")
                                                .build()
                                )
                                .build()
                )
                .build();
    }

    private String buildMetadata(List<PartTransaction> part) {
        try {
            return new ObjectMapper().writeValueAsString(part);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize part transactions: " + e.getMessage());
        }
    }

    @Override
    public PaymentConfirmationResponse processWebhook(String payload, String signature) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, signature, webhookSecret);
        } catch (SignatureVerificationException e) {
            log.error("Invalid Stripe webhook signature");
            throw new IllegalArgumentException("Invalid webhook signature");
        }

        Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalArgumentException("Failed to deserialize Stripe session"));

        // Only process paid sessions
        if ("paid".equals(session.getPaymentStatus())) {
            String checkoutData = session.getMetadata().get("checkout_data");
            if (checkoutData == null) {
                log.error("Missing checkout_data in session metadata: {}", session.getId());
                throw new IllegalStateException("Missing checkout data in session");
            }

            return new PaymentConfirmationResponse(
                    session.getId(),
                    session.getCustomerEmail(),
                    checkoutData
            );
        }

        return null;
    }

    @Override
    public PaymentProvider getProvider() {
        return PaymentProvider.STRIPE;
    }
}
