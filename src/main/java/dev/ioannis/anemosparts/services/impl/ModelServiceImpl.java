package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.ModelDto;
import dev.ioannis.anemosparts.domain.requests.ModelSaveRequest;
import dev.ioannis.anemosparts.domain.responses.ModelFindAllResponse;
import dev.ioannis.anemosparts.entities.Brand;
import dev.ioannis.anemosparts.entities.Model;
import dev.ioannis.anemosparts.mappers.ModelMapper;
import dev.ioannis.anemosparts.repositories.BrandRepo;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.services.ModelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ModelServiceImpl implements ModelService {
    private final ModelRepo modelRepo;
    private final ModelMapper mapper;

    private final BrandRepo brandRepo;

    @Override
    @Transactional(readOnly = true)
    public ModelFindAllResponse findAll() {
        return new ModelFindAllResponse(mapper.toDtos(modelRepo.findAll()).stream().toList());
    }

    @Override
    public ModelDto save(ModelSaveRequest request) {
        var brand = new Brand();
        brand.setId(request.getBrandId());

        var model = new Model();
        model.setName(request.getName());
        model.setProductionYear(request.getProductionYear());
        model.setBrand(brand);

        return mapper.toDto(save(model));
    }

    @Override
    public ModelDto update(Long id, ModelSaveRequest request) {
        var brand = new Brand();
        brand.setId(request.getBrandId());

        var model = new Model();
        model.setId(id);
        model.setName(request.getName());
        model.setProductionYear(request.getProductionYear());
        model.setBrand(brand);

        return mapper.toDto(save(model));
    }

    protected Model save(Model model) {
        log.info("Saving model: {}", model);

        if(model.getBrand() == null || model.getBrand().getId() == null) {
            throw new IllegalArgumentException("Brand should not be null");
        }

        var brand = brandRepo.findById(model.getBrand().getId()).orElseThrow(() -> new EntityNotFoundException("Brand with id " + model.getBrand().getId() + " not found"));

        model.setBrand(brand);

        var dbModel = modelRepo.save(model);

        log.info("Successfully saved model with id: {}", dbModel.getId());
        return dbModel;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting model with id: {}", id);

        if(!modelRepo.existsById(id)) {
            throw new EntityNotFoundException("Model with id " + id + " does not exist");
        }

        modelRepo.deleteById(id);
        log.info("Successfully deleted model with id: {}", id);
    }
}
