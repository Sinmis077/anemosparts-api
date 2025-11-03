package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.domain.responses.PartFindAllResponse;
import dev.ioannis.anemosparts.entities.Model;
import dev.ioannis.anemosparts.entities.OemNumber;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.exceptions.NullModelsFoundException;
import dev.ioannis.anemosparts.exceptions.OemNumberDoesNotExistException;
import dev.ioannis.anemosparts.mappers.PartMapper;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.repositories.OemRepo;
import dev.ioannis.anemosparts.repositories.PartRepo;
import dev.ioannis.anemosparts.services.PartService;
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
        var part = new Part();
        part.setName(request.getName());
        part.setPartNumber(request.getPartNumber());
        part.setOemNumber(OemNumber.builder().number(request.getOemNumber()).build());
        part.setPrice(request.getPrice());
        part.setDescription(request.getDescription());
        part.setModels(request.getModelIds().stream().map(modelId -> Model.builder().id(modelId).build()).toList());

        return mapper.toDto(save(part));
    }

    @Override
    public PartDto update(Long id, PartSaveRequest request) {
        var part = new Part();
        part.setId(id);
        part.setName(request.getName());
        part.setPartNumber(request.getPartNumber());
        part.setOemNumber(OemNumber.builder().number(request.getOemNumber()).build());
        part.setPrice(request.getPrice());
        part.setDescription(request.getDescription());
        part.setModels(request.getModelIds().stream().map(modelId -> Model.builder().id(modelId).build()).toList());

        return mapper.toDto(save(part));
    }

    protected Part save(Part part) {
        if(modelRepo.findAllById(part.getModels().stream().map(Model::getId).toList()).size() != part.getModels().size()) {
            throw new NullModelsFoundException("");
        }

        part.setModels(modelRepo.findAllById(part.getModels().stream().map(Model::getId).toList()));

        if(oemRepo.existsByNumber(part.getOemNumber().getNumber())) {
            throw new OemNumberDoesNotExistException("");
        }

        part.setOemNumber(oemRepo.findByNumber(part.getOemNumber().getNumber()));

        return partRepo.save(part);
    }

    @Override
    public void delete(long id) {
        partRepo.deleteById(id);
    }
}
