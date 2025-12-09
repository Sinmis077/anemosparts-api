package dev.ioannis.anemosparts.domain.responses;

import dev.ioannis.anemosparts.domain.PartSummaryDto;

import java.util.List;

public record PartFindAllSummariesResponse(List<PartSummaryDto> parts) {}