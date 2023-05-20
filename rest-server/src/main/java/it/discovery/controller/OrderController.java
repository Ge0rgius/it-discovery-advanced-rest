package it.discovery.controller;

import it.discovery.dto.OrderDTO;
import it.discovery.model.Book;
import it.discovery.model.Order;
import it.discovery.repository.BookRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class OrderController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("books/orders")
    public ResponseEntity<List<OrderDTO>> search(@Valid PageCriteria criteria) {
        List<Order> orders = bookRepository.findAll().stream()
                .filter(Book::hasOrders)
                .flatMap(book -> book.getOrders().stream()).toList();

        int totalSize = orders.size();
        int nextOffset = (criteria.getPage() + 1) * criteria.getSize();
        List<Order> subList = (orders.subList(criteria.getPage() * criteria.getSize(),
                Math.min(nextOffset, totalSize)));
        return ResponseEntity.ok().header("X-TOTAL-COUNT", totalSize + "")
                .body(subList.stream().map(order -> modelMapper.map(order, OrderDTO.class)).toList());
    }

    @PostMapping("books/{bookId}/orders")
    public void create(@PathVariable int bookId) {
        bookRepository.findById(bookId).ifPresent(Book::addOrder);
    }

    @DeleteMapping("books/{bookId}/orders/{orderId}")
    public void delete(@PathVariable int bookId, @PathVariable String orderId) {
        bookRepository.findById(bookId).ifPresent(book -> book.cancelOrder(orderId));
    }

    //TODO complete order, pay order, delivery order
}
