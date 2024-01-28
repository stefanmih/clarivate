package com.clarivate.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DATE")
public class Date {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATE")
    private java.util.Date date;

    @Column(name = "RECORD_ID")
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECORD_ID",  insertable = false, updatable = false)
    private Record record;

    public Date(java.util.Date date) {
        this.date = date;
    }

    public Date() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

}
