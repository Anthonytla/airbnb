package com.quest.etna;

import com.quest.etna.annotation.User;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(statements = "USE quest_web; INSERT INTO quest_web.user (id, creation_date, password, role, updated_date, username) VALUES (2, NOW(), '$2a$10$oDKQxlwnFnzMZlyQvPpKGuAEK5ctZQjhSvfvcWIVp45ChTlCCpTEu', 'ROLE_ADMIN', NOW(), 'anthoAdmin'),(3, NOW(), 'root', 'ROLE_VOYAGEUR', NOW(), 'toDelete'), (10, NOW(), '$2a$10$oDKQxlwnFnzMZlyQvPpKGuAEK5ctZQjhSvfvcWIVp45ChTlCCpTEu', 'ROLE_VOYAGEUR', NOW(), 'toUpdate')")
@User
public class UserTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    public void testUser() throws Exception {

        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername("traveler");
        String token = jwtTokenUtil.generateToken(jwtUserDetails);

        JwtUserDetails jwtUpdateUserDetails = jwtUserDetailsService.loadUserByUsername("toUpdate");
        String tokenToUpdate = jwtTokenUtil.generateToken(jwtUpdateUserDetails);

        JwtUserDetails jwtUserDetailsAdmin = jwtUserDetailsService.loadUserByUsername("anthoAdmin");
        String tokenAdmin = jwtTokenUtil.generateToken(jwtUserDetailsAdmin);

        System.out.println("-----------------------------USER----------------------------------");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/10")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenToUpdate)
                        .content("{\"role\": \"ROLE_ADMIN\" ,\"username\": \"lala\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isForbidden());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/10")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenToUpdate)
                        .content("{\"username\": \"toUpdate2\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"username\":\"toUpdate2\"")));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/10")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenAdmin)
                        .content("{\"role\": \"ROLE_ADMIN\" ,\"username\": \"lala\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"role\":\"ROLE_ADMIN\"")))
                .andExpect(content().string(containsString("\"username\":\"lala\"")));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isForbidden());

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

}
