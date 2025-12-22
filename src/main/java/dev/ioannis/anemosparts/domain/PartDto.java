package dev.ioannis.anemosparts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartDto {
    private long id;
    private String name;
    private String description;
    private String oemNumber;
    private String partNumber;
    private BigDecimal price;
    private Long quantity;

    private List<ModelDto> models;

    private List<PartImageDto> images;
}
