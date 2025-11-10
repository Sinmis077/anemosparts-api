package dev.ioannis.anemosparts.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "brands")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = true)
    private String iconUrl;

    @OneToMany(mappedBy = "brand")
    @JsonManagedReference
    private List<Model> models;
}
