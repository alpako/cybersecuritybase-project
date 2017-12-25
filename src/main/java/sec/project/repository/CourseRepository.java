package sec.project.repository;

import org.springframework.data.repository.CrudRepository;
import sec.project.domain.Course;


public interface CourseRepository extends CrudRepository<Course, Long> {
}
