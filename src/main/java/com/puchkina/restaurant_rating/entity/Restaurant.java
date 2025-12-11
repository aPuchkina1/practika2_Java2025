package com.puchkina.restaurant_rating.entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(exclude = "reviews")
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private CuisineType cuisineType;

    private BigDecimal averageCheck;
    private BigDecimal rating;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}