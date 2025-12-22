package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.entities.Address;

public interface AddressService {
    Address findByExample(Address address);

    Address createAddress(Address address);

    boolean exists(Address address);

}
