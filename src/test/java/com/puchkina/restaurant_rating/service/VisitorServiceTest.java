package com.puchkina.restaurant_rating.service;

import com.puchkina.restaurant_rating.entity.Visitor;
import com.puchkina.restaurant_rating.repository.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorServiceTest {

    @Mock
    private VisitorRepository visitorRepository;

    @InjectMocks
    private VisitorService visitorService;

    @Test
    void save_delegatesToRepository() {
        Visitor visitor = new Visitor();
        visitor.setId(1L);

        when(visitorRepository.save(visitor)).thenReturn(visitor);

        Visitor result = visitorService.save(visitor);

        assertSame(visitor, result);
        verify(visitorRepository).save(visitor);
        verifyNoMoreInteractions(visitorRepository);
    }

    @Test
    void remove_delegatesToRepository() {
        Visitor visitor = new Visitor();
        visitor.setId(1L);

        visitorService.remove(visitor);

        verify(visitorRepository).delete(visitor);
        verifyNoMoreInteractions(visitorRepository);
    }

    @Test
    void findAll_returnsRepositoryResult() {
        List<Visitor> expected = List.of(new Visitor(), new Visitor());
        when(visitorRepository.findAll()).thenReturn(expected);

        List<Visitor> result = visitorService.findAll();

        assertSame(expected, result);
        verify(visitorRepository).findAll();
        verifyNoMoreInteractions(visitorRepository);
    }

    @Test
    void findById_whenExists_returnsEntity() {
        Visitor visitor = new Visitor();
        visitor.setId(5L);
        when(visitorRepository.findById(5L)).thenReturn(Optional.of(visitor));

        Visitor result = visitorService.findById(5L);

        assertSame(visitor, result);
        verify(visitorRepository).findById(5L);
        verifyNoMoreInteractions(visitorRepository);
    }

    @Test
    void findById_whenMissing_returnsNull() {
        when(visitorRepository.findById(999L)).thenReturn(Optional.empty());

        Visitor result = visitorService.findById(999L);

        assertNull(result);
        verify(visitorRepository).findById(999L);
        verifyNoMoreInteractions(visitorRepository);
    }
}
