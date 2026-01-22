package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.requests.RegisterAccountRequest;
import dev.ioannis.anemosparts.domain.requests.UpdateAccountRequest;
import dev.ioannis.anemosparts.entities.Account;
import dev.ioannis.anemosparts.enums.UserRole;
import dev.ioannis.anemosparts.repositories.AccountRepo;
import dev.ioannis.anemosparts.services.impl.AccountServiceImpl;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepo accountRepo;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    private RegisterAccountRequest registerRequest;
    private UpdateAccountRequest updateRequest;
    private Account account;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterAccountRequest(
                "John",
                "Doe",
                "test@example.com",
                "password123"
        );

        updateRequest = new UpdateAccountRequest("Jane", "Smith");

        account = Account.builder()
                .forename("John")
                .surname("Doe")
                .email("test@example.com")
                .password("encodedPassword")
                .role(UserRole.CUSTOMER)
                .build();
    }

    @Test
    void createAccount_succeeds_whenNewEmail() {
        when(accountRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(accountRepo.save(any())).thenReturn(account);

        var result = accountService.createAccount(registerRequest);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(accountRepo).save(any());
    }

    @Test
    void createAccount_upgradesGuestAccount_whenPasswordlessAccountExists() {
        Account guestAccount = Account.builder()
                .email("test@example.com")
                .password("")
                .build();

        when(accountRepo.findByEmail(anyString())).thenReturn(Optional.of(guestAccount));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(accountRepo.save(any())).thenReturn(account);

        var result = accountService.createAccount(registerRequest);

        assertNotNull(result);
        verify(accountRepo).save(any());
    }

    @Test
    void createAccount_throwsException_whenAccountAlreadyExists() {
        when(accountRepo.findByEmail(anyString())).thenReturn(Optional.of(account));

        assertThrows(EntityExistsException.class, () -> accountService.createAccount(registerRequest));
        verify(accountRepo, never()).save(any());
    }

    @Test
    void createAdminAccount_succeeds_andSetsAdminRole() {
        when(accountRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(accountRepo.save(any())).thenAnswer(invocation -> {
            Account acc = invocation.getArgument(0);
            return acc;
        });

        var result = accountService.createAdminAccount(registerRequest);

        assertNotNull(result);
        assertEquals(UserRole.ADMIN, result.getRole());
        verify(accountRepo, times(2)).save(any());
    }

    @Test
    void createGuestAccount_succeeds() {
        Account guestAccount = Account.builder()
                .email("guest@example.com")
                .build();

        when(accountRepo.save(any())).thenReturn(guestAccount);

        var result = accountService.createGuestAccount(guestAccount);

        assertNotNull(result);
        assertEquals("guest@example.com", result.getEmail());
        verify(accountRepo).save(guestAccount);
    }

    @Test
    void updateAccount_succeeds_whenAccountExists() {
        when(accountRepo.findByEmail(anyString())).thenReturn(Optional.of(account));
        when(accountRepo.save(any())).thenReturn(account);

        var result = accountService.updateAccount("test@example.com", updateRequest);

        assertNotNull(result);
        assertEquals("Jane", result.getForename());
        assertEquals("Smith", result.getSurname());
        verify(accountRepo).save(any());
    }

    @Test
    void updateAccount_throwsException_whenAccountDoesNotExist() {
        when(accountRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> accountService.updateAccount("test@example.com", updateRequest));
        verify(accountRepo, never()).save(any());
    }

    @Test
    void findByEmail_returnsAccount_whenExists() {
        when(accountRepo.findByEmail(anyString())).thenReturn(Optional.of(account));

        var result = accountService.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void findByEmail_returnsEmpty_whenNotExists() {
        when(accountRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        var result = accountService.findByEmail("nonexistent@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void existsByEmail_returnsTrue_whenExists() {
        when(accountRepo.existsByEmail(anyString())).thenReturn(true);

        var result = accountService.existsByEmail("test@example.com");

        assertTrue(result);
    }

    @Test
    void existsByEmail_returnsFalse_whenNotExists() {
        when(accountRepo.existsByEmail(anyString())).thenReturn(false);

        var result = accountService.existsByEmail("nonexistent@example.com");

        assertFalse(result);
    }
}
