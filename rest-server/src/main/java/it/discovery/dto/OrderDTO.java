package it.discovery.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {
    private UUID id;

    private int bookId;

    private String title;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    private boolean completed;
}
