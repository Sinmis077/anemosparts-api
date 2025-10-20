package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.mappers.PartMapper;
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

    private final PartMapper mapper;

    @Override
    public List<PartDto> findAll() {
        return mapper.toDtos(partRepo.findAll());
    }

    @Override
    @Transactional
    public PartDto save(PartSaveRequest request) {
        return mapper.toDto(partRepo.save(mapper.toEntity(request)));
    }

    @Override
    public void delete(long partId) {
        if (!partRepo.existsById(partId)) {
            throw new EntityNotFoundException("Part with id " + partId + " not found");
        }

        partRepo.deleteById(partId);
    }
}
