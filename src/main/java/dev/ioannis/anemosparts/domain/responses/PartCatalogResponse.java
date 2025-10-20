package dev.ioannis.anemosparts.domain.responses;

import dev.ioannis.anemosparts.domain.PartSummaryDto;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PartCatalogResponse {
    List<PartSummaryDto> parts;
}
