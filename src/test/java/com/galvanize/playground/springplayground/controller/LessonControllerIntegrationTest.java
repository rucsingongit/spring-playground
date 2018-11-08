package com.galvanize.playground.springplayground.controller;

import com.galvanize.playground.springplayground.entity.Lesson;
import com.galvanize.playground.springplayground.repository.LessonsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LessonControllerIntegrationTest {


    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    LessonsRepository repository;

    private String[] titles = {"First title", "Second title", "Third title", "Fourth title", "Fifth title"};
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");


    @Before
    public void setUp() throws Exception {

        repository.deleteAll();
        repository.saveAll(Arrays.asList(
                new Lesson().setTitle(titles[0]).setDeliverOn(new Date(df.parse("2016-06-11").getTime())),
                new Lesson().setTitle(titles[1]).setDeliverOn(new Date(df.parse("2016-07-11").getTime())),
                new Lesson().setTitle(titles[2]).setDeliverOn(new Date(df.parse("2016-08-11").getTime())),
                new Lesson().setTitle(titles[3]).setDeliverOn(new Date(df.parse("2016-09-11").getTime())),
                new Lesson().setTitle(titles[4]).setDeliverOn(new Date(df.parse("2016-10-11").getTime()))
        ));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAllLessonTest() throws Exception {
        ResponseEntity<List<Lesson>> responseEntity = restTemplate.exchange("/api/lessons/",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Lesson>>() {
                });

        assertThat(responseEntity.getStatusCode().value(), is(HttpStatus.OK.value()));
        List<Lesson> lessons = responseEntity.getBody();
        assertNotNull(lessons);
        assertThat(lessons.size(), is(5));
        List<String> actualTitles = lessons.stream()
                .map(Lesson::getTitle)
                .collect(Collectors.toList());
        assertThat(actualTitles, containsInAnyOrder(titles));
    }

    @Test
    public void getAllBetweenDateTest() throws Exception {
        Date dateFrom = new Date(df.parse("2016-07-01").getTime());
        Date dateTo = new Date(df.parse("2016-09-30").getTime());

        String url = String.format("/api/lessons/between/%s/%s",dateFrom.toString(),dateTo.toString());
        ResponseEntity<List<Lesson>> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Lesson>>() {
                });
        assertThat(responseEntity.getStatusCode().value(), is(HttpStatus.OK.value()));
        List<Lesson> lessons = responseEntity.getBody();
        assertNotNull(lessons);
        assertThat(lessons.size(), is(3));
        List<String> actualTitles = lessons.stream()
                .map(Lesson::getTitle)
                .collect(Collectors.toList());
        assertThat(actualTitles, containsInAnyOrder(titles[1],titles[2],titles[3]));
        assertTrue(lessons.stream().anyMatch(e -> dateFrom.compareTo(e.getDeliverOn()) < 1));
        assertTrue(lessons.stream().anyMatch(e -> dateTo.compareTo(e.getDeliverOn()) > -1));
    }

    @Test
    public void createNewLessonTest() throws Exception {
        Lesson addedLesson = new Lesson().setTitle("New Title")
                .setDeliverOn(new Date(df.parse("2016-11-11").getTime()));
        HttpEntity<Lesson> httpEntity = new HttpEntity<>(
                addedLesson);
        ResponseEntity<Lesson> responseEntity =
                restTemplate.exchange("/api/lesson/", HttpMethod.POST,
                        httpEntity, new ParameterizedTypeReference<Lesson>() {
                        });
        assertThat(responseEntity.getStatusCode().value(), is(HttpStatus.CREATED.value()));
        Lesson lesson = responseEntity.getBody();
        assertNotNull(lesson);
        assertNotNull(lesson.getId());
        assertEquals(lesson.getTitle(), addedLesson.getTitle());
    }

    @Test
    public void updateTitleByIdTest() throws Exception {
        Lesson addedLesson = new Lesson().setTitle("New Title")
                .setDeliverOn(new Date(df.parse("2016-11-11").getTime()));
        HttpEntity<Lesson> httpEntity = new HttpEntity<>(
                addedLesson);
        String editedTitle = "Edited Title";
        Long editedId = repository.findAll().iterator().next().getId();
        String url = String.format("/api/lesson/%d/title/%s", editedId, editedTitle);
//        https://github.com/spring-projects/spring-boot/issues/7742
//        https://github.com/spring-projects/spring-boot/issues/7412
        RestTemplate patchRestTemplate = restTemplate.getRestTemplate();
        ResponseEntity<Lesson> responseEntity =
                patchRestTemplate.exchange(url, HttpMethod.PATCH,
                        httpEntity, new ParameterizedTypeReference<Lesson>() {});

        assertThat(responseEntity.getStatusCode().value(), is(HttpStatus.OK.value()));
        Lesson lesson = responseEntity.getBody();
        assertNotNull(lesson);
        assertThat(lesson.getTitle(), is(editedTitle));
        assertThat(lesson.getId(), is(editedId));
    }

}