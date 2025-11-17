package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.ModelDto;
import dev.ioannis.anemosparts.domain.requests.ModelSaveRequest;
import dev.ioannis.anemosparts.domain.responses.ModelFindAllResponse;

public interface ModelService {
    ModelFindAllResponse findAll();

    ModelDto save(ModelSaveRequest request);

    ModelDto update(Long id, ModelSaveRequest request);

    void delete(Long id);
}
