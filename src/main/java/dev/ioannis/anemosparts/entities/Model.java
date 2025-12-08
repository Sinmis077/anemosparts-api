package dev.ioannis.anemosparts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "brand", referencedColumnName = "id")
    private Brand brand;

    @ManyToMany(mappedBy = "models")
    @Builder.Default
    private List<Part> parts = new ArrayList<>();

    @ManyToMany(mappedBy = "models")
    @Builder.Default
    private List<OemNumber> oemNumbers = new ArrayList<>();
}
