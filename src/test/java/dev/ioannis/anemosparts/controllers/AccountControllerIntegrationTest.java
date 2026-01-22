package dev.ioannis.anemosparts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ioannis.anemosparts.domain.requests.UpdateAccountRequest;
import dev.ioannis.anemosparts.entities.*;
import dev.ioannis.anemosparts.enums.OrderStatus;
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
 * Integration tests for Account management
 * Tests customer account operations and order history
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private AddressRepo addressRepo;

    private Account testAccount;
    private Address testAddress;

    @Autowired
    private AuthTokenHelper authTokenHelper;

    @BeforeEach
    void setUp() {
        orderRepo.deleteAll();
        addressRepo.deleteAll();
        accountRepo.deleteAll();

        // Create test account
        testAccount = authTokenHelper.generateCustomer();

        // Create test address
        testAddress = addressRepo.save(Address.builder()
                .forename("John")
                .surname("Doe")
                .houseNumber("456")
                .street("Customer Street")
                .city("Limassol")
                .postalCode("54321")
                .state("Limassol")
                .country("Cyprus")
                .account(testAccount)
                .build());
    }

    @Test
    void shouldGetUserOrders() throws Exception {
        orderRepo.save(Order.builder()
                .account(testAccount)
                .shippingAddress(testAddress)
                .orderStatus(OrderStatus.PAID)
                .paymentReference("ref_001")
                .total(BigDecimal.valueOf(100.00))
                .build());

        orderRepo.save(Order.builder()
                .account(testAccount)
                .shippingAddress(testAddress)
                .orderStatus(OrderStatus.SHIPPED)
                .paymentReference("ref_002")
                .total(BigDecimal.valueOf(200.00))
                .trackingCode("TRACK123")
                .build());

        mockMvc.perform(get("/api/accounts/me/orders")
                        .cookie(authTokenHelper.customerCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders.length()").value(2))
                .andExpect(jsonPath("$.orders[0].paymentReference").exists())
                .andExpect(jsonPath("$.orders[1].trackingCode").value("TRACK123"));

        List<Order> userOrders = orderRepo.findAllByAccountEmail("customer@test.com").orElse(List.of());
        assertThat(userOrders).hasSize(2);
        assertThat(userOrders).allMatch(o -> o.getAccount().getEmail().equals(testAccount.getEmail()));
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoOrders() throws Exception {
        mockMvc.perform(get("/api/accounts/me/orders")
                        .cookie(authTokenHelper.customerCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders").isEmpty());
    }

    @Test
    void shouldUpdateOwnAccountDetails() throws Exception {
        UpdateAccountRequest request = UpdateAccountRequest.builder()
                .forename("Jane")
                .surname("Smith")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/accounts/{email}", "customer@test.com")
                        .cookie(authTokenHelper.customerCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forename").value("Jane"))
                .andExpect(jsonPath("$.surname").value("Smith"))
                .andExpect(jsonPath("$.email").value("customer@test.com"));

        Account updatedAccount = accountRepo.findByEmail("customer@test.com").orElseThrow();
        assertThat(updatedAccount.getForename()).isEqualTo("Jane");
        assertThat(updatedAccount.getSurname()).isEqualTo("Smith");
        assertThat(updatedAccount.getEmail()).isEqualTo("customer@test.com"); // Email unchanged
    }

    @Test
    void shouldNotUpdateOtherUsersAccount() throws Exception {
        // Given - create another user
        accountRepo.save(Account.builder()
                .email("other@test.com")
                .password("password")
                .forename("Other")
                .surname("User")
                .build());

        UpdateAccountRequest request = UpdateAccountRequest.builder()
                .forename("Hacker")
                .surname("Attempt")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/accounts/{email}", "other@test.com")
                        .cookie(authTokenHelper.customerCookie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden());

        Account unchangedAccount = accountRepo.findByEmail("other@test.com").orElseThrow();
        assertThat(unchangedAccount.getForename()).isEqualTo("Other");
        assertThat(unchangedAccount.getSurname()).isEqualTo("User");
    }

    @Test
    void shouldOnlyGetOwnOrders() throws Exception {
        Account otherAccount = accountRepo.save(Account.builder()
                .email("other@test.com")
                .password("password")
                .forename("Other")
                .surname("User")
                .build());

        Address otherAddress = addressRepo.save(Address.builder()
                .forename("Other")
                .surname("User")
                .houseNumber("789")
                .street("Other Street")
                .city("Paphos")
                .state("Paphos")
                .postalCode("99999")
                .country("Cyprus")
                .account(otherAccount)
                .build());

        orderRepo.save(Order.builder()
                .account(testAccount)
                .shippingAddress(testAddress)
                .orderStatus(OrderStatus.PAID)
                .paymentReference("own_ref")
                .total(BigDecimal.valueOf(100.00))
                .build());

        orderRepo.save(Order.builder()
                .account(otherAccount)
                .shippingAddress(otherAddress)
                .orderStatus(OrderStatus.PAID)
                .paymentReference("other_ref")
                .total(BigDecimal.valueOf(200.00))
                .build());

        mockMvc.perform(get("/api/accounts/me/orders")
                        .cookie(authTokenHelper.customerCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders.length()").value(1))
                .andExpect(jsonPath("$.orders[0].paymentReference").value("own_ref"));

        List<Order> ownOrders = orderRepo.findAllByAccountEmail("customer@test.com").orElse(List.of());
        assertThat(ownOrders).hasSize(1);
        assertThat(ownOrders.get(0).getPaymentReference()).isEqualTo("own_ref");
    }

    @Test
    @WithMockUser(username = "customer@test.com", roles = "USER")
    void shouldGetOrdersInCorrectStatusOrder() throws Exception {
        orderRepo.save(Order.builder()
                .account(testAccount)
                .shippingAddress(testAddress)
                .orderStatus(OrderStatus.DELIVERED)
                .paymentReference("ref_delivered")
                .total(BigDecimal.valueOf(100.00))
                .build());

        orderRepo.save(Order.builder()
                .account(testAccount)
                .shippingAddress(testAddress)
                .orderStatus(OrderStatus.PAID)
                .paymentReference("ref_paid")
                .total(BigDecimal.valueOf(150.00))
                .build());

        orderRepo.save(Order.builder()
                .account(testAccount)
                .shippingAddress(testAddress)
                .orderStatus(OrderStatus.SHIPPED)
                .paymentReference("ref_shipped")
                .total(BigDecimal.valueOf(200.00))
                .trackingCode("TRACK789")
                .build());

        mockMvc.perform(get("/api/accounts/me/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders.length()").value(3));

        List<Order> allOrders = orderRepo.findAllByAccountEmail("customer@test.com").orElse(List.of());
        assertThat(allOrders).hasSize(3);
    }

    @Test
    void shouldReturnOrderDetailsWithTrackingCode() throws Exception {
        orderRepo.save(Order.builder()
                .account(testAccount)
                .shippingAddress(testAddress)
                .orderStatus(OrderStatus.SHIPPED)
                .paymentReference("ref_with_tracking")
                .total(BigDecimal.valueOf(300.00))
                .trackingCode("TRACKING_CODE_XYZ")
                .build());

        // When & Then
        mockMvc.perform(get("/api/accounts/me/orders")
                        .cookie(authTokenHelper.customerCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].trackingCode").value("TRACKING_CODE_XYZ"))
                .andExpect(jsonPath("$.orders[0].status").value("SHIPPED"));
    }

    @Test
    @WithMockUser(username = "nonexistent@test.com", roles = "USER")
    void shouldReturn404WhenUpdatingNonExistentAccount() throws Exception {
        UpdateAccountRequest request = UpdateAccountRequest.builder()
                .forename("New")
                .surname("Name")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/accounts/{email}", "nonexistent@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRequireAuthenticationForGettingOrders() throws Exception {
        // When & Then - no authentication
        mockMvc.perform(get("/api/accounts/me/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRequireAuthenticationForUpdatingAccount() throws Exception {
        // Given
        UpdateAccountRequest request = UpdateAccountRequest.builder()
                .forename("New")
                .surname("Name")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/accounts/{email}", "customer@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden());
    }
}