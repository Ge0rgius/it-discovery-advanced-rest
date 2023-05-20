package it.discovery.dto;

import it.discovery.validator.TitleConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookDTO {
    @PositiveOrZero
    private int id;

    @NotEmpty
    private String author;

    @TitleConstraint
    private String title;

    @Min(2000)
    private int year;

    private int amount;

    private List<OrderDTO> orders;
}
