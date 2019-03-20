package xyz.nobler.spring.qr.qr_signin_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nobler.spring.qr.qr_signin_server.Repository.TeacherRepository;
import xyz.nobler.spring.qr.qr_signin_server.entity.Teacher;
import xyz.nobler.spring.qr.qr_signin_server.exception.ExceptionData;

import javax.servlet.http.HttpSession;

@RestController
public class TeacherController {
    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @PostMapping( "/admin/user/changeinfo" )
    public Teacher changeInfo(HttpSession session, Teacher teacher) {
        Teacher userdata = (Teacher) session.getAttribute("teacher");
        if (teacher.getAddress() != null) {
            userdata.setAddress(teacher.getAddress());
        }
        if (teacher.getName() != null) {
            userdata.setName(teacher.getName());
        }
        if (teacher.getPassword() != null) {
            if (teacher.getPassword().length() < 9) {
                throw new ExceptionData("密码需要10位以上");
            }
            userdata.setPassword(teacher.getPassword());

            session.invalidate(); //重新登录
        }
        if (teacher.getPhonenum() != null) {
            userdata.setPhonenum(teacher.getPhonenum());
        }
        teacherRepository.save(userdata);
        if (teacher.getPassword() == null) {
            session.setAttribute("teacher", userdata);
        }
        return new Teacher(userdata);
    }


    @GetMapping( "/admin/user/getinfo" )
    public Teacher getInfo(HttpSession session) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        return new Teacher(teacher);
    }

}
