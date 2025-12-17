package com.puchkina.restaurant_rating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puchkina.restaurant_rating.dto.visitor.VisitorRequestDto;
import com.puchkina.restaurant_rating.dto.visitor.VisitorResponseDto;
import com.puchkina.restaurant_rating.entity.Visitor;
import com.puchkina.restaurant_rating.mapper.VisitorMapper;
import com.puchkina.restaurant_rating.service.VisitorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitorController.class)
class VisitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VisitorService visitorService;

    @MockBean
    private VisitorMapper visitorMapper;

    @Test
    void getAll_returnsList() throws Exception {
        Visitor v1 = new Visitor();
        v1.setId(1L);
        Visitor v2 = new Visitor();
        v2.setId(2L);

        when(visitorService.findAll()).thenReturn(List.of(v1, v2));
        when(visitorMapper.toResponseDto(v1)).thenReturn(new VisitorResponseDto(1L, "Ann", 20, "F"));
        when(visitorMapper.toResponseDto(v2)).thenReturn(new VisitorResponseDto(2L, "Bob", 21, "M"));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(visitorService).findAll();
    }

    @Test
    void create_returnsCreatedDto() throws Exception {
        VisitorRequestDto req = new VisitorRequestDto("Ann", 20, "F");

        when(visitorMapper.toEntity(any(VisitorRequestDto.class), anyLong()))
                .thenAnswer(inv -> {
                    VisitorRequestDto dto = inv.getArgument(0);
                    Long id = inv.getArgument(1);
                    Visitor v = new Visitor();
                    v.setId(id);
                    v.setName(dto.name());
                    v.setAge(dto.age());
                    v.setGender(dto.gender());
                    return v;
                });

        when(visitorMapper.toResponseDto(any(Visitor.class)))
                .thenAnswer(inv -> {
                    Visitor v = inv.getArgument(0);
                    return new VisitorResponseDto(v.getId(), v.getName(), v.getAge(), v.getGender());
                });

        when(visitorService.save(any(Visitor.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.name").value("Ann"));

        verify(visitorService).save(any(Visitor.class));
    }

    @Test
    void update_replacesExistingAndReturnsDto() throws Exception {
        long id = 5L;
        Visitor existing = new Visitor();
        existing.setId(id);

        when(visitorService.findAll()).thenReturn(List.of(existing));

        VisitorRequestDto req = new VisitorRequestDto("NewName", 30, "F");
        Visitor updated = new Visitor();
        updated.setId(id);
        updated.setName(req.name());
        updated.setAge(req.age());
        updated.setGender(req.gender());

        when(visitorMapper.toEntity(any(VisitorRequestDto.class), eq(id))).thenReturn(updated);
        when(visitorMapper.toResponseDto(updated))
                .thenReturn(new VisitorResponseDto(id, "NewName", 30, "F"));

        when(visitorService.save(updated)).thenReturn(updated);

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("NewName"));

        verify(visitorService).remove(existing);
        verify(visitorService).save(updated);
    }

    @Test
    void delete_removesExisting() throws Exception {
        long id = 7L;
        Visitor existing = new Visitor();
        existing.setId(id);
        when(visitorService.findAll()).thenReturn(List.of(existing));

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isOk());

        verify(visitorService).remove(existing);
    }
}
