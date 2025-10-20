package dev.ioannis.anemosparts.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "part_images")
@NoArgsConstructor
@Data
public class PartImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String source;

    @ColumnDefault("false")
    private Boolean thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    private Part part;
}
