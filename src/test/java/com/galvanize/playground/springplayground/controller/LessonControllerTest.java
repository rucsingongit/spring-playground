package com.galvanize.playground.springplayground.controller;


import com.galvanize.playground.springplayground.entity.Lesson;
import com.galvanize.playground.springplayground.repository.LessonsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.swing.text.html.HTMLDocument;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    public void getLessonByIdTest() throws Exception {

        // Setup
        Mockito.when(
                repository.findById(anyLong())).thenReturn(
                        Optional.of(new Lesson()));


        // Execute
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/lesson/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        // Assert
        verify(repository, Mockito.times(1)).findById(anyLong());

        // Teardown

    }

    @Test
    public void deleteLessonByWrongIdTest() throws Exception {

        // Setup
        doThrow(new LessonNotFoundException("some text")).when(repository).deleteById(anyLong());

//        Mockito.when(
//                repository.deleteById(anyLong())).thenThrow();


        // Execute
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
                "/api/lesson/{id}", anyLong());

        ResultActions result = mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
        // Assert
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).deleteById(anyLong());

        // Teardown

    }

    @Test
    public void deleteLessonByCorrectIdTest() throws Exception {

        // Setup
        doNothing().when(repository).deleteById(anyLong());
//
//        doAnswer(new Answer<Void>() {
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable {
//                return null;
//            }
//        }).when(repository).deleteById(anyLong());

//        Mockito.when(
//                repository.deleteById(anyLong())).thenThrow();


        // Execute
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
                "/api/lesson/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON_UTF8);

        ResultActions result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
        // Assert
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());

        // Teardown

    }





    @Test
    public void findLessonsByDeliveredOnTest() throws Exception{
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

