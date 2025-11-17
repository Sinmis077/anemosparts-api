package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.domain.responses.PartFindAllResponse;
import dev.ioannis.anemosparts.services.PartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parts")
@AllArgsConstructor
public class PartController {

    private final PartService partService;

    @GetMapping
    public ResponseEntity<PartFindAllResponse> findAll() {
        return ResponseEntity.ok(partService.findAll());
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
