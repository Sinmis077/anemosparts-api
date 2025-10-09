package dev.ioannis.anemosparts.business;

import dev.ioannis.anemosparts.domain.Model;

import java.util.List;
import java.util.Optional;

public interface ModelService {
    List<Model> findAll();
    Optional<Model> findById(Model model);
}
