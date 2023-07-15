package com.quest.etna;

import com.quest.etna.annotation.Address;
import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.model.JwtUserDetails;
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
@Sql(statements = "INSERT INTO quest_web.user (id, creation_date, password, role, updated_date, username) VALUES (3, NOW(), '$2a$10$oDKQxlwnFnzMZlyQvPpKGuAEK5ctZQjhSvfvcWIVp45ChTlCCpTEu', 'ROLE_HOTE', NOW(), 'host'), (4, NOW(), '$2a$10$oDKQxlwnFnzMZlyQvPpKGuAEK5ctZQjhSvfvcWIVp45ChTlCCpTEu', 'ROLE_HOTE', NOW(), 'host4');" +
        "INSERT INTO quest_web.address (id, availability, creation_date, updated_date, city, street, country, postal_code, price, name, description, user_id, note) VALUES (2,1, NOW(), NOW(),'Paris','Rue de Faubourg','FR', 94700,850,'Lobby','TEST', 4, 0)")
@Address
public class AddressTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    public void testAddress() throws Exception {

        System.out.println("-----------------------------ADDRESS----------------------------------");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/address/visitor"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/address/visitor"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/address"))
                .andExpect(status().isUnauthorized());






        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername("host");
        String token = jwtTokenUtil.generateToken(jwtUserDetails);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content("{\"city\": \"Paris\", \"country\":\"France\", \"street\":\"rue de france\", \"postalCode\":\"75013\", \"description\":\"test\"" +
                                ", \"price\":232.32, \"name\":\"test\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/address")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"rue de france\"")));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/address/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content("{\"city\": \"Venezuela\", \"country\":\"France\", \"street\":\"rue de france\", \"postalCode\":\"75013\", \"description\":\"test\"" +
                                ", \"price\":232.32, \"name\":\"test\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"city\":\"Venezuela\"")));





        JwtUserDetails jwtUserDetails4 = jwtUserDetailsService.loadUserByUsername("host4");
        String token4 = jwtTokenUtil.generateToken(jwtUserDetails4);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/address/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token4)
                        .content("{\"city\": \"Venezuela2\", \"country\":\"France\", \"street\":\"rue de france\", \"postalCode\":\"75013\", \"description\":\"test\"" +
                                ", \"price\":232.32, \"name\":\"test\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());






        JwtUserDetails jwtUser = jwtUserDetailsService.loadUserByUsername("traveler");
        String tokenUser = jwtTokenUtil.generateToken(jwtUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenUser)
                        .content("{\"city\": \"Paris\", \"country\":\"France\", \"street\":\"rue de france\", \"postalCode\":\"75013\", \"description\":\"test\"" +
                                ", \"price\":232.32, \"name\":\"test\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/address/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenUser))
                .andExpect(status().isForbidden());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/address/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenUser)
                        .content("{\"city\": \"Venezuela2\", \"country\":\"France\", \"street\":\"rue de france\", \"postalCode\":\"75013\", \"description\":\"test\"" +
                                ", \"price\":232.32, \"name\":\"test\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());





        JwtUserDetails jwtAdmin = jwtUserDetailsService.loadUserByUsername("anthoAdmin");
        String tokenAdmin = jwtTokenUtil.generateToken(jwtAdmin);


        mockMvc.perform(MockMvcRequestBuilders
                        .put("/address/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenAdmin)
                        .content("{\"city\": \"Chicago\", \"country\":\"France\", \"street\":\"rue de france\", \"postalCode\":\"75013\", \"description\":\"test\"" +
                                ", \"price\":232.32, \"name\":\"test\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"city\":\"Chicago\"")));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/address/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenAdmin))
                .andExpect(status().isOk());



        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/address/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isForbidden());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/address/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token4))
                .andExpect(status().isOk());
    }

}
