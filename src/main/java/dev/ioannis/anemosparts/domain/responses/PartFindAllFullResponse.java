package dev.ioannis.anemosparts.domain.responses;

import dev.ioannis.anemosparts.domain.PartDto;

import java.util.List;

public record PartFindAllFullResponse(List<PartDto> parts) {}
