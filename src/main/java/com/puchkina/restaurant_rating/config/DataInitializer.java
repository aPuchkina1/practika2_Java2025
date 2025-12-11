package com.puchkina.restaurant_rating.config;

import com.puchkina.restaurant_rating.entity.CuisineType;
import com.puchkina.restaurant_rating.entity.Restaurant;
import com.puchkina.restaurant_rating.entity.Review;
import com.puchkina.restaurant_rating.entity.Visitor;
import com.puchkina.restaurant_rating.service.RestaurantService;
import com.puchkina.restaurant_rating.service.ReviewService;
import com.puchkina.restaurant_rating.service.VisitorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @PostConstruct
    public void initData() {
        System.out.println("\nТЕСТ: DataInitializer");

        Visitor v1 = new Visitor();
        v1.setName("Оксана");
        v1.setAge(25);
        v1.setGender("F");

        Visitor v2 = new Visitor();
        v2.setName(null);
        v2.setAge(30);
        v2.setGender("F");

        Visitor v3 = new Visitor();
        v3.setName("Максим");
        v3.setAge(19);
        v3.setGender("M");

        v1 = visitorService.save(v1);
        v2 = visitorService.save(v2);
        v3 = visitorService.save(v3);

        Restaurant r1 = new Restaurant();
        r1.setName("MAMMAMIA!");
        r1.setDescription("Cause I'm ITALIANO");
        r1.setCuisineType(CuisineType.ИТАЛЬЯНСКАЯ);
        r1.setAverageCheck(new BigDecimal("1200"));
        r1.setRating(BigDecimal.ZERO);

        Restaurant r2 = new Restaurant();
        r2.setName("ChinaTown");
        r2.setDescription("ПО лапшичке?");
        r2.setCuisineType(CuisineType.КИТАЙСКАЯ);
        r2.setAverageCheck(new BigDecimal("900"));
        r2.setRating(BigDecimal.ZERO);

        r1 = restaurantService.save(r1);
        r2 = restaurantService.save(r2);

        Review rev1 = new Review();
        rev1.setScore(5);
        rev1.setText("Отлично!");
        rev1.setRestaurant(r1);
        rev1.setVisitor(v1);
        reviewService.save(rev1);

        Review rev2 = new Review();
        rev2.setScore(4);
        rev2.setText("Хорошо, но шумно");
        rev2.setRestaurant(r1);
        rev2.setVisitor(v2);
        reviewService.save(rev2);

        Review rev3 = new Review();
        rev3.setScore(3);
        rev3.setText("Лапша супер! Персонал неприятный!");
        rev3.setRestaurant(r2);
        rev3.setVisitor(v3);
        reviewService.save(rev3);

        System.out.println("Посетители: " + visitorService.findAll());
        System.out.println("Рестораны: " + restaurantService.findAll());
        System.out.println("Оценки: " + reviewService.findAll());

        System.out.println("КОНЕЦ ТЕСТА: DataInitializer\n");
    }
}