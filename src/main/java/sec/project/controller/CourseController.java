package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sec.project.domain.Course;
import sec.project.domain.User;
import sec.project.repository.CourseRepository;
import sec.project.repository.UserRepository;

@Controller
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/course/enroll", method = RequestMethod.GET)
    public String loadForm(Model model, Authentication authentication) {
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("fullname", authentication.getName());
        return "enroll";
    }

    @RequestMapping(value = "/course/enroll", method = RequestMethod.POST)
    public String submitForm(Authentication authentication, RedirectAttributes redirectAttributes, @RequestParam Long courseId) {
        User user = userRepository.getUserByUsername(authentication.getName());
        Course course = courseRepository.findOne(courseId);
        if (course.hasParticipant(user)) {
            return "redirect:/";
        } else {
            course.addParticipant(user);
            courseRepository.save(course);
            redirectAttributes.addFlashAttribute("successMessage", "Enrolled!");
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/course/enrollments", method = RequestMethod.GET)
    public String viewEnrollments(Authentication authentication, Model model) {
        User user = userRepository.getUserByUsername(authentication.getName());
        model.addAttribute("fullname", user.getFullname());
        model.addAttribute("courses", user.getCourses());
        return "enrollments";
    }

    @RequestMapping(value = "/admin/course/enrollments", method = RequestMethod.GET)
    public String getSignups(Authentication authentication, Model model) {
        return "admin/enrollments";
    }


}
