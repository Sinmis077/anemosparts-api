package dev.ioannis.anemosparts.entities;

import dev.ioannis.anemosparts.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private OrderStatus orderStatus;
    private String trackingCode;
    private String paymentReference;

    @ManyToOne
    @JoinColumn(name = "accountUuid", referencedColumnName = "uuid")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "shippingAddressId", referencedColumnName = "id")
    private Address shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<PartTransaction> partTransactions;
}
