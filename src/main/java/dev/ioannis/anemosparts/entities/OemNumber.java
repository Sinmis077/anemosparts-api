package dev.ioannis.anemosparts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "oem_numbers")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OemNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String number;

    @ManyToMany
    @JoinTable(name = "oemnumber_model", joinColumns = @JoinColumn(name = "oem_number_id", referencedColumnName = "id"), inverseJoinColumns = {@JoinColumn(name = "modelid", referencedColumnName = "id"),})
    @Builder.Default
    private List<Model> models =  new ArrayList<>();

    @OneToMany(mappedBy = "oemNumber")
    @Builder.Default
    private List<Part> parts = new ArrayList<>();
}
