package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.domain.responses.PartFindAllByIdResponse;
import dev.ioannis.anemosparts.domain.responses.PartFindAllResponse;
import dev.ioannis.anemosparts.domain.responses.PartFindAllSummariesResponse;
import dev.ioannis.anemosparts.services.PartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/parts")
@AllArgsConstructor
public class PartController {
    private final PartService partService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PartFindAllSummariesResponse findAllSummaries() {
        return partService.findAllSummaries();
    }

    @GetMapping("/full")
    @ResponseStatus(HttpStatus.OK)
    public PartFindAllResponse findAll() {
        return partService.findAll();
    }

    @GetMapping("/ids")
    @ResponseStatus(HttpStatus.OK)
    public PartFindAllByIdResponse findAllById(@RequestParam List<Long> ids) {
        return partService.findByIds(ids);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PartDto findById(@NotNull @PathVariable Long id) {
        return partService.find(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartDto create(@RequestBody @Valid PartSaveRequest request) {
        return partService.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PartDto update(
        @PathVariable @NotNull @Positive Long id,
        @RequestBody @Valid PartSaveRequest request
    ) {
        return partService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull @Positive Long id) {
        partService.delete(id);
    }
}
