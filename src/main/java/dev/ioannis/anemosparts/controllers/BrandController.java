package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.BrandDto;
import dev.ioannis.anemosparts.domain.requests.BrandSaveRequest;
import dev.ioannis.anemosparts.domain.responses.BrandFindAllResponse;
import dev.ioannis.anemosparts.services.BrandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brands")
@AllArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BrandFindAllResponse findAll() {
        return brandService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BrandDto create(@RequestBody @Valid BrandSaveRequest request) {
        return brandService.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BrandDto update(
            @PathVariable @NotNull @Positive Long id,
            @RequestBody @Valid BrandSaveRequest request) {
        return brandService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull @Positive Long id) {
        brandService.delete(id);
    }
}
