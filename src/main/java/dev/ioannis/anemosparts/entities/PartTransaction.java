package dev.ioannis.anemosparts.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.ioannis.anemosparts.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "partTransactions")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PartTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    @Builder.Default
    private TransactionStatus status = TransactionStatus.HOLD;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "partId", referencedColumnName = "id")
    @JsonIgnore
    private Part part;

    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    @JsonIgnore
    private Order order;
}
