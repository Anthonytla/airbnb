package com.quest.etna;

import com.jayway.jsonpath.JsonPath;
import com.quest.etna.annotation.Authentication;
import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//    @Sql(statements = "ALTER TABLE user AUTO_INCREMENT=0")
@Sql(statements = "DROP DATABASE IF EXISTS quest_web; CREATE DATABASE quest_web; USE quest_web; SET FOREIGN_KEY_CHECKS = 0; DROP TABLE IF EXISTS user; DROP TABLE IF EXISTS address;" +
        "CREATE TABLE user (id int(11) NOT NULL AUTO_INCREMENT," +
        "username VARCHAR(255) NOT NULL UNIQUE, " +
        "password VARCHAR(255) NOT NULL," +
        "role VARCHAR(255) NULL," +
        "creation_date DATETIME NULL," +
        "updated_date DATETIME NULL," +
        "PRIMARY KEY (id));" +
        "SET FOREIGN_KEY_CHECKS = 1;" +
        "CREATE TABLE address (id int(11) NOT NULL AUTO_INCREMENT," +
        "user_id int(11) NOT NULL," +
        "availability int NOT NULL," +
        "price decimal NOT NULL," +
        "name VARCHAR(100) NOT NULL," +
        "description LONGTEXT," +
        "max_voyageur int DEFAULT 0," +
        "street VARCHAR(100) NOT NULL," +
        "postal_code VARCHAR(30) NOT NULL," +
        "city VARCHAR(50) NOT NULL," +
        "country VARCHAR(50) NOT NULL," +
        "creation_date DATETIME NULL," +
        "updated_date DATETIME NULL," +
        "image_data LONGTEXT NULL," +
        "note FLOAT NULL, " +
        "PRIMARY KEY (id)," +
        "FOREIGN KEY (user_id) REFERENCES user (id));" +
        "CREATE TABLE reservation (id int(11) NOT NULL AUTO_INCREMENT," +
        "user_id int(11) NOT NULL," +
        "address_id int(11) NOT NULL," +
        "starting_date DATETIME NOT NULL," +
        "ending_date DATETIME NOT NULL," +
        "PRIMARY KEY (id)," +
        "FOREIGN KEY (user_id) REFERENCES user (id)," +
        "FOREIGN KEY (address_id) REFERENCES address (id));" +
        "CREATE TABLE review (id int(11) NOT NULL AUTO_INCREMENT," +
        "user_id int(11) NOT NULL," +
        "creation_date DATETIME NULL," +
        "updated_date DATETIME NULL," +
        "address_id int(11) NOT NULL," +
        "commentaire LONGTEXT NULL," +
        "note FLOAT NULL, " +
        "cleanliness int NULL," +
        "services int NULL," +
        "quality_price int NULL," +
        "PRIMARY KEY (id)," +
        "FOREIGN KEY (user_id) REFERENCES user (id)," +
        "FOREIGN KEY (address_id) REFERENCES address (id));" +
        "CREATE TABLE message (id int(11) NOT NULL AUTO_INCREMENT," +
        "from_host VARCHAR(225) NOT NULL," +
        "address_id int NOT NULL," +
        "text MEDIUMTEXT NULL," +
        "PRIMARY KEY (id)," +
        "FOREIGN KEY (address_id) REFERENCES address (id));" +
        "ALTER TABLE user AUTO_INCREMENT=1;"
)
@Authentication
public class AuthenticationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired

    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    public void testAuthentication() throws Exception {


        System.out.println("-----------------------------AUTHENTICATION----------------------------------");
        userRepository.deleteAll();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content("{\"username\": \"traveler\", \"password\":\"root\", \"role\":\"ROLE_VOYAGEUR\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content("{\"username\": \"traveler\", \"password\":\"root\", \"role\":\"ROLE_VOYAGEUR\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/authenticate")
                        .content("{\"username\": \"traveler\", \"password\":\"zzcdocjeo\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/authenticate")
                        .content("{\"username\": \"traveler\", \"password\":\"root\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")))
                .andReturn();


        String token = JsonPath.read(response.getResponse().getContentAsString(), "$.token");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }
}
