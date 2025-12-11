package dev.ioannis.anemosparts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "oem_number", referencedColumnName = "id")
    private OemNumber oemNumber;
    @Column(length = 20, nullable = false)
    private String partNumber;
    @ColumnDefault("1")
    @Column(nullable = false)
    private Long quantity;

    @OneToMany(fetch =FetchType.EAGER, orphanRemoval = true, mappedBy = "part")
    @Builder.Default
    private List<PartImage> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "part_model",
            joinColumns = @JoinColumn(name = "partId"),
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "modelId",
                            referencedColumnName = "id"
                    )
                }
            )
    @Builder.Default
    private List<Model> models =  new ArrayList<>();

    @OneToMany(mappedBy = "partId")
    @Builder.Default
    private List<PartTransaction> transactions = new ArrayList<>();
}
