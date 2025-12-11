package com.puchkina.restaurant_rating.mapper;

import com.puchkina.restaurant_rating.dto.visitor.VisitorRequestDto;
import com.puchkina.restaurant_rating.dto.visitor.VisitorResponseDto;
import com.puchkina.restaurant_rating.entity.Visitor;
import org.springframework.stereotype.Component;

@Component
public class VisitorMapper {

    public Visitor toEntity(VisitorRequestDto dto, Long id) {
        Visitor visitor = new Visitor();
        visitor.setId(id);
        visitor.setName(dto.name());
        visitor.setAge(dto.age());
        visitor.setGender(dto.gender());
        return visitor;
    }

    public Visitor toEntity(VisitorRequestDto dto) {
        Visitor visitor = new Visitor();
        visitor.setName(dto.name());
        visitor.setAge(dto.age());
        visitor.setGender(dto.gender());
        return visitor;
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