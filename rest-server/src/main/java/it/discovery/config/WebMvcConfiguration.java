package it.discovery.config;

import it.discovery.dto.BookDTO;
import it.discovery.dto.OrderDTO;
import it.discovery.model.Book;
import it.discovery.model.Order;
import it.discovery.version.v1.BookV1DTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableScheduling
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private List<HandlerInterceptor> interceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        interceptors.forEach(registry::addInterceptor);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        TypeMap<Book, BookDTO> map = mapper.createTypeMap(Book.class, BookDTO.class);
        map.addMapping(Book::getName, BookDTO::setTitle);

        TypeMap<BookDTO, Book> map2 = mapper.createTypeMap(BookDTO.class, Book.class);
        map2.addMapping(BookDTO::getTitle, Book::setName);

        TypeMap<BookV1DTO, Book> map3 = mapper.createTypeMap(BookV1DTO.class, Book.class);
        map3.addMapping(BookV1DTO::getTitle, Book::setName);

        TypeMap<Book, BookV1DTO> map4 = mapper.createTypeMap(Book.class, BookV1DTO.class);
        map4.addMapping(Book::getName, BookV1DTO::setTitle);

        TypeMap<Order, OrderDTO> orderMap = mapper.createTypeMap(Order.class, OrderDTO.class);
        orderMap.addMapping(order -> order.getBook().getName(), OrderDTO::setTitle);

        return mapper;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", HandlerTypePredicate.forAnnotation(RestController.class));
    }

    @Bean
    ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
}
