package xyz.nobler.spring.qr.qr_signin_server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.nobler.spring.qr.qr_signin_server.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findCoursesByUsername(String username);
}
