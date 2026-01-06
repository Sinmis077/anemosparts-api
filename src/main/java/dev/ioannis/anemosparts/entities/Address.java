package dev.ioannis.anemosparts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Addresses")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accountUuid", referencedColumnName = "uuid")
    private Account account;

    @OneToMany(mappedBy = "shippingAddress")
    private List<Order> orders;

    @Column(nullable = false)
    private String forename;
    @Column(nullable = false)
    private String surname;

    private String extras;
    @Column(nullable = false)
    private String houseNumber;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String postalCode;
    @Column(nullable = false)
    private String country;
}
