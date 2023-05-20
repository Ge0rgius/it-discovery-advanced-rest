package it.discovery.actuator.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import it.discovery.event.BookAddedEvent;
import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class MetricManagement {

    private final MeterRegistry meterRegistry;

    private final BookRepository bookRepository;

    private final AtomicLong orderCount = new AtomicLong();

    private Counter booksAdded;

    @PostConstruct
    void init() {
        booksAdded = meterRegistry.counter("books.added");
        meterRegistry.gauge("order.total", orderCount);
    }

    @Scheduled(fixedDelay = 1000)
    public void updateMetrics() {
        orderCount.set(bookRepository.findAll().stream().filter(Book::hasOrders)
                .mapToLong(book -> book.getOrders().size()).sum());
    }

    @EventListener
    public void onBookAdded(BookAddedEvent event) {
        booksAdded.increment();
    }
}
