package com.galvanize.playground.springplayground.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.playground.springplayground.entity.Lesson;
import com.galvanize.playground.springplayground.repository.LessonsRepository;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(value = LessonController.class, secure = false)
public class LessonControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    LessonsRepository repository;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        repository.deleteAll();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        repository.saveAll(Arrays.asList(
                new Lesson().setTitle("First Title").setDeliveredOn(new Date(df.parse("2016-10-10").getTime())),
                new Lesson().setTitle("Second Title").setDeliveredOn(new Date(df.parse("2016-11-10").getTime())),
                new Lesson().setTitle("Third Title").setDeliveredOn(new Date(df.parse("2016-10-10").getTime()))

        ));

    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void getAllTest() throws Exception {

        // Setup

        // Execute
        String response = mockMvc.perform(get("/api/lessons"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Lesson> result = mapper.readValue(response, new TypeReference<List<Lesson>>() {});

        // Assert
        assertThat("expected three lessons!",result.size(),is(3));
        List<String> actualTitles = result.stream()
                .map(Lesson::getTitle)
                .collect(Collectors.toList());
        assertThat(actualTitles, containsInAnyOrder("First Title","Second Title","Third Title"));

        // Teardown

    }

    @Test
    public void getLessonByIdTest() throws Exception {

        // Setup
        Long searchingId = repository.findAll().iterator().next().getId();

        // Execute
        String response = mockMvc.perform(get("/api/lesson/{id}",searchingId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response, is(notNullValue()));

        Lesson result = mapper.readValue(response, new TypeReference<Lesson>() {});
        // Assert

        assertThat(result.getId(), is(searchingId));
        // Teardown

    }


















//    @MockBean
//    private LessonController lessonController;
//    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//    Lesson mockLesson = new Lesson().setId(1L).setTitle("Title")
//            .setDeliveredOn(new Date(df.parse("2016-06-11").getTime()));
//    Date dateFrom = new Date(df.parse("2016-06-11").getTime());
//    Date dateTo = new Date(df.parse("2016-06-11").getTime());


//    public LessonControllerIntegrationTest() throws ParseException {
//    }

//    @Test
//    public void getAllLessonTest() throws Exception {
//        when(lessonController.getAll()).thenReturn(Arrays.asList(mockLesson));
//
//        RequestBuilder requestBuilder = get(
//                "/api/lessons").accept(
//                MediaType.APPLICATION_JSON);
//
//        MvcResult result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andReturn();
//
//        System.out.println(result.getResponse());
//        String expected = "[{id:1,title:\"Title\",deliveredOn:\"2016-06-11\"}]";
//
//        verify(lessonController, times(1)).getAll();
//
//        JSONAssert.assertEquals(expected, result.getResponse()
//                .getContentAsString(), false);
//    }
//
//    @Test
//    public void getAllBetweenDateTest() throws Exception {
//        when(lessonController.getAllBetween(dateFrom,dateTo)).thenReturn(Arrays.asList(mockLesson));
//
//        RequestBuilder requestBuilder = get(
//                "/api/lessons/between/{dateFrom}/{dateTo}", dateFrom, dateTo).accept(
//                MediaType.APPLICATION_JSON);
//
//        MvcResult result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andReturn();
//
//        System.out.println(result.getResponse());
//        String expected = "[{id:1,title:\"Title\",deliveredOn:\"2016-06-11\"}]";
//
//        JSONAssert.assertEquals(expected, result.getResponse()
//                .getContentAsString(), false);
//    }
//
//    @Test
//    public void create() throws Exception {
//
//        Lesson added = new Lesson().setId(1L).setTitle("Title")
//                .setDeliveredOn(new Date(df.parse("2016-06-11").getTime()));
//
//        Lesson newLesson = new Lesson().setTitle("Title").setDeliveredOn(new Date(df.parse("2016-06-11").getTime()));
//
//        String newLessonJson = "[{title:\"Title\",deliveredOn:\"2016-06-11\"}]";
//
//        when(lessonController.create(any(Lesson.class))).thenReturn(added);
//
//        mockMvc.perform(post("/api/lesson")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(newLessonJson)
//        )
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.title", is("Title")))
//                .andExpect(jsonPath("$.deliveredOn", is("2016-06-11")));
//
//        verify(lessonController, times(1)).create(newLesson);
//    }
//
//    @Test
//    public void updateTitle() throws Exception {
//    }

}