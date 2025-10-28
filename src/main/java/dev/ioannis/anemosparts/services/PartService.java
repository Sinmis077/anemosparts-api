package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PartService {
    List<PartDto> findAll();

    PartDto save(PartSaveRequest request);

    PartDto update(Long id, PartSaveRequest request);

    void delete(long partId);
}
