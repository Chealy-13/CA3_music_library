package com.example.ca3_music_library.controllers;


import com.example.ca3_music_library.Persistence.UserDao;
import com.example.ca3_music_library.business.User;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController controller;
    private UserDao userDao;
    private HttpSession session;

    @Test
    void testRegisterSuccess() {
        String username = "DamianUser";
        String password = "Damian2002";
        String confirm = "Damian2002";
        String first = "Damian";
        String last = "Magiera";
        String email = "Damian@example.com";
        Model model = new ExtendedModelMap();

        String result = controller.register(username, password, confirm, first, last, email, model, session);

        assertEquals("redirect:/payment", result);
        assertNotNull(session.getAttribute("currentUser"));
        assertTrue(model.asMap().isEmpty());
    }

    @Test
    void testRegisterFailureUsernameIsBlank() {
        String username = "";
        String password = "Damian2002";
        String confirm = "Damian2002";
        String email = "Damian@example.com";
        Model model = new ExtendedModelMap();

        String result = controller.register(username, password, confirm, null, null, email, model, session);

        assertEquals("registration", result);
        assertEquals("Cannot register without a username", model.asMap().get("errorMessage"));
        assertNull(session.getAttribute("currentUser"));
    }

    @Test
    void testRegisterFailurePasswordAreNotMatching() {

        String username = "DamianUse";
        String password = "hello";
        String confirm = "bye";
        String email = "Damian@example.com";
        Model model = new ExtendedModelMap();

        String result = controller.register(username, password, confirm, null, null, email, model, session);

        assertEquals("registration", result);
        assertEquals("Passwords must match!", model.asMap().get("errorMessage"));
        assertNull(session.getAttribute("currentUser"));
    }

    @Test
    void testLoginSuccess() {
        String username = "Damian";
        String password = "Damian2002";
        Model model = new ExtendedModelMap();
        String result = controller.login(username, password, model, session);

        assertEquals("index", result);
        assertEquals("Login successful", model.asMap().get("message"));
        assertNotNull(session.getAttribute("currentUser"));
    }

    @Test
    void testLoginFailureIncorrectCredentials() {
        String username = "Damian";
        String password = "wrongPassword";
        Model model = new ExtendedModelMap();

        String result = controller.login(username, password, model, session);

        assertEquals("login", result);
        assertEquals("Username/password incorrect.", model.asMap().get("errorMessage"));
        assertNull(session.getAttribute("currentUser"));
    }

    @Test
    void testLoginFailureBlankUsername() {
        String username = "";
        String password = "Damian2002";
        Model model = new ExtendedModelMap();

        String result = controller.login(username, password, model, session);

        assertEquals("login", result);
        assertEquals("Cannot register without a username", model.asMap().get("errorMessage"));
        assertNull(session.getAttribute("currentUser"));
    }

    @Test
    void testLoginFailureBlankPassword() {
        String username = "Damian";
        String password = "";
        Model model = new ExtendedModelMap();


        String result = controller.login(username, password, model, session);

        assertEquals("login", result);
        assertEquals("Cannot register without a password", model.asMap().get("errorMessage"));
        assertNull(session.getAttribute("currentUser"));
    }

    @Test
    void testViewProfileNotLoggedIn() {
        Model model = new ExtendedModelMap();
        String result = controller.viewProfile(model, session);

        assertEquals("login", result);
        assertEquals("You need to log in to view your profile.", model.asMap().get("errorMessage"));
        assertNull(model.asMap().get("user"));
    }
    @Test
    void testShowEditProfilePageLoggedIn() {

        Model model = new ExtendedModelMap();
        User loggedInUser = new User();
        loggedInUser.setUsername("testUser");
        session.setAttribute("currentUser", loggedInUser);

        String result = controller.showEditProfilePage(model, session);

        assertEquals("editProfile", result);
        assertEquals(loggedInUser, model.asMap().get("user"));
        assertNull(model.asMap().get("errorMessage"));
    }

    @Test
    void testShowEditProfilePageNotLoggedIn() {
        Model model = new ExtendedModelMap();

        String result = controller.showEditProfilePage(model, session);

        assertEquals("login", result);
        assertEquals("You need to log in to edit your profile.", model.asMap().get("errorMessage"));
        assertNull(model.asMap().get("user"));
    }

    @Test
    void testUpdateUserDetailsNotLoggedIn() {
        Model model = new ExtendedModelMap();

        String result = controller.updateUserDetails("username", "first", "last", "email", session, model);

        assertEquals("login", result);
        assertEquals("You need to log in to edit your profile.", model.asMap().get("errorMessage"));
    }
    }

