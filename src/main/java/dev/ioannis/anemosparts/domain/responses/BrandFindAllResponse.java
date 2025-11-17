package dev.ioannis.anemosparts.domain.responses;

import dev.ioannis.anemosparts.domain.BrandDto;

import java.util.List;

public record BrandFindAllResponse(List<BrandDto> brands) {
}
