package controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LanguageController {

    @GetMapping("/changeLanguage")
    public String changeLanguage(HttpServletRequest request){
        return "redirect:"+ request.getHeader("referer");
    }
}
