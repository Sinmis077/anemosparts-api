package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.domain.responses.PartFindAllResponse;
import org.springframework.stereotype.Service;

@Service
public interface PartService {
    PartFindAllResponse findAll();

    PartDto save(PartSaveRequest request);

    PartDto update(Long id, PartSaveRequest request);

    void delete(long partId);
}
