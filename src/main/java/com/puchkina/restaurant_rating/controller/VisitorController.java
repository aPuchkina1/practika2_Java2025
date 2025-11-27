package com.puchkina.restaurant_rating.controller;

import com.puchkina.restaurant_rating.dto.visitor.VisitorRequestDto;
import com.puchkina.restaurant_rating.dto.visitor.VisitorResponseDto;
import com.puchkina.restaurant_rating.entity.Visitor;
import com.puchkina.restaurant_rating.mapper.VisitorMapper;
import com.puchkina.restaurant_rating.service.VisitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;
    private final VisitorMapper visitorMapper;

    private final AtomicLong idGenerator = new AtomicLong(100);

    @GetMapping
    public List<VisitorResponseDto> getAll() {
        return visitorService.findAll().stream()
                .map(visitorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public VisitorResponseDto create(@RequestBody @Valid VisitorRequestDto requestDto) {
        Long newId = idGenerator.incrementAndGet();
        Visitor visitor = visitorMapper.toEntity(requestDto, newId);
        visitorService.save(visitor);
        return visitorMapper.toResponseDto(visitor);
    }

    @PutMapping("/{id}")
    public VisitorResponseDto update(@PathVariable Long id,
                                     @RequestBody @Valid VisitorRequestDto requestDto) {
        Visitor updated = visitorMapper.toEntity(requestDto, id);

        visitorService.findAll().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .ifPresent(visitorService::remove);

        visitorService.save(updated);
        return visitorMapper.toResponseDto(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        visitorService.findAll().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .ifPresent(visitorService::remove);
    }
}