package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.mappers.PartMapper;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.repositories.OemRepo;
import dev.ioannis.anemosparts.repositories.PartRepo;
import dev.ioannis.anemosparts.services.PartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PartServiceImpl implements PartService {

    private final PartRepo partRepo;
    private final ModelRepo modelRepo;
    private final OemRepo oemRepo;

    private final PartMapper mapper;

    @Override
    public List<PartDto> findAll() {
        return mapper.toDtos(partRepo.findAll());
    }

    @Override
    @Transactional
    public PartDto save(PartSaveRequest request) {
        Part part = Part.builder()
                .id(request.getId())
                .name(request.getName())
                .partNumber(request.getPartNumber())
                .price(request.getPrice())
                .description(request.getDescription())
                .models(modelRepo.findAllById(request.getModelIds()))
                .build();

        if(oemRepo.existsByNumber(request.getOemNumber())) {
            part.setOemNumber(oemRepo.findByNumber(request.getOemNumber()));
        }

        return mapper.toDto(partRepo.save(part));
    }

    @Override
    public void delete(long partId) {
        if (!partRepo.existsById(partId)) {
            throw new EntityNotFoundException("Part with id " + partId + " not found");
        }

        partRepo.deleteById(partId);
    }
}
