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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brands")
@AllArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<BrandFindAllResponse> findAll() {
        return ResponseEntity.ok(brandService.findAll());
    }

    @PostMapping
    public ResponseEntity<BrandDto> create(@RequestBody @Valid BrandSaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(brandService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandDto> update(
            @PathVariable @NotNull @Positive Long id,
            @RequestBody @Valid BrandSaveRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(brandService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Positive Long id) {
        brandService.delete(id);
        return  ResponseEntity.noContent().build();
    }
}
