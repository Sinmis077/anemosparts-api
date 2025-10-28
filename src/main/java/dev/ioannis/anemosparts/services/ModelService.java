package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.ModelDto;
import dev.ioannis.anemosparts.domain.requests.ModelSaveRequest;

import java.util.List;

public interface ModelService {
    List<ModelDto> findAll();

    ModelDto save(ModelSaveRequest request);

    ModelDto update(Long id, ModelSaveRequest request);

    void delete(Long id);
}
