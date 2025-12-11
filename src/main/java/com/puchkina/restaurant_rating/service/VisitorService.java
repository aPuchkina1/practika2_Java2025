package com.puchkina.restaurant_rating.service;

import com.puchkina.restaurant_rating.entity.Visitor;
import com.puchkina.restaurant_rating.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;

    public Visitor save(Visitor visitor) {
        return visitorRepository.save(visitor);
    }

    public void remove(Visitor visitor) {
        visitorRepository.delete(visitor);
    }

    public List<Visitor> findAll() {
        return visitorRepository.findAll();
    }

    public Visitor findById(Long id) {
        return visitorRepository.findById(id).orElse(null);
    }
}
