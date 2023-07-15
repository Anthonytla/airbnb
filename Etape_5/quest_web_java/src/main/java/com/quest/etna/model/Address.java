package com.quest.etna.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "address")
public class    Address implements Serializable {

    @Id
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String street;

    @Column(nullable = false, length = 30)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String postalCode;

    @Column(nullable = false, length = 50)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String city;

    @Column(nullable = false, length = 50)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String country;

    @ManyToOne
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JoinColumn(nullable = false, name="user_id")
    private User user;

    @JsonIgnore
    private Date creationDate;

    @JsonIgnore
    private Date updatedDate;

    public Address() {
        this.creationDate = new Date();
        this.updatedDate = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @PrePersist
    public void persist() {
        this.creationDate = new Date();
    }

    @PreUpdate
    public  void update() {
        this.updatedDate = new Date();
    }
}
