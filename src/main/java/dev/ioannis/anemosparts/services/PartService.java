package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.domain.responses.PartFindAllByIdResponse;
import dev.ioannis.anemosparts.domain.responses.PartFindAllResponse;
import dev.ioannis.anemosparts.domain.responses.PartFindAllSummariesResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PartService {
    PartFindAllSummariesResponse findAllSummaries();

    PartFindAllResponse findAll();

    PartDto find(long id);

    PartFindAllByIdResponse findByIds(@NotNull @Size(min = 1) List<Long> ids);

    PartDto save(PartSaveRequest request);

    PartDto update(Long id, PartSaveRequest request);

    void delete(long partId);

}
