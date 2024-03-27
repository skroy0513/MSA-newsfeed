package kr.co.reservesite.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("테스트 코드 테스트")
    void home() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello world"));
    }

    @Test
    void register() {
    }

    @Test
    void confirm() {
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void changePw() {
    }

    @Test
    void changeProfile() {
    }

    @Test
    void getNickname() {
    }
}