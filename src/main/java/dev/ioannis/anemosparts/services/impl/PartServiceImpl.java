package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.domain.responses.PartFindAllResponse;
import dev.ioannis.anemosparts.entities.Model;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.exceptions.EntityModelsNotFoundException;
import dev.ioannis.anemosparts.exceptions.OemNumberDoesNotExistException;
import dev.ioannis.anemosparts.mappers.PartMapper;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.repositories.OemRepo;
import dev.ioannis.anemosparts.repositories.PartRepo;
import dev.ioannis.anemosparts.services.PartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class PartServiceImpl implements PartService {

    private final PartRepo partRepo;
    private final ModelRepo modelRepo;
    private final OemRepo oemRepo;

    private final PartMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public PartFindAllResponse findAll() {
        return new PartFindAllResponse(mapper.toSummaries(partRepo.findAll()));
    }

    @Override
    public PartDto save(PartSaveRequest request) {
        if(request.getModelIds() == null || request.getModelIds().isEmpty()) {
            throw new IllegalArgumentException("Save request is missing models");
        }

        var part = mapper.toEntity(request);

        return mapper.toDto(save(part));
    }

    @Override
    public PartDto update(Long id, PartSaveRequest request) {
        if(request.getModelIds() == null || request.getModelIds().isEmpty()) {
            throw new IllegalArgumentException("Save request is missing models");
        }

        var part = mapper.toEntity(request);
        part.setId(id);

        return mapper.toDto(save(part));
    }

    protected Part save(Part part) {
        if(modelRepo.findAllById(part.getModels().stream().map(Model::getId).toList()).size() != part.getModels().size()) {
            throw new EntityModelsNotFoundException("");
        }

        part.setModels(modelRepo.findAllById(part.getModels().stream().map(Model::getId).toList()));

        if(part.getOemNumber() != null) {
            if(oemRepo.existsByNumber(part.getOemNumber().getNumber()))
                part.setOemNumber(oemRepo.findByNumber(part.getOemNumber().getNumber()));

            else throw new OemNumberDoesNotExistException("");
        }

        return partRepo.save(part);
    }

    @Override
    public void delete(long id) {
        if(partRepo.existsById(id)) {
            partRepo.deleteById(id);
        }
        else throw new EntityNotFoundException("Could not find part with id: " + id + " to delete");
    }
}
