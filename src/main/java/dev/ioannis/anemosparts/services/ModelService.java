package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.BrandDto;
import dev.ioannis.anemosparts.domain.ModelDto;
import dev.ioannis.anemosparts.domain.PartDto;

import java.util.List;

public interface ModelService {
    List<ModelDto> findAll();

    ModelDto save(ModelDto modelDto);

    void delete(Long id);
}
