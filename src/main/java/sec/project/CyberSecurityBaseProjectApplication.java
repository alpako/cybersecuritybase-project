package sec.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import sec.project.domain.Course;
import sec.project.domain.User;
import sec.project.repository.CourseRepository;
import sec.project.repository.UserRepository;

import java.util.HashSet;

@SpringBootApplication
public class CyberSecurityBaseProjectApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(CyberSecurityBaseProjectApplication.class);
    }

    @Bean
    public CommandLineRunner init(UserRepository userRepository, CourseRepository courseRepository) {
        return (args) -> {
            User user = new User("admin", passwordEncoder.encode("1234"), "The administrator", "Secret Address",
                    new HashSet<>());
            userRepository.save(user);

            for (int i = 0; i < 10; i++) {
                user = new User("user" + i, passwordEncoder.encode("1234"), "User Name " + i,
                        "World Street " + 1, new HashSet<>());
                userRepository.save(user);
                Course course = new Course("my security course " + i, new HashSet<>());
                courseRepository.save(course);
                course.addParticipant(user);
                courseRepository.save(course);
            }

        };
    }
}
