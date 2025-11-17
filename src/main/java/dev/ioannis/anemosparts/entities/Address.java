package dev.ioannis.anemosparts.entities;

import dev.ioannis.anemosparts.enums.AddressType;
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
    @JoinColumn(name = "userUuid", referencedColumnName = "uuid")
    private User user;

    private AddressType type;

    private String extras;
    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
