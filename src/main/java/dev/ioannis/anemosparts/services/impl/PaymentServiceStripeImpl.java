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
import dev.ioannis.anemosparts.domain.requests.CheckoutRequest;
import dev.ioannis.anemosparts.domain.responses.CheckoutResponse;
import dev.ioannis.anemosparts.domain.responses.PaymentConfirmationResponse;
import dev.ioannis.anemosparts.entities.PartTransaction;
import dev.ioannis.anemosparts.enums.PaymentProvider;
import dev.ioannis.anemosparts.services.InventoryService;
import dev.ioannis.anemosparts.services.OrderService;
import dev.ioannis.anemosparts.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceStripeImpl implements PaymentService {
    private final OrderService orderService;
    private final InventoryService inventoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${app.payment.stripe.webhook.secret}")
    private String webhookSecret;

    @Value("${app.payment.shipping.rate:12}")
    private Long shippingRate;
    @Value("${app.stripe.success.url}")
    private String successUrl;
    @Value("${app.stripe.cancel.url}")
    private String cancelUrl;

    @Override
    public CheckoutResponse createCheckout(CheckoutRequest checkoutRequest) {
        List<PartTransaction> cart = inventoryService.hold(checkoutRequest.getCart());

        List<LineItem> lineItems = buildLineItems(cart);
        lineItems.add(buildShippingRateItemLine());

        var metaData = buildMetadata(cart);

        try {
            var sessionParams = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCustomerEmail(checkoutRequest.getAccount().getEmail())
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addAllLineItem(lineItems)
                    .putMetadata("checkout_data", buildMetadata(checkoutRequest))
                    .putMetadata("part_transaction_data", metaData)
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

    private String buildMetadata(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize object: " + e.getMessage());
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

        log.debug(session.getPaymentStatus());
        // Only process paid sessions
        if ("paid".equals(session.getPaymentStatus())) {
            String checkoutData = session.getMetadata().get("checkout_data");
            if (checkoutData == null) {
                log.error("Missing checkout_data in session metadata: {}", session.getId());
                throw new IllegalStateException("Missing checkout data in session");
            }

            try {
                var checkoutRequest = objectMapper.readValue(checkoutData, CheckoutRequest.class);
                var transactions = deserializeTransactions(session);

                log.debug(transactions.toString(), checkoutRequest.toString(), session);

                orderService.createOrder(checkoutRequest, transactions, session.getId());
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Could not deserialize checkout request: " + e.getMessage());
            }

            return new PaymentConfirmationResponse(
                    session.getId(),
                    session.getCustomerEmail(),
                    checkoutData
            );
        }

        if("cancelled".equals(session.getPaymentStatus())) {
            var transactions =  deserializeTransactions(session);

            inventoryService.releaseHold(transactions);
        }

        return null;
    }

    private List<PartTransaction> deserializeTransactions(Session session) {
        String partTransactionData = session.getMetadata().get("part_transaction_data");

        if (partTransactionData == null) {
            log.error("Missing part_transaction_data in session metadata: {}", session.getId());
            throw new IllegalStateException("Missing part_transaction_data in session");
        }

        try {
            var partTransactions = objectMapper.readValue(partTransactionData, PartTransaction[].class);

            return Arrays.stream(partTransactions).toList();
        } catch (JsonProcessingException e)
        {
            throw new RuntimeException("Could not deserialize part_transactions: " + e.getMessage());
        }
    }

    @Override
    public PaymentProvider getProvider() {
        return PaymentProvider.STRIPE;
    }
}
