package xyz.nobler.spring.qr.qr_signin_server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.nobler.spring.qr.qr_signin_server.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, String> {

}
