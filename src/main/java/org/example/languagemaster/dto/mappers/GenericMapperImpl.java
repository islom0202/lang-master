package org.example.languagemaster.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.example.languagemaster.dto.mappers.GenericMapper;
import org.modelmapper.ModelMapper;

import java.util.List;
@RequiredArgsConstructor
public class GenericMapperImpl<E, D> implements GenericMapper<E, D> {
  private final ModelMapper mapper;
  private final Class<E> entityClass;
  private final Class<D> dtoClass;

  @Override
  public D toDto(E entity) {
    return mapper.map(entity, dtoClass);
  }

  @Override
  public E toEntity(D dto) {
    return mapper.map(dto, entityClass);
  }

  @Override
  public List<D> toDtoList(List<E> entities) {
    return entities
            .stream()
            .map(this::toDto)
            .toList();
  }

  @Override
  public List<E> toEntityList(List<D> dtos) {
    return dtos
            .stream()
            .map(this::toEntity)
            .toList();
  }
}
