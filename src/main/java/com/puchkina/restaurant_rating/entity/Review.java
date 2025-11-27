package com.puchkina.restaurant_rating.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    private Long visitorId;
    private Long restaurantId;
    private int score;
    private String text;
}