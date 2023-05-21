package it.discovery.version;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.discovery.model.Book;
import it.discovery.version.v1.BookV1DTO;
import it.discovery.version.v2.BookV2DTO;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class DefaultBookFactory implements BookFactory {

    private final ModelMapper modelMapper;

    private final Map<ApiVersion, Class<?>> mapping;

    public DefaultBookFactory(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        mapping = Map.of(ApiVersion.v1, BookV1DTO.class, ApiVersion.v2, BookV2DTO.class);
    }

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public Book untransform(byte[] data, ApiVersion version) {

        Class<?> clz = mapping.get(findVersion(version));
        try {
            Object dto = objectMapper.readValue(data, clz);
            return modelMapper.map(dto, Book.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ApiVersion findVersion(ApiVersion version) {
        return Arrays.stream(ApiVersion.values()).filter(version1 -> version1.match(version)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Version " + version + " not supported"));
    }

    @Override
    public Object transform(Book book, ApiVersion version) {
        Class<?> clz = mapping.get(findVersion(version));
        return modelMapper.map(book, clz);
    }
}
