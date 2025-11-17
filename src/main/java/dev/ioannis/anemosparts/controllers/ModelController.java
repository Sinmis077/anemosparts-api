package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.ModelDto;
import dev.ioannis.anemosparts.domain.requests.ModelSaveRequest;
import dev.ioannis.anemosparts.domain.responses.ModelFindAllResponse;
import dev.ioannis.anemosparts.services.ModelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/models")
@AllArgsConstructor
public class ModelController {
    private final ModelService modelService;

    @GetMapping
    public ResponseEntity<ModelFindAllResponse> findAll() {
        return ResponseEntity.ok(modelService.findAll());
    }

    @PostMapping
    public ResponseEntity<ModelDto> create(@RequestBody @Valid ModelSaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelDto> update(
            @PathVariable @NotNull @Positive Long id,
            @RequestBody @Valid ModelSaveRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(modelService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Positive Long id) {
        modelService.delete(id);
        return  ResponseEntity.noContent().build();
    }
}
