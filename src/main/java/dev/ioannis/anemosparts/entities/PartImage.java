package dev.ioannis.anemosparts.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "part_images")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PartImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String source;

    @ColumnDefault("false")
    @Builder.Default
    private Boolean isThumbnail = false;

    @ManyToOne
    @JoinColumn(name = "part_id", referencedColumnName = "id")
    @JsonIgnore
    private Part part;
}