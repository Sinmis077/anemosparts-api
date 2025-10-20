package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.BrandDto;

import java.util.List;

public interface BrandService {
    List<BrandDto> findAll();

    BrandDto save(BrandDto brandDTO);

    void delete(Long id);
}
