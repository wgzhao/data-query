package com.github.wgzhao.dbquery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestAuth {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuth() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcwNjU3ODQxNCwiZXhwIjoxNzA2NjY0ODE0fQ.VZpkoL7ADRIUE3EYwLttNw8MLjnX7em1K5d8LmjjEj4";
        mockMvc.perform(get("/api/v1/datasources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token ))
                .andExpect(status().isOk());

    }
}
