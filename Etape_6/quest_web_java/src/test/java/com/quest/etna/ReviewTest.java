package com.quest.etna;

import com.quest.etna.annotation.Review;
import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.repositories.UserRepository;
import org.junit.jupiter.api.Test;
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
@Review
@Sql(statements = "INSERT INTO quest_web.reservation (id, starting_date, ending_date, user_id, address_id) " +
        "VALUES (3, '2020-12-31', '2022-12-31', 1, 2);" +
        "INSERT INTO quest_web.review (id, creation_date, updated_date, user_id, address_id, commentaire, note, cleanliness, services, quality_price) " +
        "VALUES (1, NOW(), NOW(), 1, 2, 'top de top', 4, 3, 4, 5)")
public class ReviewTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    public void testReview() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/2/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());


        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername("traveler");
        String token = jwtTokenUtil.generateToken(jwtUserDetails);

        JwtUserDetails jwtUserDetails2 = jwtUserDetailsService.loadUserByUsername("traveler2");
        String token2 = jwtTokenUtil.generateToken(jwtUserDetails2);

        JwtUserDetails jwtAdmin = jwtUserDetailsService.loadUserByUsername("anthoAdmin");
        String tokenAdmin = jwtTokenUtil.generateToken(jwtAdmin);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/3/review")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content("{\"commentaire\": \"top\" ,\"cleanliness\": \"5\",\"qualityPrice\": \"5\",\"services\": \"5\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address/2/review")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content("{\"commentaire\": \"top\" ,\"cleanliness\": \"1\",\"qualityPrice\": \"2\",\"services\": \"3\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("\"note\":2.0")));


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/address/visitor/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"note\":3.0")));


        mockMvc.perform(MockMvcRequestBuilders
                        .put("/review/2")
                        .content("{\"commentaire\": \"down\" ,\"cleanliness\": \"2\",\"qualityPrice\": \"3\",\"services\": \"4\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/review/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token2)
                        .content("{\"commentaire\": \"down\" ,\"cleanliness\": \"2\",\"qualityPrice\": \"3\",\"services\": \"4\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/review/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenAdmin)
                        .content("{\"commentaire\": \"down\" ,\"cleanliness\": \"2\",\"qualityPrice\": \"3\",\"services\": \"4\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/review/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content("{\"commentaire\": \"down\" ,\"cleanliness\": \"2\",\"qualityPrice\": \"3\",\"services\": \"4\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/review/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content("{\"commentaire\": \"down\" ,\"cleanliness\": \"2\",\"qualityPrice\": \"3\",\"services\": \"4\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"note\":3.0")));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/review/2"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/review/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token2))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("\"success\":false")));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/review/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"success\":true")));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/review/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"success\":true")));




    }
}
