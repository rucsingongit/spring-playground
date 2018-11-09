package com.galvanize.playground.springplayground.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@Data
@Accessors(chain = true)
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveredOn;

//    public Long getId() {
//        return id;
//    }
//
//    public Lesson setId(Long id) {
//        this.id = id;
//        return this;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public Lesson setTitle(String title) {
//        this.title = title;
//        return this;
//    }
//
//    public Date getDeliveredOn() {
//        return deliveredOn;
//    }
//
//    public Lesson setDeliveredOn(Date deliveredOn) {
//        this.deliveredOn = deliveredOn;
//        return this;
//    }
}
