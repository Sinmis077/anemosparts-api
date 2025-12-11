package dev.ioannis.anemosparts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartSummaryDto {
    private Long id;
    private String name;
    private String description;
    private String oemNumber;
    private String partNumber;
    private BigDecimal price;
    private Long quantity;
    private Optional<String> thumbnailSrc;

    private String brandIconSrc;

    private List<Long> modelIds;
}
