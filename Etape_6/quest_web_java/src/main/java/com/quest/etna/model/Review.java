package com.quest.etna.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "review")
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "LONGTEXT")
    private String commentaire;

    @CreatedDate
    private Date creationDate;

    @LastModifiedDate
    private Date updatedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @ManyToOne
    @JoinColumn(nullable = false, name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name="address_id")
    private Address address;

    @Column(nullable = false)
    private float note;

    @Column(nullable = false)
    private int cleanliness;

    @Column(nullable = false)
    private int services;

    public int getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(int cleanliness) {
        this.cleanliness = cleanliness;
    }

    public int getServices() {
        return services;
    }

    public void setServices(int services) {
        this.services = services;
    }

    public int getQualityPrice() {
        return qualityPrice;
    }

    public void setQualityPrice(int qualityPrice) {
        this.qualityPrice = qualityPrice;
    }

    @Column(nullable = false)
    private int qualityPrice;

    @PrePersist
    public void persist() {
        this.creationDate = new Date();
    }

    @PreUpdate
    public  void update() {
        this.updatedDate = new Date();
    }
}
