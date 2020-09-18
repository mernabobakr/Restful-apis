package com.kidzona.parentsservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Entity
@Table(name = "kids")
public class Kid {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "school_address")
    @NotNull
    private String schoolAddress;

    @Column(name = "picture_url")
    @NotNull
    private String pictureUrl;

    @Column(name = "birthday")
    @NotNull
    @Past
    private Date birthday;

    @Column(name = "status")
    private int status;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @JsonIgnore
    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parentId) {
        this.parent = parentId;
    }
}
