package controllers;

import Persistence.UserDao;
import Persistence.UserDaoImpl;
import business.User;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class UserController {
    @PostMapping("register")
    public String register(@RequestParam(name = "username") String username,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "confirm") String confirm,
                           @RequestParam(name = "first", required = false) String first,
                           @RequestParam(name = "last", required = false) String last,
                           @RequestParam(name = "email") String email,
                           Model model, HttpSession session) {
        String errorMessage = null;
        if (username == null || username.isBlank()) {
            errorMessage = "You must create a username to register";

        } else if (password == null || password.isBlank()) {
            errorMessage = "You must create a password to register";
        } else if (confirm == null || confirm.isBlank() || !confirm.equals(password)) {
            errorMessage = "Passwords must match!";

        } else if (email == null || email.isBlank()) {
            errorMessage = " You can not register without a valid email";
        }
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            return "registration";
        }
        User newUser = User.builder()
                .username(username)
                .password(password)
                .firstName(first)
                .lastName(last)
                .email(email)
                .isAdmin(false)
                .build();
        UserDao userDao = new UserDaoImpl("database.properties");
        boolean registered = userDao.registerUser(newUser);
        if (registered) {
            String success = "Registration successful, you are now logged in.";
            model.addAttribute("message", success);
            User loggedInUser = userDao.loginUser(username, password);
            session.setAttribute("currentUser", loggedInUser);
            return "index";
        } else {
            log.info("Could not register user with username: " + username + " and email: " + email + ".");
            String failed = "Username/email address unavailable.";
            model.addAttribute("errorMessage", failed);
            return "registration";
        }
    }

    @PostMapping("login")
    public String login(@RequestParam(name = "username") String username,
                        @RequestParam(name = "password") String password,
                        Model model, HttpSession session) {
        String errorMsg = null;
        if (username == null || username.isBlank()) {
            errorMsg = "Cannot register without a username";
        } else if (password == null || password.isBlank()) {
            errorMsg = "Cannot register without a password";
        }
        if (errorMsg != null) {
            model.addAttribute("errorMessage", errorMsg);
            return "login";
        }

        UserDao userDao = new UserDaoImpl("database.properties");
        User loggedInUser = userDao.loginUser(username, password);
        if (loggedInUser != null) {
            String success = "Login successful";
            model.addAttribute("message", success);
            session.setAttribute("currentUser", loggedInUser);
            return "index";
        } else {
            String failed = "Username/password incorrect.";
            model.addAttribute("errorMessage", failed);
            return "login";
        }
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("currentUser");
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "You need to log in to view your profile.");
            return "login";
        }
        model.addAttribute("user", loggedInUser);
        return "profile";
    }
    @GetMapping("/editProfile")
    public String showEditProfilePage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("currentUser");
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "You must login to edit details");
            return "login";
        }
        model.addAttribute("user", loggedInUser);
        return "editProfile";
    }

}