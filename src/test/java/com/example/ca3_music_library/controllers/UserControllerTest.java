package com.example.ca3_music_library.controllers;


import com.example.ca3_music_library.Persistence.UserDao;
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
        String confirmPassword = "Password6";

        String result = userController.register(username, password, confirmPassword, null, null, "test@example.com", model, session);

        Mockito.verify(model).addAttribute("errorMessage", "Passwords must match!");
        assertEquals("registration", result);
    }

    @Test
    void testRegister_PasswordWithoutUppercaseOrNumber() {
        HttpSession session = mock(HttpSession.class);
        Model model = mock(Model.class);

        String username = "Damian";
        String password = "password";
        String confirmPass = "password";
        String first = "Damian";
        String last = "Magiera";
        String email = "test@example.com";

        String result = userController.register(username, password, confirmPass, first, last, email, model, session);
        assertEquals("registration", result, "Should go back to registration page for invalid password.");
        verify(model).addAttribute("errorMessage", "Password must contain at least one uppercase letter and one number!");
    }
}
