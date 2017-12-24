package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import java.util.List;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(Authentication authentication, @RequestParam String name, @RequestParam String address, @RequestParam String course) {

        signupRepository.save(new Signup(authentication.getName(), name, address, course));
        return "done";
    }

    @RequestMapping(value = "signups", method = RequestMethod.GET)
    public String getSignups(Authentication authentication, Model model) {
        List<Signup> signups = signupRepository.getAllByUserId(authentication.getName());
        model.addAttribute("signups", signups);
        return "signups";
    }


}
