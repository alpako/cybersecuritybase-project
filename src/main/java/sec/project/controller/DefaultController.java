package sec.project.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    @RequestMapping("*")
    public String catchall(Authentication authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("user", authentication.getName());
        }
        model.addAttribute("successMessage", null);
        return "home";
    }

    @RequestMapping("/")
    public String root(Authentication authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("user", authentication.getName());
        }
        model.addAttribute("successMessage", null);
        return "home";
    }
}
