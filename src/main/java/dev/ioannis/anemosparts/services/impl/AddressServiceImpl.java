package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.entities.Address;
import dev.ioannis.anemosparts.repositories.AddressRepo;
import dev.ioannis.anemosparts.services.AddressService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {
    private final AddressRepo addressRepo;

    @Override
    public Address findByExample(Address address) {
        return addressRepo.findOne(
                Example.of(address,
                        ExampleMatcher.matching().withIgnorePaths(
                                "id",
                                "orders"
                        )
                )
        ).orElse(null);
    }

    @Override
    public Address createAddress(Address address) {
        return addressRepo.save(address);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Address address) {
        Address probe = Address.builder()
                .forename(address.getForename())
                .surname(address.getSurname())
                .extras(address.getExtras())
                .houseNumber(address.getHouseNumber())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .account(address.getAccount())
                .build();

        Example<Address> example = Example.of(probe,
                ExampleMatcher.matching()
                        .withIgnorePaths(
                                "id",
                                "orders"
                        )
        );

        return addressRepo.exists(example);
    }
}
