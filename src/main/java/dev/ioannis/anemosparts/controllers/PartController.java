package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.services.PartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @GetMapping
    public ResponseEntity<List<PartDto>> findAll() {
        return ResponseEntity.ok(partService.findAll());
    }

    @PostMapping
    public ResponseEntity<PartDto> createPart(@RequestBody @Valid PartSaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(partService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartDto> updatePart(
            @PathVariable @NotNull @Positive Long id,
            @RequestBody @Valid PartSaveRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(partService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePart(@PathVariable @NotNull @Positive Long id) {
        partService.delete(id);
        return  ResponseEntity.noContent().build();
    }
}
