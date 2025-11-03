package dev.ioannis.anemosparts.domain.responses;

import dev.ioannis.anemosparts.domain.ModelDto;

import java.util.List;

public record ModelFindAllResponse(List<ModelDto> models) {}
