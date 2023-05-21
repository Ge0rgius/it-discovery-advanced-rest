package it.discovery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.discovery.version.v1.BookV1DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Book element")
public class BookDTO extends BookV1DTO {
}
