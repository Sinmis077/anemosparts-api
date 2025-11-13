package dev.ioannis.anemosparts.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Bank_Card")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BankCard {
    @Id
    private Long id;

    private String number;
    private Long cvv;
    private LocalDate expirationDate;
}
