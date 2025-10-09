package dev.ioannis.anemosparts.business;

import dev.ioannis.anemosparts.domain.Brand;
import dev.ioannis.anemosparts.domain.Model;
import dev.ioannis.anemosparts.domain.Part;

import java.util.List;
import java.util.Optional;

public interface ModelService {
    List<Model> findAll();
    List<Model> findByPart(Part part);
    List<Model> findByBrand(Brand brand);
    Optional<Model> findById(Model model);
    Model save(Model model);
    void delete(Model model);
}
