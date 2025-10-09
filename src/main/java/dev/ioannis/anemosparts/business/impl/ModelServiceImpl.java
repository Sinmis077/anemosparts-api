package dev.ioannis.anemosparts.business.impl;

import dev.ioannis.anemosparts.business.ModelService;
import dev.ioannis.anemosparts.domain.Model;
import dev.ioannis.anemosparts.mapper.ModelMapper;
import dev.ioannis.anemosparts.persistance.ModelRepo;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ModelServiceImpl implements ModelService {
    private final ModelRepo modelRepo;

    @Override
    public List<Model> findAll() {
        return ModelMapper.INSTANCE.toModels(IterableUtils.toList(modelRepo.findAll()));
    }

    @Override
    public Optional<Model> findById(Model model) {
        return Optional.ofNullable(ModelMapper.INSTANCE.toModel(modelRepo.findById(ModelMapper.INSTANCE.toModelEntityId(model)).orElse(null)));
    }
}
