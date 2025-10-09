package dev.ioannis.anemosparts.business;

import dev.ioannis.anemosparts.domain.Brand;

import java.util.List;

public interface BrandService {
    Brand save(Brand brand);
    List<Brand> findAll();

}
