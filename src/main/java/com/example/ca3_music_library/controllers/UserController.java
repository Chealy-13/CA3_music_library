package com.example.ca3_music_library.controllers;

import com.example.ca3_music_library.Persistence.UserDao;
import com.example.ca3_music_library.Persistence.UserDaoImpl;
import com.example.ca3_music_library.business.User;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;


@Slf4j
@Controller
public class UserController {
    private final UserDao userDao;

    public UserController() {
        this.userDao = new UserDaoImpl("database.properties");
    }

    /**
     * User registration takes care of validating the input data, creating a new user,
     * and if successful redirect them to the payment page
     *
     * @param username The username(unique) provided by the user during registration.
     * @param password The password provided by the user.
     * @param confirm  The confirmation password entered by the user, for matching purposes
     * @param first    The first name of the user.
     * @param last     The last name of the user.
     * @param email    The email address of the user, used for account communication and identification.
     * @param model    The model object for passing data to the registration view in case of validation errors.
     * @param session  The current HTTP session to store the user data if registration is successful.
     * @return Redirects to the "payment" page for subscription or returns the "registration" page if validation fails.
     */
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
        } else if (!password.matches("^(?=.*[A-Z])(?=.*\\d).+$")) {
            errorMsg = "Password must contain at least one uppercase letter and one number.";
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

        try {
            boolean registered = userDao.register(newUser);
            if (registered) {
                session.setAttribute("currentUser", newUser);
                return "redirect:/payment";
            }
        } catch (IllegalStateException e) {

            model.addAttribute("errorMessage", "Username or email already exists. Please choose a different one.");
            return "registration";
        }

        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        return "registration";
    }

    /**
     * Displays the payment page to allow users to complete their subscription.
     *
     * @param session The current HTTP session to validate the user's login status.
     * @param model   The model object to handle messages or errors.
     *                Returns the "payment" view if the user is logged in, otherwise redirects to the login html page.
     */
    @GetMapping("/payment")
    public String showPaymentPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("currentUser");
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "You need to log in to complete payment.");
            return "login";
        }
        return "payment";
    }

    /**
     * Processes payment by checking the user's credit card details: card number, expiry date, and CVV
     *
     * @param cardNumber The 16-digit credit card number entered by the user.
     * @param expiryDate The card's expiry date in MM/YY format.
     * @param cvv        The 3-digit security code (CVV) on the card.
     * @param session    The current HTTP session to get the logged-in user's information.
     * @param model      Used to display success or error messages.
     *                   return Redirects to the home page if payment is successful, or back to the "payment" page if the details are invalid.
     */
    @PostMapping("/payment/confirm")
    public String confirmPayment(@RequestParam(name = "cardNumber") String cardNumber,
                                 @RequestParam(name = "expiryDate") String expiryDate,
                                 @RequestParam(name = "cvv") String cvv,
                                 HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("currentUser");
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "You need to log in to complete payment.");
            return "login";
        }

        boolean hasError = false;
        if (!cardNumber.matches("^\\d{16}$")) {
            model.addAttribute("errorMessageCardNumber", "Invalid card number. Must be 16 digits.");
            hasError = true;
        }

        if (!expiryDate.matches("^(0[1-9]|1[0-2])\\/\\d{2}$")) {
            model.addAttribute("errorMessageExpiryDate", "Invalid expiry date. Must be in MM/YY format.");
            hasError = true;
        }


        if (!cvv.matches("^\\d{3}$")) {
            model.addAttribute("errorMessageCvv", "Invalid CVV. Must be 3 digits.");
            hasError = true;
        }

        if (hasError) {
            return "payment";
        }

        loggedInUser.setSubscriptionStatus(true);
        loggedInUser.setSubscriptionExpiry(LocalDate.now().plusYears(1));
        UserDao userDao = new UserDaoImpl("database.properties");
        userDao.updateUser(loggedInUser);

        session.setAttribute("currentUser", loggedInUser);
        session.setAttribute("successMessage", "Registration and payment successful! Welcome to our service.");
        return "redirect:/";
    }


    /**
     * This manages the login process by validating user credentials, authenticating the user, and
     * redirecting them to the home page when login is successful.
     *
     * @param username The username provided by the user during the login stage.
     * @param password The password provided by the user.
     * @param model    The model object to pass notifications.
     * @param session  The current HTTP session to store the authenticated user's details.
     *                 Redirects to "index" if successfully logged in or back to "login" if authentication fails.
     */
    @PostMapping("login")
    public String login(@RequestParam(name = "username") String username,
                        @RequestParam(name = "password") String password,
                        Model model, HttpSession session) {
        String errorMsg = null;
        if (username == null || username.isBlank()) {
            errorMsg = "Cannot login without a username";
        } else if (password == null || password.isBlank()) {
            errorMsg = "Cannot login without a password";
        }
        if (errorMsg != null) {
            model.addAttribute("errorMessage", errorMsg);
            return "login";
        }

        UserDao userDao = new UserDaoImpl("database.properties");
        User loggedInUser = userDao.login(username, password);
        if (loggedInUser != null) {
            String success = "Login successfully";
            model.addAttribute("message", success);
            session.setAttribute("currentUser", loggedInUser);
            return "index";
        } else {
            String failed = "Username/password incorrect.";
            model.addAttribute("errorMessage", failed);
            return "login";

        }
    }

    /**
     * Showcases the logged-in user's profile details.
     * Only logged-in user can view the profile information.
     *
     * @param model   The model object to pass user details to the profile view.
     * @param session The current HTTP session to get the logged-in user's information.
     *                Returns the "profile" view if the user is logged in, otherwise redirects to the login page.
     */
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

    /**
     * This logs out from the current user invalidating their session.
     *
     * @param model The model object to display a logout message.
     *              Returns to the index.html page after successfully logging out the user.
     */
    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        session.setAttribute("currentUser", null);

        model.addAttribute("message", "Logout successful!");
        return "index";
    }

    /**
     * Showcases the profile editing page where users can update their account details.
     *
     * @param model   The model object is used to fill the form with the user's current details
     * @param session The current HTTP session to get the logged-in user's details.
     * @return Shows the editProfile.html page if the user is logged in if not, redirects to the login page.
     */
    @GetMapping("/editProfile")
    public String showEditProfilePage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("currentUser");
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "You need to log in to edit your profile.");
            return "login";
        }
        model.addAttribute("user", loggedInUser);
        return "editProfile";
    }

    /**
     * Updates the user's account details username, first name, last name, and email address.
     * The changes are saved to the database, and the session is updated with the new details
     *
     * @param username The new username entered by the user.
     * @param first    The new first name.
     * @param last     The new last name.
     * @param email    The new email address entered by the user.
     * @param session  The current HTTP session to update the user's details.
     * @param model    Used to display success or error messages.
     * @return Redirects to the home page if the update is successful, or back to the "editProfile" page if it fails.
     */
    @PostMapping("/edit")
    public String updateUserDetails(@RequestParam(name = "username") String username,
                                    @RequestParam(name = "first", required = false) String first,
                                    @RequestParam(name = "last", required = false) String last,
                                    @RequestParam(name = "email") String email,
                                    HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("currentUser");
        if (loggedInUser == null) {
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
            session.setAttribute("currentUser", loggedInUser);
            model.addAttribute("message", "Your profile has been updated successfully.");
            return "redirect:/profile";
        } else {
            model.addAttribute("errorMessage", "Failed to update your profile. Please try again.");
            return "editProfile";
        }

    }
    /**
     * Displays the subscription renewal page to the logged-in user,
     * if the user is not logged in, they will be sent the login page with an error message.
     *
     * @param session The current HTTP session to retrieve the logged-in user's information.
     * @param model   The model object used to pass attributes to the view.
     * Returns the "renewSubscription" view if the user is logged in, if not sent
     *         to the "login" view with an error message.
     */
    @GetMapping("/renew")
    public String showRenewalPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("currentUser");
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "You need to log in to renew your subscription first!");
            return "login";
        }
        model.addAttribute("user", loggedInUser);
        return "renewSubscription";
    }
    /**
     * This method takes care of subscription renewals.
     * Validates the provided card details
     *
     * @param cardNumber  The 16-digit credit card number,
     * @param expiryDate  The expiry date of the card in MM/YY format.
     * @param cvv         A 3 digit CVV code of the card.
     *                    The current HTTP session to retrieve the logged-in users' information.
     * @param model       The model object used to display success or error messages.
     * @return sends to the home page with a success message if the renewal is successful,
     *         or returns to the renewSubscription page with error messages
     */
    @PostMapping("/renew/confirm")
    public String confirmRenewal(@RequestParam(name = "cardNumber") String cardNumber,
                                 @RequestParam(name = "expiryDate") String expiryDate,
                                 @RequestParam(name = "cvv") String cvv,
                                 HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("currentUser");
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "You must log in to renew your subscription");
            return "login";
        }

        if (!cardNumber.matches("^\\d{16}$")) {
            model.addAttribute("errorMessageCardNumber", "Invalid card number, must be 16 digits, Please try again!");
            return "renewSubscription";
        }

        if (!expiryDate.matches("^(0[1-9]|1[0-2])\\/\\d{2}$")) {
            model.addAttribute("errorMessageExpiryDate", "Invalid card month format, must be (MM/YY), Please try again!");
            return "renewSubscription";
        }

        if (!cvv.matches("^\\d{3}$")) {
            model.addAttribute("errorMessageCvv", "Invalid CVV, must be 3 digits, Please try again!");
            return "renewSubscription";
        }

        LocalDate currentExpiry = loggedInUser.getSubscriptionExpiry();
        LocalDate newExpiry = (currentExpiry != null && currentExpiry.isAfter(LocalDate.now()))
                ? currentExpiry.plusYears(1)
                : LocalDate.now().plusYears(1);

        loggedInUser.setSubscriptionStatus(true);
        loggedInUser.setSubscriptionExpiry(newExpiry);

        UserDao userDao = new UserDaoImpl("database.properties");
        userDao.updateSubscription(loggedInUser.getUsername(), true, newExpiry);

        session.setAttribute("currentUser", loggedInUser);
        session.setAttribute("successMessage", "Subscription renewed successfully!");

        return "redirect:/";
    }
    /**
     * This method is made for confirmation of subscription renewal,
     * Validates the credit card details,the user's subscription
     *mstatus and expiry date in the system.
     *
     * @param cardNumber  The 16-digit credit card number.
     * @param expiryDate  The expiry date of the credit card in MM/YY format.
     * @param cvv         The 3-digit CVV.
     * @param session     The current HTTP session used to retrieve the logged-in user's information.
     * @param model       The model object used to handle and display messages
     * @return  to the home page with a success message if the renewal is successful,
     *         or returns to the renewSubscription page with appropriate error messages.
     */
    @PostMapping("/renewSubscription/confirm")
    public String confirmRenewSubscription(@RequestParam(name = "cardNumber") String cardNumber,
                                           @RequestParam(name = "expiryDate") String expiryDate,
                                           @RequestParam(name = "cvv") String cvv,
                                           HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("currentUser");
        if (loggedInUser == null) {
            model.addAttribute("errorMessage", "You need to log in to renew your subscription.");
            return "login";
        }

        if (!cardNumber.matches("^\\d{16}$")) {
            model.addAttribute("errorMessage", "Invalid card number, must be 16 digits, Please try again!");
            return "renewSubscription";
        }

        if (!expiryDate.matches("^(0[1-9]|1[0-2])\\/\\d{2}$")) {
            model.addAttribute("errorMessage", "Invalid card month format, must be (MM/YY), Please try again!");
            return "renewSubscription";
        }

        if (!cvv.matches("^\\d{3}$")) {
            model.addAttribute("errorMessage", "Invalid CVV, must be 3 digits, Please try again!");
            return "renewSubscription";
        }

        LocalDate newExpiryDate = loggedInUser.getSubscriptionExpiry() != null && loggedInUser.getSubscriptionExpiry().isAfter(LocalDate.now())
                ? loggedInUser.getSubscriptionExpiry().plusYears(1)
                : LocalDate.now().plusYears(1);

        loggedInUser.setSubscriptionStatus(true);
        loggedInUser.setSubscriptionExpiry(newExpiryDate);

        UserDao userDao = new UserDaoImpl("database.properties");
        userDao.updateUser(loggedInUser);

        session.setAttribute("currentUser", loggedInUser);
        session.setAttribute("successMessage", "Subscription renewed successfully!");

        return "redirect:/";
    }
    /**
     * Displays a greeting message on the greeting page, with the current user logged,
     * Otherwise, it displays a generic welcome message.
     *
     * @param session The current HTTP session used to retrieve the logged-in user's information.
     * @param model   The model object used to pass the greeting message to the view.
     * @return the greeting page to display the greeting message.
     */
    @GetMapping("/greeting")
    public String showGreeting(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("currentUser");

        if (loggedInUser != null) {
            model.addAttribute("greetingMessage", "Welcome back, " + loggedInUser.getFirstName() + "!");
        } else {
            model.addAttribute("greetingMessage", "Welcome to our music library!");
        }

        return "greeting";



    }
}

