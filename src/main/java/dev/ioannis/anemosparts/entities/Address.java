package dev.ioannis.anemosparts.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    private String extras;
    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
