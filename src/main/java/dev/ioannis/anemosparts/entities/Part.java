package dev.ioannis.anemosparts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Table(name = "parts")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(length = 500, nullable = false)
    private String description;
    @ColumnDefault("10")
    private Double price;
    @ManyToOne(fetch = FetchType.LAZY)
    private OemNumber oemNumber;
    @Column(length = 20, nullable = false)
    private String partNumber;
    @ColumnDefault("1")
    @Column(nullable = false)
    private Integer quantity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "part_model", joinColumns = @JoinColumn(name = "partId"), inverseJoinColumns = {@JoinColumn(name = "modelId", referencedColumnName = "id"),})
    private List<Model> models;
}
