package com.galvanize.playground.springplayground.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.playground.springplayground.entity.Lesson;
import com.galvanize.playground.springplayground.repository.LessonsRepository;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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
    public void getLessonsTest() throws Exception {
        when(repository.findAll()).thenReturn(new ArrayList<Lesson>());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/lessons")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    public void getLessonByCorrectIdTest() throws Exception {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(new Lesson()));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/lesson/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(repository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    public void getLessonByWrongIdTest() throws Exception {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(
                "/api/lesson/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        verify(repository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    public void deleteLessonByWrongIdTest() throws Exception {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/lesson/{id}", anyLong()))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(repository, times(1)).existsById(anyLong());
        verify(repository, times(0)).deleteById(anyLong());
    }

    @Test
    public void deleteLessonByCorrectIdTest() throws Exception {
        doNothing().when(repository).deleteById(anyLong());
        when(repository.existsById(anyLong())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/lesson/{id}", anyLong()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(repository, times(1)).existsById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    public void findLessonsByDeliveredOnTest() throws Exception {
        Date mockDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2010-10-10").getTime());

        when(repository.findLessonsByDeliveredOn(mockDate)).thenReturn(new ArrayList<Lesson>());

        mockMvc.perform(MockMvcRequestBuilders.get(
                "/api/lessons/date/{date}", mockDate))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(repository, Mockito.times(1)).findLessonsByDeliveredOn(mockDate);
    }

    @Test
    public void updateLessonByCorrectIdTest() throws Exception {
        Lesson lessonMock = new Lesson();
        when(repository.findById(anyLong())).thenReturn(Optional.of(lessonMock));
        when(repository.save(lessonMock)).thenReturn(lessonMock);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/lesson/{id}", anyLong())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsBytes(lessonMock))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(lessonMock);
    }

    @Test
    public void updateLessonByWrongIdTest() throws Exception {
        Lesson lessonMock = new Lesson();
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        when(repository.save(lessonMock)).thenReturn(lessonMock);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/lesson/{id}", anyLong())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsBytes(lessonMock))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).save(lessonMock);
    }

    @Test
    public void createLessonTest() throws Exception {
        Lesson lessonMock = new Lesson();
        when(repository.save(lessonMock)).thenReturn(lessonMock);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/lesson")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsBytes(lessonMock))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(repository, times(1)).save(lessonMock);
    }

    @Test
    public void findLessonsBetweenDeliveredOnTest() throws Exception {
        Date mockDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2010-10-10").getTime());

        when(repository.findLessonsByDeliveredOnBetween(mockDate, mockDate)).thenReturn(new ArrayList<Lesson>());

        mockMvc.perform(MockMvcRequestBuilders.get(
                "/api/lessons/date-between/{d1}/{d2}", mockDate, mockDate))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(repository, Mockito.times(1)).findLessonsByDeliveredOnBetween(mockDate, mockDate);
    }

    @Test
    public void updateTitleByCorrectIdTest() throws Exception {
        Lesson lessonMock = new Lesson();
        when(repository.findById(anyLong())).thenReturn(Optional.of(lessonMock));
        when(repository.save(lessonMock)).thenReturn(lessonMock);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/lesson/{id}/title/{title}", anyLong(), "any String")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(lessonMock);
    }

    @Test
    public void updateTitleByWrongIdTest() throws Exception {
        Lesson lessonMock = new Lesson();
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        when(repository.save(lessonMock)).thenReturn(lessonMock);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/lesson/{id}/title/{title}", anyLong(), "any String")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).save(lessonMock);
    }
}

