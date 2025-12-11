package dev.ioannis.anemosparts.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    @Size(min = 1)
    @NotEmpty
    private Map<Long, Integer> items = new HashMap<>();
}
