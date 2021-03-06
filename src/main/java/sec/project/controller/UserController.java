package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sec.project.domain.User;
import sec.project.repository.UserRepository;

import java.util.HashSet;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/user/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    @RequestMapping(path = "/user/register", method = RequestMethod.POST)
    public String register(Model model, RedirectAttributes redirectAttributes, @RequestParam String username, @RequestParam String password,
                           @RequestParam String fullname, @RequestParam String status) {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            User newUser = new User(username, passwordEncoder.encode(password), fullname, status, new HashSet<>());
            userRepository.save(newUser);
            redirectAttributes.addFlashAttribute("successMessage", "Registration Successful.");
//            model.addAttribute("successMessage","Registration Successful.");
            return "redirect:/";
        } else {
            return "register";
        }
    }


    @RequestMapping(path = "/user/update", method = RequestMethod.GET)
    public String update(Model model, Authentication authentication) {
        User user = userRepository.getUserByUsername(authentication.getName());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("fullname", user.getFullname());
        model.addAttribute("status", user.getStatus());
        return "update";

    }

    @RequestMapping(path = "/user/update", method = RequestMethod.POST)
    public String update(@RequestParam String username, @RequestParam String password,
                         @RequestParam String fullname, @RequestParam String status) {
        User user = userRepository.getUserByUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullname(fullname);
        user.setStatus(status);
        userRepository.save(user);
        return "home";
    }

    @RequestMapping(path = "/user/view", method = RequestMethod.GET)
    public String update(Authentication authentication, Model model) {
        User user = userRepository.getUserByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @RequestMapping(path = "/user/list", method = RequestMethod.GET)
    public String userList(Authentication authentication, Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }
}
