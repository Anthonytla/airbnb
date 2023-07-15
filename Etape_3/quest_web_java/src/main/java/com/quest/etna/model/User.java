package com.quest.etna.model;

import com.quest.etna.enums.UserRole;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreatedDate
    private Date creationDate;

    @LastModifiedDate
    private Date updatedDate;

    public User() {
        role = UserRole.ROLE_USER;
        this.creationDate = new Date();
        this.updatedDate = new Date();
    }

    public String getUsername() {
        return username;
    }

    public User(int id, String username, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.creationDate = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(username, user.username) && Objects.equals(password, user.password) && role == user.role && Objects.equals(creationDate, user.updatedDate) && Objects.equals(updatedDate, user.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role, creationDate, updatedDate);
    }

    @Override
    public String toString() {
        return username + "_" + id;

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return creationDate;
    }

    public void setCreatedAt(Date createdAt) {
        this.creationDate = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedDate;
    }

    public void setUpdatedAt(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

}
