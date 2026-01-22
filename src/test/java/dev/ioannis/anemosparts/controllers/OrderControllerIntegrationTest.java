package dev.ioannis.anemosparts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ioannis.anemosparts.domain.requests.ShipOrderRequest;
import dev.ioannis.anemosparts.domain.requests.UpdateOrderStatusRequest;
import dev.ioannis.anemosparts.entities.*;
import dev.ioannis.anemosparts.enums.OrderStatus;
import dev.ioannis.anemosparts.enums.TransactionStatus;
import dev.ioannis.anemosparts.helpers.AuthTokenHelper;
import dev.ioannis.anemosparts.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Order management
 * Covers the complete order lifecycle after checkout:
 * 1. Viewing orders
 * 2. Updating order status
 * 3. Adding tracking codes
 * 4. Cancelling orders
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private PartRepo partRepo;

    @Autowired
    private ModelRepo modelRepo;

    @Autowired
    private BrandRepo brandRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    private Account testAccount;
    private Address testAddress;
    private Part testPart;
    private Order testOrder;
    @Autowired
    private AuthTokenHelper authTokenHelper;

    @BeforeEach
    void setUp() {
        // Clean database
        transactionRepo.deleteAll();
        orderRepo.deleteAll();
        addressRepo.deleteAll();
        accountRepo.deleteAll();
        partRepo.deleteAll();
        modelRepo.deleteAll();
        brandRepo.deleteAll();

        // Create test account
        testAccount = accountRepo.save(Account.builder()
                .email("customer@example.com")
                .password("hashedPassword")
                .forename("Test")
                .surname("Customer")
                .build());

        // Create test address
        testAddress = addressRepo.save(Address.builder()
                .forename("Test")
                .surname("Customer")
                .houseNumber("123")
                .street("Test Street")
                .city("Nicosia")
                .postalCode("12345")
                .state("Nicosia")
                .country("Cyprus")
                .account(testAccount)
                .build());

        // Create test part
        Brand brand = brandRepo.save(Brand.builder()
                .name("Honda")
                .build());

        Model model = modelRepo.save(Model.builder()
                .name("CBR600RR")
                .productionYear(2021)
                .brand(brand)
                .build());

        testPart = partRepo.save(Part.builder()
                .name("Exhaust System")
                .description("Performance exhaust")
                .partNumber("EXH-001")
                .price(BigDecimal.valueOf(450))
                .quantity(10L)
                .models(List.of(model))
                .build());

        // Create test order
        testOrder = orderRepo.save(Order.builder()
                .account(testAccount)
                .shippingAddress(testAddress)
                .orderStatus(OrderStatus.PAID)
                .paymentReference("test_payment_ref")
                .total(BigDecimal.valueOf(462.00)) // 450 + 12 shipping
                .build());

        // Create transaction for the order
        transactionRepo.save(PartTransaction.builder()
                .part(testPart)
                .order(testOrder)
                .quantity(1L)
                .status(TransactionStatus.COMPLETED)
                .build());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllOrders() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders[0].id").value(testOrder.getId()))
                .andExpect(jsonPath("$.orders[0].status").value("PAID"))
                .andExpect(jsonPath("$.orders[0].total").value(462.00));

        // Verify database state
        List<Order> orders = orderRepo.findAll();
        assertThat(orders).hasSize(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateOrderStatusToProcessing() throws Exception {
        // Given
        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.builder()
                .orderId(testOrder.getId())
                .status(OrderStatus.PROCESSING)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        // When
        mockMvc.perform(put("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSING"));

        // Then - Verify database updated
        Order updatedOrder = orderRepo.findById(testOrder.getId()).orElseThrow();
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.PROCESSING);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddTrackingCodeAndUpdateStatusToShipped() throws Exception {
        // Given
        ShipOrderRequest request = ShipOrderRequest.builder()
                .orderId(testOrder.getId())
                .trackingCode("TRACK123456789")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        // When
        mockMvc.perform(put("/api/orders/ship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPED"))
                .andExpect(jsonPath("$.trackingCode").value("TRACK123456789"));

        // Then - Verify database updated
        Order shippedOrder = orderRepo.findById(testOrder.getId()).orElseThrow();
        assertThat(shippedOrder.getOrderStatus()).isEqualTo(OrderStatus.SHIPPED);
        assertThat(shippedOrder.getTrackingCode()).isEqualTo("TRACK123456789");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotAllowStatusChangeFromShippedToPaid() throws Exception {
        // Given - order is already shipped
        testOrder.setOrderStatus(OrderStatus.SHIPPED);
        orderRepo.save(testOrder);

        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.builder()
                .orderId(testOrder.getId())
                .status(OrderStatus.PAID)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(put("/api/orders")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()); // Should fail validation

        // Verify order status unchanged
        Order unchangedOrder = orderRepo.findById(testOrder.getId()).orElseThrow();
        assertThat(unchangedOrder.getOrderStatus()).isEqualTo(OrderStatus.SHIPPED);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCancelPaidOrder() throws Exception {
        // When
        mockMvc.perform(delete("/api/orders")
                        .param("orderId", testOrder.getId().toString()))
                .andExpect(status().isNoContent());

        // Then - Verify order cancelled in database
        Order cancelledOrder = orderRepo.findById(testOrder.getId()).orElseThrow();
        assertThat(cancelledOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotCancelShippedOrder() throws Exception {
        // Given - order is shipped
        testOrder.setOrderStatus(OrderStatus.SHIPPED);
        orderRepo.save(testOrder);

        // When & Then
        mockMvc.perform(delete("/api/orders")
                        .param("orderId", testOrder.getId().toString()))
                .andExpect(status().isBadRequest()); // Should throw IllegalStateException

        // Verify order status unchanged
        Order unchangedOrder = orderRepo.findById(testOrder.getId()).orElseThrow();
        assertThat(unchangedOrder.getOrderStatus()).isEqualTo(OrderStatus.SHIPPED);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotCancelDeliveredOrder() throws Exception {
        // Given - order is delivered
        testOrder.setOrderStatus(OrderStatus.DELIVERED);
        orderRepo.save(testOrder);

        // When & Then
        mockMvc.perform(delete("/api/orders")
                        .param("orderId", testOrder.getId().toString()))
                .andExpect(status().isBadRequest());

        // Verify order status unchanged
        Order unchangedOrder = orderRepo.findById(testOrder.getId()).orElseThrow();
        assertThat(unchangedOrder.getOrderStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotAddTrackingCodeToDeliveredOrder() throws Exception {
        // Given - order is already delivered
        testOrder.setOrderStatus(OrderStatus.DELIVERED);
        orderRepo.save(testOrder);

        ShipOrderRequest request = ShipOrderRequest.builder()
                .orderId(testOrder.getId())
                .trackingCode("TRACK123")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(put("/api/orders/ship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        // Verify tracking code not added
        Order unchangedOrder = orderRepo.findById(testOrder.getId()).orElseThrow();
        assertThat(unchangedOrder.getTrackingCode()).isNull();
    }

    @Test
    void shouldHandleMultipleOrdersForSameCustomer() throws Exception {
        // Given - create second order for same customer
        orderRepo.save(Order.builder()
                .account(testAccount)
                .shippingAddress(testAddress)
                .orderStatus(OrderStatus.PROCESSING)
                .paymentReference("test_payment_ref_2")
                .total(BigDecimal.valueOf(162.00))
                .build());

        // When
        mockMvc.perform(get("/api/orders")
                        .cookie(authTokenHelper.adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders.length()").value(2));

        // Verify both orders in database
        List<Order> orders = orderRepo.findAll();
        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getAccount).containsOnly(testAccount);
    }

    @Test
    void shouldUpdateOrderStatusThroughCompleteLifecycle() throws Exception {
        // PAID -> PREPARING
        UpdateOrderStatusRequest preparingRequest = UpdateOrderStatusRequest.builder()
                .orderId(testOrder.getId())
                .status(OrderStatus.PROCESSING)
                .build();

        mockMvc.perform(put("/api/orders")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(preparingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSING"));

        Order afterPreparing = orderRepo.findById(testOrder.getId()).orElseThrow();
        assertThat(afterPreparing.getOrderStatus()).isEqualTo(OrderStatus.PROCESSING);

        // PREPARING -> SHIPPED (with tracking)
        ShipOrderRequest shipRequest = ShipOrderRequest.builder()
                .orderId(testOrder.getId())
                .trackingCode("TRACK999")
                .build();

        mockMvc.perform(put("/api/orders/ship")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shipRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPED"));

        Order afterShipping = orderRepo.findById(testOrder.getId()).orElseThrow();
        assertThat(afterShipping.getOrderStatus()).isEqualTo(OrderStatus.SHIPPED);
        assertThat(afterShipping.getTrackingCode()).isEqualTo("TRACK999");

        // SHIPPED -> DELIVERED
        UpdateOrderStatusRequest deliveredRequest = UpdateOrderStatusRequest.builder()
                .orderId(testOrder.getId())
                .status(OrderStatus.DELIVERED)
                .build();

        mockMvc.perform(put("/api/orders")
                        .cookie(authTokenHelper.adminCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveredRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));

        Order afterDelivery = orderRepo.findById(testOrder.getId()).orElseThrow();
        assertThat(afterDelivery.getOrderStatus()).isEqualTo(OrderStatus.DELIVERED);
    }
}