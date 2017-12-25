package sec.project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sec.project.domain.Course;
import sec.project.domain.User;
import sec.project.repository.CourseRepository;
import sec.project.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class CyberSecurityBaseProjectApplication {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(CyberSecurityBaseProjectApplication.class);
    }

    @Bean
    public CommandLineRunner init(UserRepository userRepository, CourseRepository courseRepository) {
        return (args) -> {
            User user = new User("alpako", "1234", "Al Pako", "my street",
                    new HashSet<>());
            userRepository.save(user);
            Set<User> participants = new HashSet<>();
            for (User u : userRepository.findAll()) {
                participants.add(u);
            }
            Course course = new Course("my security course", new HashSet<>());
            courseRepository.save(course);
            course.setParticipants(participants);
            courseRepository.save(course);
        };
    }
}
