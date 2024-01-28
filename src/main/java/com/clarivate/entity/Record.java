package com.clarivate.entity;

import jakarta.persistence.*;

import java.util.List;

@Table(name = "RECORD")
@Entity
public class Record {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "HOLIDAY")
    private String holiday;

    @OneToMany(mappedBy = "record", fetch = FetchType.EAGER)
    private List<Date> date;

    public Record(){}

    public Record(String holiday, List<Date> date) {
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

    public List<Date> getDate() {
        return date;
    }

    public void setDate(List<Date> date) {
        this.date = date;
    }

}
