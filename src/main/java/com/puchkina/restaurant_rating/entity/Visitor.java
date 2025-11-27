package com.puchkina.restaurant_rating.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Visitor {

    @NotNull
    private Long id;
    private String name;
    @NotNull
    private int age;
    @NotNull
    private String gender;
}