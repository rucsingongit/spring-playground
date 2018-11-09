package com.galvanize.playground.springplayground.repository;

import com.galvanize.playground.springplayground.entity.Lesson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface LessonsRepository extends CrudRepository<Lesson, Long> {

    Iterable<Lesson> findLessonsByDeliveredOnBetween(Date d1, Date d2);

    Iterable<Lesson> findLessonsByDeliveredOn(Date date);

//    @Modifying
//    @Transactional
//    @Query("update Lesson t set t.title = :title where t.id = :id")
//    void updateTitleById(@Param("title") String title, @Param("id") Long id);

}
