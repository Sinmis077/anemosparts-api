package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.domain.responses.PartFindAllByIdResponse;
import dev.ioannis.anemosparts.domain.responses.PartFindAllResponse;
import dev.ioannis.anemosparts.domain.responses.PartFindAllSummariesResponse;
import dev.ioannis.anemosparts.entities.Model;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.mappers.PartMapper;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.repositories.OemRepo;
import dev.ioannis.anemosparts.repositories.PartImageRepo;
import dev.ioannis.anemosparts.repositories.PartRepo;
import dev.ioannis.anemosparts.services.PartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class PartServiceImpl implements PartService {

    private final PartRepo partRepo;
    private final ModelRepo modelRepo;
    private final OemRepo oemRepo;
    private final PartImageRepo partImageRepo;

    private final PartMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public PartFindAllSummariesResponse findAllSummaries() {
        return new PartFindAllSummariesResponse(mapper.toSummaries(partRepo.findAll()));
    }

    @Override
    @Transactional(readOnly = true)
    public PartFindAllResponse findAll() {
        return new PartFindAllResponse(mapper.toDtos(partRepo.findAll()));
    }

    @Override
    @Transactional(readOnly = true)
    public PartDto find(long id) {
        if(partRepo.existsById(id)) {
            return mapper.toDto(partRepo.findById(id).get());
        }
        else throw new EntityNotFoundException("Part with id " + id + " not found");
    }

    @Override
    @Transactional(readOnly = true)
    public PartFindAllByIdResponse findByIds(List<Long> ids) {
        return new PartFindAllByIdResponse(mapper.toDtos(partRepo.findAllById(ids)));
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
        var part = mapper.toEntity(request);
        part.setId(id);

        if(request.getOemNumber() != null) {
            part.setOemNumber(oemRepo.findByNumber(request.getOemNumber()));
        }

        return mapper.toDto(save(part));
    }

    protected Part save(Part part) {
        log.info("Saving part: {}", part);

        if(modelRepo.findAllById(part.getModels().stream().map(Model::getId).toList()).size() != part.getModels().size()) {
            throw new EntityNotFoundException("Some of the parts models don't exist anymore");
        }

        if(part.getOemNumber() != null) {
            if(part.getOemNumber().getId() != null) {
                part.setOemNumber(oemRepo.findByNumber(part.getOemNumber().getNumber()));
            }
            else {
                oemRepo.save(part.getOemNumber());
            }
        }

        var dbPart = partRepo.save(part);

        if(!part.getImages().isEmpty()) {
            for(var image : part.getImages()) {
                image.setPart(dbPart);
            }

            partImageRepo.saveAll(part.getImages());
        }

        log.info("Successfully saved part with id {}", dbPart.getId());
        return dbPart;
    }

    @Override
    public void delete(long id) {
        log.info("Deleting part with id {}", id);

        if(!partRepo.existsById(id)) {
            throw new EntityNotFoundException("Could not find part with id: " + id + " to delete");
        }

        partRepo.deleteById(id);

        log.info("Successfully deleted part with id {}", id);
    }
}
