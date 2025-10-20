package dev.ioannis.anemosparts.domain.requests;

import dev.ioannis.anemosparts.domain.PartImageDto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
@NoArgsConstructor
public class PartSaveRequest {
    @NotBlank
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters long")
    private String name;
    @NotBlank
    @Size(min = 10, max = 500, message = "The description must be between 10 and 500 words long")
    private String description;
    @NotBlank
    private String oemNumber;
    @NotBlank
    private String partNumber;
    @DecimalMin("0.25")
    @DecimalMax("1000")
    private double price;
    @Range(min = 0, max = 100)
    private int quantity = 0;

    private List<PartImageRequest> images;

    @Size(min = 1, max = 20)
    private List<Long> modelIds;
}
