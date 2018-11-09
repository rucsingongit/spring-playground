package com.galvanize.playground.springplayground.controller;


import com.galvanize.playground.springplayground.entity.Lesson;
import com.galvanize.playground.springplayground.repository.LessonsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = LessonController.class, secure = false)
public class LessonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonsRepository repository;

    @Test
    public void getLessonByCorrectIdTest() throws Exception {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(new Lesson()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/lesson/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(repository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    public void getLessonByWrongIdTest() throws Exception {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/lesson/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();

        verify(repository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    public void deleteLessonByWrongIdTest() throws Exception {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
//        doThrow(new LessonNotFoundException(anyString())).when(repository).deleteById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/lesson/{id}", anyLong()))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).deleteById(anyLong());
    }

    @Test
    public void deleteLessonByCorrectIdTest() throws Exception {
        doNothing().when(repository).deleteById(anyLong());
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Lesson()));
//        doAnswer(new Answer<Void>() {
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable {
//                return null;
//            }
//        }).when(repository).deleteById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/lesson/{id}", anyLong()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
    }


    @Test
    public void findLessonsByDeliveredOnTest() throws Exception {
        // Setup
        Iterable<Lesson> objects = anyIterable();
        Mockito.when(
                repository.findLessonsByDeliveredOn(any(Date.class))).thenReturn(objects);


        // Execute
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/lessons/date/{date}", any(Date.class));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        verify(repository, Mockito.times(1)).findLessonsByDeliveredOn(any(Date.class));


    }

}

