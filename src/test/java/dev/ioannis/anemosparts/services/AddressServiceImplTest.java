package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.entities.Account;
import dev.ioannis.anemosparts.entities.Address;
import dev.ioannis.anemosparts.repositories.AddressRepo;
import dev.ioannis.anemosparts.services.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepo addressRepo;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Account account;
    private Address address;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .email("test@example.com")
                .build();

        address = Address.builder()
                .forename("John")
                .surname("Doe")
                .houseNumber("123")
                .street("Main St")
                .city("City")
                .state("State")
                .postalCode("12345")
                .country("Country")
                .extras("Apt 4B")
                .account(account)
                .build();
    }

    @Test
    void findByExample_returnsAddress_whenExists() {
        when(addressRepo.findOne(any(Example.class))).thenReturn(Optional.of(address));

        var result = addressService.findByExample(address);

        assertNotNull(result);
        assertEquals("John", result.getForename());
        assertEquals("Main St", result.getStreet());
        verify(addressRepo).findOne(any(Example.class));
    }

    @Test
    void findByExample_returnsNull_whenNotExists() {
        when(addressRepo.findOne(any(Example.class))).thenReturn(Optional.empty());

        var result = addressService.findByExample(address);

        assertNull(result);
        verify(addressRepo).findOne(any(Example.class));
    }

    @Test
    void createAddress_succeeds() {
        when(addressRepo.save(any())).thenReturn(address);

        var result = addressService.createAddress(address);

        assertNotNull(result);
        assertEquals("John", result.getForename());
        assertEquals("Main St", result.getStreet());
        verify(addressRepo).save(address);
    }

    @Test
    void exists_returnsTrue_whenAddressExists() {
        when(addressRepo.exists(any(Example.class))).thenReturn(true);

        var result = addressService.exists(address);

        assertTrue(result);
        verify(addressRepo).exists(any(Example.class));
    }

    @Test
    void exists_returnsFalse_whenAddressDoesNotExist() {
        when(addressRepo.exists(any(Example.class))).thenReturn(false);

        var result = addressService.exists(address);

        assertFalse(result);
        verify(addressRepo).exists(any(Example.class));
    }
}
