package dev.ioannis.anemosparts.daemons;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Address")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Address {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accountUuid", referencedColumnName = "uuid")
    private Account account;

    private AddressType type;

    private String extras;
    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
