package com.galvanize.playground.springplayground.controller;

import com.galvanize.playground.springplayground.entity.Lesson;
import com.galvanize.playground.springplayground.repository.LessonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Optional;

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
//        throw new LessonNotFoundException("test");
        return repository.findAll();
    }

    @PostMapping("/lesson")
    @ResponseStatus(HttpStatus.CREATED)
    public Lesson create(@RequestBody Lesson lesson) {
        return repository.save(lesson);
    }

    @PutMapping(value = "/lesson/{id}") // api/
    public Lesson updateById(@RequestBody Lesson bodyLesson, @PathVariable Long id) {
        Optional<Lesson> byId = repository.findById(id);
        if (!byId.isPresent()) {
            throw new LessonNotFoundException(String.format("Lesson with id: %d wasn't found!", id));
        }

        Lesson l = byId.get();
        bodyLesson.setId(l.getId());
        repository.save(bodyLesson);
        return bodyLesson;
    }

    @DeleteMapping("/lesson/{id}")
    public void deleteById(@PathVariable Long id){
        Optional<Lesson> byId = repository.findById(id);
        if (!byId.isPresent()) {
            throw new LessonNotFoundException(String.format("Lesson with id: %d wasn't found!", id));
        }
        repository.deleteById(id);
    }


    @GetMapping("/lessons/date/{date}")
    public Iterable<Lesson> getAllByDate(@PathVariable Date date){
        return repository.findLessonsByDeliveredOn(date);
    }

    @GetMapping("/lesson/{id}")
    public Lesson getLessonById(@PathVariable Long id) {
        Optional<Lesson> byId = repository.findById(id);
        if (!byId.isPresent()) {
            throw new LessonNotFoundException(String.format("Lesson with id: %d wasn't found", id));
        }
        return byId.get();
    }







    @GetMapping("/lessons/between/{d1}/{d2}")
    public Iterable<Lesson> getAllBetween(@PathVariable Date d1, @PathVariable Date d2) {
        return repository.findLessonsByDeliveredOnBetween(d1,d2);
    }


    @PatchMapping("/lesson/{id}/title/{title}")
    public Lesson updateTitle(@PathVariable Long id, @PathVariable String title) {
        repository.updateTitleById(title, id);
        return repository.findById(id).get();
    }


}
