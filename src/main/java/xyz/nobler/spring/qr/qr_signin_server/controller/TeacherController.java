package xyz.nobler.spring.qr.qr_signin_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nobler.spring.qr.qr_signin_server.Repository.TeacherRepository;
import xyz.nobler.spring.qr.qr_signin_server.entity.Teacher;

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
        userdata.setAddress(teacher.getAddress());
        userdata.setName(teacher.getName());
        userdata.setPassword(teacher.getPassword());
        userdata.setPhonenum(teacher.getPhonenum());
        teacherRepository.save(userdata);
        session.setAttribute("teacher", userdata);
        return userdata;
    }


    @GetMapping( "/admin/user/getinfo")
    public Teacher getInfo(HttpSession session) {
        return (Teacher) session.getAttribute("teacher");
    }

}
