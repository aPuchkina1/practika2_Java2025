package com.puchkina.restaurant_rating.repository;

import com.puchkina.restaurant_rating.entity.Visitor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

}