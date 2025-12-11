package dev.ioannis.anemosparts.daemons;

import dev.ioannis.anemosparts.domain.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String customerEmail;
    private AddressDto address;
    private List<Long> partIds;
    private List<Integer> quantities;
}
