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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
@AllArgsConstructor
public class PartController {

    private final PartService partService;

    @GetMapping("/summaries")
    public ResponseEntity<PartFindAllSummariesResponse> findAllSummaries() {
        return ResponseEntity.ok(partService.findAllSummaries());
    }

    @GetMapping
    public ResponseEntity<PartFindAllResponse> findAll() {
        return ResponseEntity.ok(partService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartDto> findById(@NotNull @PathVariable Long id) {
        return ResponseEntity.ok(partService.find(id));
    }

    @GetMapping("/ids")
    public ResponseEntity<PartFindAllByIdResponse> findByIds(@NotNull @Size(min = 1) @RequestParam List<Long> ids) {
        return ResponseEntity.ok(partService.findByIds(ids));
    }

    @PostMapping
    public ResponseEntity<PartDto> create(
        @RequestBody @Valid PartSaveRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            partService.save(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartDto> update(
        @PathVariable @NotNull @Positive Long id,
        @RequestBody @Valid PartSaveRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            partService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable @NotNull @Positive Long id
    ) {
        partService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
