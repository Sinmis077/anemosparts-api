package dev.ioannis.anemosparts.domain;


import dev.ioannis.anemosparts.daemons.AddressType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private String extras;
    @NotEmpty
    private String houseNumber;
    @NotEmpty
    private String street;
    @NotEmpty
    private String city;
    @NotEmpty
    private String state;
    @NotEmpty
    private String postalCode;
    @NotEmpty
    private String country;
}
