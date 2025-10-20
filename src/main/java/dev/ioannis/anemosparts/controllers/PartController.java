package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.services.PartService;
import jakarta.validation.Valid;
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
    public List<PartDto> findAll() {
        return partService.findAll();
    }

//    @GetMapping("/search")
//    public PartDto getPartById(@RequestParam long id) {
//        return partService.findById(id);
//    }

    @PostMapping
    public ResponseEntity<PartDto> createPart(@RequestBody @Valid PartSaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(partService.save(request));
    }
}
