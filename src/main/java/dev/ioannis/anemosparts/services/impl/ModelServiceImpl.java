package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.ModelDto;
import dev.ioannis.anemosparts.domain.requests.ModelSaveRequest;
import dev.ioannis.anemosparts.domain.responses.ModelFindAllResponse;
import dev.ioannis.anemosparts.entities.Brand;
import dev.ioannis.anemosparts.entities.Model;
import dev.ioannis.anemosparts.exceptions.NullBrandException;
import dev.ioannis.anemosparts.mappers.ModelMapper;
import dev.ioannis.anemosparts.repositories.BrandRepo;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.services.ModelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // BUG: This bypasses the protected save(Model) method's brand validation
        // Should call save(model) instead of modelRepo.save(model) for consistency
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
        // BAD HABIT: Empty error message in exception - should provide meaningful message
        if(model.getBrand() == null || model.getBrand().getId() == null) {
            throw new NullBrandException("Brand should not be null");
        }

        var brand = brandRepo.findById(model.getBrand().getId()).orElseThrow(() -> new EntityNotFoundException("Brand with id " + model.getBrand().getId() + " not found"));

        model.setBrand(brand);

        return modelRepo.save(model);
    }

    @Override
    public void delete(Long id) {
        if(!modelRepo.existsById(id)) {
            throw new EntityNotFoundException("Model with id " + id + " does not exist");
        }

        modelRepo.deleteById(id);
    }
}
