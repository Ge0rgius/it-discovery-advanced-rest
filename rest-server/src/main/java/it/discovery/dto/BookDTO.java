package it.discovery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.discovery.validator.TitleConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Book element")
public class BookDTO {
    @PositiveOrZero
    @Schema(description = "Unique book identifier")
    private int id;

    @NotEmpty
    @Schema(description = "Book author")
    private String author;

    @TitleConstraint
    private String title;

    @Min(2000)
    private int year;

    private int amount;

    private List<OrderDTO> orders;
}
