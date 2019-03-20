package xyz.nobler.spring.qr.qr_signin_server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.nobler.spring.qr.qr_signin_server.entity.Signin;
import xyz.nobler.spring.qr.qr_signin_server.entity.SigninMultiKey;

import java.util.List;

public interface SigninRepository extends JpaRepository<Signin, SigninMultiKey> {
    List<Signin> findSigninsByCourseidAndSigninid(Integer courseid, Integer signinid);

    Integer countSigninsByCourseidAndStudentidAndStatus(Integer courseid, String studentid, Integer status);

    Integer countSigninsByCourseidAndSigninidAndStatus(Integer courseid, Integer signinid, Integer status);
}
 