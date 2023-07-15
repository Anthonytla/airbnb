package com.quest.etna;

import com.jayway.jsonpath.JsonPath;
import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    @Order(1)
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
            "description VARCHAR(255)," +
            "max_voyageur int DEFAULT 0," +
            "street VARCHAR(100) NOT NULL," +
            "postal_code VARCHAR(30) NOT NULL," +
            "city VARCHAR(50) NOT NULL," +
            "country VARCHAR(50) NOT NULL," +
            "creation_date DATETIME NULL," +
            "updated_date DATETIME NULL," +
            "image_data LONGTEXT NULL," +
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
            "commentaire VARCHAR(225)," +
            "note int NOT NULL," +
            "PRIMARY KEY (id)," +
            "FOREIGN KEY (user_id) REFERENCES user (id)," +
            "FOREIGN KEY (address_id) REFERENCES address (id));" +
            "ALTER TABLE user AUTO_INCREMENT=1;"
    )
    public void testAuthenticate() throws Exception {

        userRepository.deleteAll();

        System.out.println("INNNNNNNNN");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content("{\"username\": \"antho51\", \"password\":\"root\", \"role\":\"ROLE_USER\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content("{\"username\": \"antho51\", \"password\":\"root\", \"role\":\"ROLE_USER\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/authenticate")
                        .content("{\"username\": \"antho51\", \"password\":\"root\"}")
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

    @Test
    @Order(2)
    @Sql(statements = "USE quest_web; INSERT INTO quest_web.user (id, creation_date, password, role, updated_date, username) VALUES (2, NOW(), '$2a$10$oDKQxlwnFnzMZlyQvPpKGuAEK5ctZQjhSvfvcWIVp45ChTlCCpTEu', 'ROLE_ADMIN', NOW(), 'anthoAdmin'),(3, NOW(), 'root', 'ROLE_USER', NOW(), 'toDelete')")
    public void testUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user"))
                .andExpect(status().isUnauthorized());

        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername("antho51");
        String token = jwtTokenUtil.generateToken(jwtUserDetails);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isForbidden());

        JwtUserDetails jwtUserDetailsAdmin = jwtUserDetailsService.loadUserByUsername("anthoAdmin");
        String tokenAdmin = jwtTokenUtil.generateToken(jwtUserDetailsAdmin);
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        /*mockMvc.perform(MockMvcRequestBuilders
                        .post("/authenticate")
                        .content("{\"username\": \"anthoAdmin\", \"password\":\"root\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                result -> mockMvc.perform(MockMvcRequestBuilders.delete("/user/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenAdmin))
                        .andExpect(status().isOk()));*/
    }

    @Test
    @Order(3)
    @Sql(statements = "INSERT INTO quest_web.user (id, creation_date, password, role, updated_date, username) VALUES (3, NOW(), '$2a$10$oDKQxlwnFnzMZlyQvPpKGuAEK5ctZQjhSvfvcWIVp45ChTlCCpTEu', 'ROLE_USER', NOW(), 'user')")
    public void testAddress() throws Exception {
//        addressRepository.deleteAll();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/address"))
                .andExpect(status().isUnauthorized());

        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername("antho51");
        String token = jwtTokenUtil.generateToken(jwtUserDetails);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/address")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content("{\"city\": \"Paris\", \"country\":\"France\", \"street\":\"rue de france\", \"postalCode\":\"75013\", \"description\":\"test\"" +
                                ", \"price\":232.32, \"name\":\"test\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        JwtUserDetails jwtUser = jwtUserDetailsService.loadUserByUsername("user");
        String tokenUser = jwtTokenUtil.generateToken(jwtUser);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/address/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenUser))
                .andExpect(status().isForbidden());

        JwtUserDetails jwtAdmin = jwtUserDetailsService.loadUserByUsername("anthoAdmin");
        String tokenAdmin = jwtTokenUtil.generateToken(jwtAdmin);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/address/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenAdmin))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @Sql(statements = "INSERT INTO quest_web.address (id, availability, user_id, creation_date, city, country, street, postal_code, description" +
            ", price, name , updated_date) VALUES (4, 1, 3, NOW(), 'Paris', 'France','coco','45000', 'cocotier test', 231.23, " +
            "'voyager chez coco',NOW())")
    public void testReservation() throws Exception {
//        addressRepository.deleteAll();
       /* mockMvc.perform(MockMvcRequestBuilders
                        .get("/address/3/reservation"))
                .andExpect(status().isUnauthorized());

        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername("antho51");
        String token = jwtTokenUtil.generateToken(jwtUserDetails);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/address")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
*/

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/2/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());


        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername("antho51");
        String token = jwtTokenUtil.generateToken(jwtUserDetails);
        String end = "2026-12-31";
        String start = "2023-01-01";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/3/reservation")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(String.format("{\"starting_date\": \"%s\" ,\"ending_date\": \"%s\"}", start, end))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/2/reservation")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(String.format("{\"starting_date\": \"%s\" ,\"ending_date\": \"%s\"}", start, end))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        end = "2028-12-31";
        start = "2024-01-01";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/2/reservation")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(String.format("{\"starting_date\": \"%s\" ,\"ending_date\": \"%s\"}", start, end))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        end = "2023-06-31";
        start = "2022-12-01";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/2/reservation")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(String.format("{\"starting_date\": \"%s\" ,\"ending_date\": \"%s\"}", start, end))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        end = "2025-12-31";
        start = "2024-01-01";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/2/reservation")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(String.format("{\"starting_date\": \"%s\" ,\"ending_date\": \"%s\"}", start, end))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        end = "2019-12-31";
        start = "2018-01-01";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/2/reservation")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(String.format("{\"starting_date\": \"%s\" ,\"ending_date\": \"%s\"}", start, end))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnavailableForLegalReasons());

        end = "2027-12-31";
        start = "2028-01-01";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/2/reservation")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(String.format("{\"starting_date\": \"%s\" ,\"ending_date\": \"%s\"}", start, end))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        /*JwtUserDetails jwtUser = jwtUserDetailsService.loadUserByUsername("user");
        String tokenUser = jwtTokenUtil.generateToken(jwtUser);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/address/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenUser))
                .andExpect(status().isForbidden());

        JwtUserDetails jwtAdmin = jwtUserDetailsService.loadUserByUsername("anthoAdmin");
        String tokenAdmin = jwtTokenUtil.generateToken(jwtAdmin);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/address/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenAdmin))
                .andExpect(status().isOk());*/
    }
    @Test
    @Order(4)
    @Sql(statements = "INSERT INTO quest_web.reservation (id, starting_date, ending_date, user_id, address_id)" +
            "VALUES (3, '2020-12-31', '2022-12-31', 1, 2)")
    public void testReview() throws Exception {
//        addressRepository.deleteAll();
       /* mockMvc.perform(MockMvcRequestBuilders
                        .get("/address/3/reservation"))
                .andExpect(status().isUnauthorized());

        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername("antho51");
        String token = jwtTokenUtil.generateToken(jwtUserDetails);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/address")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
*/

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/2/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());


        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername("antho51");
        String token = jwtTokenUtil.generateToken(jwtUserDetails);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/3/review")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content("{\"commentaire\": \"top\" ,\"note\": \"5\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/2/review")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content("{\"commentaire\": \"top\" ,\"note\": \"5\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        /*JwtUserDetails jwtUser = jwtUserDetailsService.loadUserByUsername("user");
        String tokenUser = jwtTokenUtil.generateToken(jwtUser);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/address/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenUser))
                .andExpect(status().isForbidden());

        JwtUserDetails jwtAdmin = jwtUserDetailsService.loadUserByUsername("anthoAdmin");
        String tokenAdmin = jwtTokenUtil.generateToken(jwtAdmin);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/address/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenAdmin))
                .andExpect(status().isOk());*/
    }
}
