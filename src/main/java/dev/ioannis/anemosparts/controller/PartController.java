package dev.ioannis.anemosparts.controller;

import dev.ioannis.anemosparts.business.PartService;
import dev.ioannis.anemosparts.domain.Part;
import dev.ioannis.anemosparts.domain.request.NewPartRequest;
import dev.ioannis.anemosparts.mapper.PartMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parts")
@AllArgsConstructor
public class PartController {
    private final PartService partService;

    @GetMapping
    public List<Part> getAllParts() {
        return partService.findAll();
    }

    @GetMapping("/search")
    public Part getPartById(@RequestParam long id) {
        return partService.findById(id)
                .orElseThrow(() -> new RuntimeException("No Part with id " + id));
    }

    @PostMapping
    public ResponseEntity<Part> createPart(@RequestBody @Valid NewPartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                        .body(partService.save(PartMapper.INSTANCE.toModel(request)));
    }
}
