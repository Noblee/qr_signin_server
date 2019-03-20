package xyz.nobler.spring.qr.qr_signin_server.Repository;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.nobler.spring.qr.qr_signin_server.entity.Student;
import xyz.nobler.spring.qr.qr_signin_server.entity.StudentMultiKey;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, StudentMultiKey> {
    List<Student> findStudentsByCourseid(Integer courseid);

    Integer countStudentsByCourseid(Integer courseid);

    List<Student> findStudentsByCourseidAndOpenid(Integer courseid, String openid);
}
