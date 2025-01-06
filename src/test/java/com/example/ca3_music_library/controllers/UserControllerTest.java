package com.example.ca3_music_library.controllers;


import com.example.ca3_music_library.Persistence.UserDao;
import com.example.ca3_music_library.business.User;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private UserController userController;
    private UserDao userDaoMock;
    private Model model;
    private HttpSession session;

    @Test
    void testRegister_PasswordsDoNotMatch() {
        String username = "testUser";
        String password = "Password123";
        String confirmPassword = "Password456";

        String result = userController.register(username, password, confirmPassword, null, null, "test@example.com", model, session);

        Mockito.verify(model).addAttribute("errorMessage", "Passwords must match!");
        assertEquals("registration", result);
    }
}
