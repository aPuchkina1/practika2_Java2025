package com.puchkina.restaurant_rating.mapper;

import com.puchkina.restaurant_rating.dto.visitor.VisitorRequestDto;
import com.puchkina.restaurant_rating.dto.visitor.VisitorResponseDto;
import com.puchkina.restaurant_rating.entity.Visitor;
import org.springframework.stereotype.Component;

@Component
public class VisitorMapper {

    public Visitor toEntity(VisitorRequestDto dto, Long id) {
        return new Visitor(
                id,
                dto.name(),
                dto.age(),
                dto.gender()
        );
    }

    public Visitor toEntity(VisitorRequestDto dto) {
        return new Visitor(
                null,
                dto.name(),
                dto.age(),
                dto.gender()
        );
    }

    public VisitorResponseDto toResponseDto(Visitor visitor) {
        return new VisitorResponseDto(
                visitor.getId(),
                visitor.getName(),
                visitor.getAge(),
                visitor.getGender()
        );
    }
}