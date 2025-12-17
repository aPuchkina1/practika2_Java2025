package com.puchkina.restaurant_rating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puchkina.restaurant_rating.dto.review.ReviewRequestDto;
import com.puchkina.restaurant_rating.dto.review.ReviewResponseDto;
import com.puchkina.restaurant_rating.entity.Restaurant;
import com.puchkina.restaurant_rating.entity.Review;
import com.puchkina.restaurant_rating.entity.Visitor;
import com.puchkina.restaurant_rating.mapper.ReviewMapper;
import com.puchkina.restaurant_rating.service.RestaurantService;
import com.puchkina.restaurant_rating.service.ReviewService;
import com.puchkina.restaurant_rating.service.VisitorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ReviewMapper reviewMapper;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private VisitorService visitorService;

    @Test
    void getAll_returnsList() throws Exception {
        Visitor v1 = new Visitor();
        v1.setId(1L);
        Restaurant rest1 = new Restaurant();
        rest1.setId(10L);

        Review r1 = new Review();
        r1.setVisitor(v1);
        r1.setRestaurant(rest1);
        r1.setScore(5);
        r1.setText("A");

        Visitor v2 = new Visitor();
        v2.setId(2L);
        Restaurant rest2 = new Restaurant();
        rest2.setId(11L);

        Review r2 = new Review();
        r2.setVisitor(v2);
        r2.setRestaurant(rest2);
        r2.setScore(3);
        r2.setText("B");

        when(reviewService.findAll()).thenReturn(List.of(r1, r2));
        when(reviewMapper.toResponseDto(r1)).thenReturn(new ReviewResponseDto(1L, 10L, 5, "A"));
        when(reviewMapper.toResponseDto(r2)).thenReturn(new ReviewResponseDto(2L, 11L, 3, "B"));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].visitorId").value(1))
                .andExpect(jsonPath("$[0].restaurantId").value(10))
                .andExpect(jsonPath("$[0].score").value(5))
                .andExpect(jsonPath("$[0].text").value("A"))
                .andExpect(jsonPath("$[1].visitorId").value(2))
                .andExpect(jsonPath("$[1].restaurantId").value(11))
                .andExpect(jsonPath("$[1].score").value(3))
                .andExpect(jsonPath("$[1].text").value("B"));

        verify(reviewService).findAll();
    }

    @Test
    void getOne_returnsDtoWhenFound() throws Exception {
        long visitorId = 1L;
        long restaurantId = 10L;

        Visitor visitor = new Visitor();
        visitor.setId(visitorId);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Review review = new Review();
        review.setVisitor(visitor);
        review.setRestaurant(restaurant);
        review.setScore(5);
        review.setText("Great");

        when(reviewService.findAll()).thenReturn(List.of(review));
        when(reviewMapper.toResponseDto(review)).thenReturn(new ReviewResponseDto(visitorId, restaurantId, 5, "Great"));

        mockMvc.perform(get("/api/reviews/{visitorId}/{restaurantId}", visitorId, restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitorId").value(1))
                .andExpect(jsonPath("$.restaurantId").value(10))
                .andExpect(jsonPath("$.score").value(5));
    }

    @Test
    void create_createsReviewAndReturnsDto() throws Exception {
        ReviewRequestDto req = new ReviewRequestDto(1L, 10L, 4, "Ok");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        Visitor visitor = new Visitor();
        visitor.setId(1L);

        when(restaurantService.findById(10L)).thenReturn(restaurant);
        when(visitorService.findById(1L)).thenReturn(visitor);

        Review entity = new Review();
        entity.setRestaurant(restaurant);
        entity.setVisitor(visitor);
        entity.setScore(4);
        entity.setText("Ok");

        when(reviewMapper.toEntity(req, restaurant, visitor)).thenReturn(entity);
        when(reviewService.save(entity)).thenReturn(entity);
        when(reviewMapper.toResponseDto(entity)).thenReturn(new ReviewResponseDto(1L, 10L, 4, "Ok"));

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitorId").value(1))
                .andExpect(jsonPath("$.restaurantId").value(10))
                .andExpect(jsonPath("$.score").value(4));

        verify(reviewService).save(entity);
    }

    @Test
    void update_replacesExistingAndReturnsDto() throws Exception {
        long visitorId = 1L;
        long restaurantId = 10L;

        // existing review
        Visitor oldV = new Visitor();
        oldV.setId(visitorId);
        Restaurant oldR = new Restaurant();
        oldR.setId(restaurantId);

        Review existing = new Review();
        existing.setVisitor(oldV);
        existing.setRestaurant(oldR);

        when(reviewService.findAll()).thenReturn(List.of(existing));

        ReviewRequestDto req = new ReviewRequestDto(visitorId, restaurantId, 2, "Bad");

        when(restaurantService.findById(restaurantId)).thenReturn(oldR);
        when(visitorService.findById(visitorId)).thenReturn(oldV);

        Review updated = new Review();
        updated.setVisitor(oldV);
        updated.setRestaurant(oldR);
        updated.setScore(2);
        updated.setText("Bad");

        when(reviewMapper.toEntity(req, oldR, oldV)).thenReturn(updated);
        when(reviewService.save(updated)).thenReturn(updated);
        when(reviewMapper.toResponseDto(updated)).thenReturn(new ReviewResponseDto(visitorId, restaurantId, 2, "Bad"));

        mockMvc.perform(put("/api/reviews/{visitorId}/{restaurantId}", visitorId, restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(2));

        verify(reviewService).remove(existing);
        verify(reviewService).save(updated);
    }

    @Test
    void delete_removesExisting() throws Exception {
        long visitorId = 1L;
        long restaurantId = 10L;

        Visitor visitor = new Visitor();
        visitor.setId(visitorId);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Review existing = new Review();
        existing.setVisitor(visitor);
        existing.setRestaurant(restaurant);

        when(reviewService.findAll()).thenReturn(List.of(existing));

        mockMvc.perform(delete("/api/reviews/{visitorId}/{restaurantId}", visitorId, restaurantId))
                .andExpect(status().isOk());

        verify(reviewService).remove(existing);
    }
}
