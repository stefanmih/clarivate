package com.clarivate.entity;

import com.clarivate.collection.Tuple;
import jakarta.persistence.*;

@Table(name = "RECORD")
@Entity
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "HOLIDAY")
    private String holiday;

    @Column(name = "DATE")
    private String date;

    public Record(){}

    public Record(String holiday, String date) {
        this.holiday = holiday;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Tuple getTuple() throws Exception {
        return Tuple.tupleOf(String.class, String.class).createTuple(holiday, date);
    }
}
