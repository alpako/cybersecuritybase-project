package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String enroll(Authentication authentication, RedirectAttributes redirectAttributes, @RequestParam Long courseId) {
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

    @RequestMapping(value = "/course/disenroll/{key}", method = RequestMethod.POST)
    public String disenroll(Authentication authentication, RedirectAttributes redirectAttributes, @PathVariable(value = "key") Long courseId) {
        User user = userRepository.getUserByUsername(authentication.getName());
        Course course = courseRepository.findOne(courseId);

        if (course.hasParticipant(user)) {
            course.deleteParticipant(user);
            courseRepository.save(course);
            redirectAttributes.addFlashAttribute("successMessage", "Disenrolled!");
            return "redirect:/";
        } else {
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

    @RequestMapping(value = "/course/disenrollUser/{course}/{user}", method = RequestMethod.POST)
    public String disenrollUser(RedirectAttributes redirectAttributes,
                                @PathVariable(value = "course") Long courseId,
                                @PathVariable(value = "user") Long userId) {
        User user = userRepository.findOne(userId);
        Course course = courseRepository.findOne(courseId);

        if (user != null && course != null && course.hasParticipant(user)) {
            course.deleteParticipant(user);
            courseRepository.save(course);
            redirectAttributes.addFlashAttribute("successMessage", "Disenrolled User!");
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }


    @RequestMapping(value = "/admin/course/enrollments", method = RequestMethod.GET)
    public String getSignups(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "admin/enrollments";
    }


}
