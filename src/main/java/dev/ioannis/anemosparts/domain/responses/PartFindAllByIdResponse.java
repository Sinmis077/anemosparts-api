package dev.ioannis.anemosparts.domain.responses;

import dev.ioannis.anemosparts.domain.PartDto;

import java.util.List;

public record PartFindAllByIdResponse(List<PartDto> parts) {
}
