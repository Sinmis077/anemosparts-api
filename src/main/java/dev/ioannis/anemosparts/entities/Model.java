package dev.ioannis.anemosparts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "models")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer productionYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand", referencedColumnName = "name")
    private Brand brand;
}
