package dev.ioannis.anemosparts.entities;

import dev.ioannis.anemosparts.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Accounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    private String forename;
    private String surname;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRole role = UserRole.CUSTOMER;

    @OneToMany(mappedBy = "account")
    private List<Address> addresses;

    @OneToMany(mappedBy = "account")
    private List<Order> orders;
}
