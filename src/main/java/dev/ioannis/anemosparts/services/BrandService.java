package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.BrandDto;
import dev.ioannis.anemosparts.domain.requests.BrandSaveRequest;
import dev.ioannis.anemosparts.domain.responses.BrandFindAllResponse;

public interface BrandService {
    BrandFindAllResponse findAll();

    BrandDto save(BrandSaveRequest request);

    BrandDto update(Long id, BrandSaveRequest request);

    void delete(Long id);
}
