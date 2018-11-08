package com.galvanize.playground.springplayground.controller;

import com.galvanize.playground.springplayground.entity.Lesson;
import com.galvanize.playground.springplayground.repository.LessonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping("/api")
public class LessonController {

    private final LessonsRepository repository;


    @Autowired
    public LessonController(LessonsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/lessons")
    public Iterable<Lesson> getAll() {
        return repository.findAll();
    }

    @GetMapping("/lessons/between/{d1}/{d2}")
    public Iterable<Lesson> getAllBetween(@PathVariable Date d1, @PathVariable Date d2) {
        return repository.findLessonsByDeliverOnBetween(d1,d2);
    }

    @PostMapping("/lesson")
    @ResponseStatus(HttpStatus.CREATED)
    public Lesson create(@RequestBody Lesson lesson) {
        return repository.save(lesson);
    }

    @PatchMapping("/lesson/{id}/title/{title}")
    public Lesson updateTitle(@PathVariable Long id, @PathVariable String title) {
        System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111");
        repository.updateTitleById(title, id);
        return repository.findById(id).get();
    }


}
