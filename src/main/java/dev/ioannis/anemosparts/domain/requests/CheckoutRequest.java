package dev.ioannis.anemosparts.domain.requests;

import dev.ioannis.anemosparts.domain.AddressDto;
import dev.ioannis.anemosparts.domain.CartDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequest {
    private CheckoutAccount account;
    private AddressDto address;
    private CartDto cart;
}
