package com.galvanize.playground.springplayground.controller;

import com.galvanize.playground.springplayground.entity.Lesson;
import com.galvanize.playground.springplayground.repository.LessonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LessonController {

    private static final String NOT_FOUND_FORMAT = "Lesson with id: %d wasn't found!";

    private final LessonsRepository repository;

    @Autowired
    public LessonController(LessonsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/lessons")
    public Iterable<Lesson> getAll() {
        return repository.findAll();
    }

    @GetMapping("/lessons/date/{date}")
    public Iterable<Lesson> getAllByDate(@PathVariable Date date){
        return repository.findLessonsByDeliveredOn(date);
    }

    @GetMapping("/lesson/{id}")
    public Lesson getLessonById(@PathVariable Long id) {
        Optional<Lesson> byId = repository.findById(id);
        if (!byId.isPresent()) {
            throw new LessonNotFoundException(String.format(NOT_FOUND_FORMAT, id));
        }
        return byId.get();
    }

    @PostMapping("/lesson")
    @ResponseStatus(HttpStatus.CREATED)
    public Lesson create(@RequestBody Lesson lesson) {
        return repository.save(lesson);
    }

    @PutMapping(value = "/lesson/{id}")
    public Lesson updateById(@RequestBody Lesson bodyLesson, @PathVariable Long id) {
        Optional<Lesson> byId = repository.findById(id);
        if (!byId.isPresent())
            throw new LessonNotFoundException(String.format(NOT_FOUND_FORMAT, id));
        Lesson lesson = byId.get();
        lesson.setTitle(bodyLesson.getTitle());
        lesson.setDeliveredOn(bodyLesson.getDeliveredOn());
        return repository.save(lesson);
    }

    @DeleteMapping("/lesson/{id}")
    public void deleteById(@PathVariable Long id){
        if (!repository.existsById(id))
            throw new LessonNotFoundException(String.format(NOT_FOUND_FORMAT, id));
        repository.deleteById(id);
    }

    @GetMapping("/lessons/date-between/{d1}/{d2}")
    public Iterable<Lesson> getAllBetween(@PathVariable Date d1, @PathVariable Date d2) {
        return repository.findLessonsByDeliveredOnBetween(d1,d2);
    }

    @PatchMapping("/lesson/{id}/title/{title}")
    public Lesson updateTitleById(@PathVariable Long id, @PathVariable String title) {
        Optional<Lesson> byId = repository.findById(id);
        if (!byId.isPresent())
            throw new LessonNotFoundException(String.format(NOT_FOUND_FORMAT, id));
        Lesson lesson = byId.get();
        lesson.setTitle(title);
        return repository.save(lesson);
    }

}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class LessonNotFoundException extends RuntimeException {
    LessonNotFoundException(String message) {
        super(message);
    }
}