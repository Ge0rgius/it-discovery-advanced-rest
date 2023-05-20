package it.discovery.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString(exclude = "book")
public class Order {
    private UUID id;

    private Book book;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    private boolean completed;

    public void complete() {
        if (completed) {
            throw new IllegalStateException("Order " + id + " is already completed");
        }

        completed = true;
        completedAt = LocalDateTime.now();
    }

}
