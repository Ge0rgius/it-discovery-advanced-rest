package it.discovery.config;

import it.discovery.version.BookFactory;
import it.discovery.version.DefaultBookFactory;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VersionConfiguration {

    @Bean
    BookFactory bookFactory(ModelMapper modelMapper) {
        return new DefaultBookFactory(modelMapper);
    }
}
