package com.quest.etna.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "address")
public class    Address implements Serializable {

    @Id
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Transient
    public int reviewsNb;
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
    @OneToMany(mappedBy = "address", cascade = CascadeType.REMOVE)
    List<Review> reviews;

    @OneToMany(mappedBy = "address", cascade = CascadeType.REMOVE)
    @JsonIgnore
    List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @JsonIgnore
    private Date creationDate;

    @JsonIgnore
    private Date updatedDate;

    @Column(nullable = false)
    private float price;
    @Column(nullable = false)
    private boolean availability;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int max_voyageur;
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @OneToMany(mappedBy = "address", cascade = CascadeType.REMOVE)
    @JsonIgnore
    List<Reservation> reservations;

    @Column(columnDefinition = "LONGTEXT")
    private String imageData;

    private float note;

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

   /* private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }*/

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMax_voyageur() {
        return max_voyageur;
    }

    public void setMax_voyageur(int max_voyageur) {
        this.max_voyageur = max_voyageur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
