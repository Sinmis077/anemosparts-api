package dev.ioannis.anemosparts.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@NoArgsConstructor
@Data
public class OemNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String number;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "oemnumber_model", joinColumns = @JoinColumn(name = "oeam_number_id", referencedColumnName = "id"), inverseJoinColumns = {@JoinColumn(name = "modelid", referencedColumnName = "id"),})
    private List<Model> model;
}
