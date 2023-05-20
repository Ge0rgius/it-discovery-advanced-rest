package it.discovery.actuator.indicator;

import it.discovery.repository.BookRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class BookShopHealthIndicator implements HealthIndicator {

    private final static int DEFAULT_BOOK_AMOUNT_THRESHOLD = 50;

    private final BookRepository bookRepository;

    private final int bookAmountThreshold;

    public BookShopHealthIndicator(BookRepository bookRepository, Environment env) {
        this.bookRepository = bookRepository;
        bookAmountThreshold = env.getProperty("book.amount.threshold", Integer.class, DEFAULT_BOOK_AMOUNT_THRESHOLD);
    }

    @Override
    public Health health() {
        try {
            int size = bookRepository.findAll().size();

            if (size > bookAmountThreshold) {
                return Health.up().withDetail("Book amount", size).build();
            } else if (size > 0) {
                return Health.down().withDetail("Book amount", size)
                        .withDetail("Min threshold", bookAmountThreshold).build();
            } else {
                return Health.outOfService().withDetail("Emergency reason", "Book shop is empty").build();
            }
        } catch (Exception ex) {
            return Health.unknown().withException(ex).build();
        }
    }
}
