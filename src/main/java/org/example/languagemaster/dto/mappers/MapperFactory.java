package org.example.languagemaster.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.example.languagemaster.dto.mappers.GenericMapper;
import org.example.languagemaster.dto.mappers.GenericMapperImpl;
import org.modelmapper.ModelMapper;

@RequiredArgsConstructor
public class MapperFactory {
    private final ModelMapper modelMapper;

    public <E, D> GenericMapper<E, D> getMapper(Class<E> entityClass, Class<D> dtoClass) {
        return new GenericMapperImpl<>(modelMapper, entityClass, dtoClass);
    }
}
