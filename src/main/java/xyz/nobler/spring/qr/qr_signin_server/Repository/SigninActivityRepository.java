package xyz.nobler.spring.qr.qr_signin_server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.nobler.spring.qr.qr_signin_server.entity.SigninActivity;

import java.util.List;

public interface SigninActivityRepository extends JpaRepository<SigninActivity, Integer> {
    List<SigninActivity> findByCourseid(Integer courseid);

    Integer countSigninActivitiesByCourseid(Integer courseid);

}
