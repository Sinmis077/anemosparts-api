package dev.ioannis.anemosparts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "shippingAddress")
    private List<Order> orders;

    private String extras;
    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
