package controllers;

import Persistence.UserDao;
import Persistence.UserDaoImpl;
import business.User;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
public class UserController {
    @PostMapping("register")
    public String register(@RequestParam(name = "username") String username,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "confirmPass") String confirm,
                           @RequestParam(name = "first", required = false) String first,
                           @RequestParam(name = "last", required = false) String last,
                           @RequestParam(name = "email") String email,
                           Model model, HttpSession session) {
        String errorMsg = null;
        if (username == null || username.isBlank()) {
            errorMsg = "Cannot register without a username";
        } else if (password == null || password.isBlank()) {
            errorMsg = "Cannot register without a password";
        } else if (confirm == null || confirm.isBlank() || !confirm.equals(password)) {
            errorMsg = "Passwords must match!";
        } else if (email == null || email.isBlank()) {
            errorMsg = "Cannot register without a valid email";
        }
        if (errorMsg != null) {
            model.addAttribute("errorMessage", errorMsg);
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
        boolean registered = userDao.register(newUser);
        if (registered) {
            String success = "Registration successful, you are now logged in.";
            model.addAttribute("message", success);
            User loggedInUser = userDao.login(username, password);
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
        User loggedInUser = userDao.login(username, password);
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

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        session.setAttribute("currentUser", null);

        model.addAttribute("message", "Logout successful.");
        return "index";
    }

    @GetMapping("/editProfile")
    public String showEditProfilePage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("currentUser");
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "You need to log in to edit your profile.");
            return "login"; // Redirect to login if the user is not logged in
        }
        model.addAttribute("user", loggedInUser); // Pass the user details to the view
        return "editProfile";
    }

    @PostMapping("/edit")
    public String updateUserDetails(@RequestParam(name = "username") String username,
                                    @RequestParam(name = "first", required = false) String first,
                                    @RequestParam(name = "last", required = false) String last,
                                    @RequestParam(name = "email") String email,
                                    HttpSession session, Model model) {
        log.info("POST /edit triggered with username: {}, first: {}, last: {}, email: {}", username, first, last, email);


        User loggedInUser = (User) session.getAttribute("currentUser");
        if (loggedInUser == null) {
            log.error("No user found in session. Redirecting to login page.");
            model.addAttribute("errorMessage", "You need to log in to edit your profile.");
            return "login";
        }


        loggedInUser.setUsername(username);
        loggedInUser.setFirstName(first);
        loggedInUser.setLastName(last);
        loggedInUser.setEmail(email);

        UserDao userDao = new UserDaoImpl("database.properties");
        boolean updated = userDao.updateUser(loggedInUser);

        if (updated) {
            log.info("Profile updated successfully for user: {}", username);
            session.setAttribute("currentUser", loggedInUser); // Update session
            model.addAttribute("message", "Your profile has been updated successfully.");
            return "redirect:/";
        } else {
            log.error("Failed to update profile for user: {}", username);
            model.addAttribute("errorMessage", "Failed to update your profile. Please try again.");
            return "editProfile";
        }
    }
}
