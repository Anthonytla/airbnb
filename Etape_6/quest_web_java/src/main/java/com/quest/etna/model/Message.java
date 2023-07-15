package com.quest.etna.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "message")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, length = 255)
    private String fromHost;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String text;
    @ManyToOne
    @JoinColumn(nullable = false, name="address_id")
    private Address address;
    // getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Message(String fromHost, String text, Address address) {
        this.fromHost = fromHost;
        this.text = text;
        this.address = address;
    }

    public Message() {

    }

    public String getFromHost() {
        return fromHost;
    }

    public void setFromHost(String fromHost) {
        this.fromHost = fromHost;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        return "MessageModel{" +
                "message='" + text + '\'' +
                ", fromLogin='" + fromHost + '\'' +
                '}';
    }
}