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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/models")
@AllArgsConstructor
public class ModelController {
    private final ModelService modelService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ModelFindAllResponse findAll() {
        return modelService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModelDto create(@RequestBody @Valid ModelSaveRequest request) {
        return modelService.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModelDto update(
            @PathVariable @NotNull @Positive Long id,
            @RequestBody @Valid ModelSaveRequest request) {
        return modelService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull @Positive Long id) {
        modelService.delete(id);
    }
}
