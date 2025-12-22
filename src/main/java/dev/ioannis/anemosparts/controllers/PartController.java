package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.services.PartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/parts")
@AllArgsConstructor
public class PartController {

    private final PartService partService;

    @GetMapping
    public ResponseEntity<Record> find(@RequestHeader(value = "app-part-state", required = false) String partState,
                                       @RequestParam(required = false) List<Long> ids) {
        if(ids != null && !ids.isEmpty()) {
            return ResponseEntity.ok(partService.findByIds(ids));
        }
        if(partState.equals("summaries"))
        {
            return ResponseEntity.ok(partService.findAllSummaries());
        }
        else if(partState.equals("full")) {
            return ResponseEntity.ok(partService.findAll());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartDto> findById(@NotNull @PathVariable Long id) {
        return ResponseEntity.ok(partService.find(id));
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
