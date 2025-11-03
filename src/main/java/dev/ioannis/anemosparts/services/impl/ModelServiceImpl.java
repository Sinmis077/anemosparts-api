package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.ModelDto;
import dev.ioannis.anemosparts.domain.requests.ModelSaveRequest;
import dev.ioannis.anemosparts.domain.responses.ModelFindAllResponse;
import dev.ioannis.anemosparts.entities.Model;
import dev.ioannis.anemosparts.mappers.ModelMapper;
import dev.ioannis.anemosparts.repositories.BrandRepo;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.services.ModelService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ModelServiceImpl implements ModelService {
    private final ModelRepo modelRepo;
    private final ModelMapper mapper;

    private final BrandRepo brandRepo;

    @Override
    @Transactional
    public ModelFindAllResponse findAll() {
        return new ModelFindAllResponse(mapper.toDtos(modelRepo.findAll()).stream().map(ModelDto::toString).toList());
    }

    @Override
    public ModelDto save(ModelSaveRequest request) {
        var brand = brandRepo.findById(request.getBrandId())
                    .orElseThrow(() -> new EntityNotFoundException("Brand with id " + request.getBrandId() + " not found"));

        var model = new Model();
        model.setName(request.getName());
        model.setProductionYear(request.getProductionYear());

        model.setBrand(brand);

        return mapper.toDto(modelRepo.save(model));
    }

    @Override
    public ModelDto update(Long id, ModelSaveRequest request) {
        var brand = brandRepo.findById(request.getBrandId())
                .orElseThrow(() -> new EntityNotFoundException("Brand with id " + request.getBrandId() + " not found"));

        var model = new Model();
        model.setId(id);
        model.setName(request.getName());
        model.setProductionYear(request.getProductionYear());

        model.setBrand(brand);

        return mapper.toDto(modelRepo.save(model));
    }

    @Override
    public void delete(Long id) {
        modelRepo.deleteById(id);
    }
}
