package com.quest.etna.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quest.etna.enums.UserRole;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class UserResponse extends UserDetails {


    public UserResponse(String username, UserRole role) {
        super(username, role);
    }
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int id;

    private Date creationDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    private Date updatedDate;

}
