package dev.ioannis.anemosparts.daemons;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String customerEmail;
    @Size(min = 1)
    private List<CartItem> items;
}
