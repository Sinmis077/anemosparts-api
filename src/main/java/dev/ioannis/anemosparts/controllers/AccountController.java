package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.requests.UpdateAccountRequest;
import dev.ioannis.anemosparts.domain.responses.FindOrdersResponse;
import dev.ioannis.anemosparts.domain.responses.UserDetails;
import dev.ioannis.anemosparts.services.AccountService;
import dev.ioannis.anemosparts.services.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final OrderService orderService;
    private final AccountService accountService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me/orders")
    public FindOrdersResponse getOrders(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        return new FindOrdersResponse(orderService.findOrdersByCustomerEmail(userDetails.getUsername()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{email}")
    public UserDetails getUserDetails(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails, @PathVariable @NotNull @Email String email, @RequestBody @Valid UpdateAccountRequest request) {
        if (!Objects.equals(userDetails.getUsername(), email)) {
            throw new AccessDeniedException("You are not authorized to perform this action.");
        }
        var account = accountService.updateAccount(email, request);

        return new UserDetails(account.getForename(), account.getSurname(), account.getEmail());
    }
}
