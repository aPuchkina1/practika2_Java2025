package com.puchkina.restaurant_rating.repository;

import com.puchkina.restaurant_rating.entity.Restaurant;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}