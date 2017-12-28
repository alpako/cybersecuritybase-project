package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sec.project.repository.CourseRepository;

@Controller
public class DefaultController {

    @Autowired
    CourseRepository courseRepository;

    @RequestMapping("*")
    public String catchall(Authentication authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("user", authentication.getName());
        }
        model.addAttribute("successMessage", null);
        model.addAttribute("allCourses", courseRepository.findAll());
        return "home";
    }

    @RequestMapping("/")
    public String root(Authentication authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("user", authentication.getName());
        }
        model.addAttribute("successMessage", null);
        model.addAttribute("allCourses", courseRepository.findAll());
        return "home";
    }
}
