package dev.ioannis.anemosparts.entities;

import dev.ioannis.anemosparts.enums.UserDefaults;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "User")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID uuid;

    private String email;
    private String password;

    private String forename;
    private String surname;

    private LocalDate birthday;

    private List<Address> addresses;
    private List<BankCard> bankCards;
}
