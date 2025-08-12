package org.example.languagemaster.dto.mappers;

import java.util.List;

public interface GenericMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
    List<D> toDtoList(List<E> entities);
    List<E> toEntityList(List<D> dtos);
}