package com.puchkina.restaurant_rating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puchkina.restaurant_rating.dto.restaurant.RestaurantRequestDto;
import com.puchkina.restaurant_rating.dto.restaurant.RestaurantResponseDto;
import com.puchkina.restaurant_rating.entity.CuisineType;
import com.puchkina.restaurant_rating.entity.Restaurant;
import com.puchkina.restaurant_rating.mapper.RestaurantMapper;
import com.puchkina.restaurant_rating.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private RestaurantMapper restaurantMapper;

    @Test
    void getAll_returnsList() throws Exception {
        Restaurant r1 = new Restaurant();
        r1.setId(1L);
        Restaurant r2 = new Restaurant();
        r2.setId(2L);

        when(restaurantService.findAll()).thenReturn(List.of(r1, r2));
        when(restaurantMapper.toResponseDto(r1))
                .thenReturn(new RestaurantResponseDto(1L, "R1", "", "ITALIAN", new BigDecimal("100.00"), null));
        when(restaurantMapper.toResponseDto(r2))
                .thenReturn(new RestaurantResponseDto(2L, "R2", "", "JAPANESE", new BigDecimal("200.00"), null));

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(restaurantService).findAll();
    }

    @Test
    void create_returnsCreatedDto() throws Exception {
        RestaurantRequestDto req = new RestaurantRequestDto(
                "MyRest",
                "Nice",
                "ИТАЛЬЯНСКАЯ",
                new BigDecimal("150.00")
        );

        when(restaurantMapper.toEntity(any(RestaurantRequestDto.class), anyLong()))
                .thenAnswer(inv -> {
                    RestaurantRequestDto dto = inv.getArgument(0);
                    Long id = inv.getArgument(1);
                    Restaurant r = new Restaurant();
                    r.setId(id);
                    r.setName(dto.name());
                    r.setDescription(dto.description());
                    r.setCuisineType(CuisineType.valueOf(dto.cuisineType()));
                    r.setAverageCheck(dto.averageCheck());
                    return r;
                });

        when(restaurantMapper.toResponseDto(any(Restaurant.class)))
                .thenAnswer(inv -> {
                    Restaurant r = inv.getArgument(0);
                    return new RestaurantResponseDto(
                            r.getId(), r.getName(), r.getDescription(), r.getCuisineType().name(), r.getAverageCheck(), r.getRating()
                    );
                });

        when(restaurantService.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11)) // AtomicLong starts at 10
                .andExpect(jsonPath("$.name").value("MyRest"))
                .andExpect(jsonPath("$.rating").doesNotExist());

        verify(restaurantService).save(any(Restaurant.class));
    }

    @Test
    void update_replacesExistingAndReturnsDto() throws Exception {
        long id = 3L;
        Restaurant existing = new Restaurant();
        existing.setId(id);
        when(restaurantService.findAll()).thenReturn(List.of(existing));

        RestaurantRequestDto req = new RestaurantRequestDto(
                "Updated",
                "Desc",
                "ЯПОНСКАЯ",
                new BigDecimal("250.00")
        );

        Restaurant updated = new Restaurant();
        updated.setId(id);
        updated.setName(req.name());
        updated.setDescription(req.description());
        updated.setCuisineType(CuisineType.valueOf(req.cuisineType()));
        updated.setAverageCheck(req.averageCheck());

        when(restaurantMapper.toEntity(any(RestaurantRequestDto.class), eq(id))).thenReturn(updated);
        when(restaurantService.save(updated)).thenReturn(updated);
        when(restaurantMapper.toResponseDto(updated))
                .thenReturn(new RestaurantResponseDto(id, "Updated", "Desc", "ЯПОНСКАЯ", new BigDecimal("250.00"), null));

        mockMvc.perform(put("/api/restaurants/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Updated"));

        verify(restaurantService).remove(existing);
        verify(restaurantService).save(updated);
    }

    @Test
    void delete_removesExisting() throws Exception {
        long id = 4L;
        Restaurant existing = new Restaurant();
        existing.setId(id);
        when(restaurantService.findAll()).thenReturn(List.of(existing));

        mockMvc.perform(delete("/api/restaurants/{id}", id))
                .andExpect(status().isOk());

        verify(restaurantService).remove(existing);
    }
}
