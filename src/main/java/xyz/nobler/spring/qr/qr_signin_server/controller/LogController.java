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
public class LogController {
    private final TeacherRepository teacherRepository;

    @Autowired
    public LogController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @PostMapping( "/login" )
    public Teacher login(HttpSession session, Teacher teacher) {

        Teacher userdata;
        try {
            userdata = teacherRepository.findById(teacher.getUsername()).get();
        } catch (Exception e) {
            throw new ExceptionData("此账号不存在，请注册。");

        }
        if (!teacher.getPassword().equals(userdata.getPassword())) {
            throw new ExceptionData("密码不正确。");
        }
        session.setAttribute("teacher", userdata);

        return new Teacher(userdata);
    }

    @PostMapping( "/signup" )
    public Teacher signup(Teacher teacher) {
        if (teacher.getPassword().length() <= 9) {
            throw new ExceptionData("密码需要10位以上");
        }
        try {
            teacherRepository.findById(teacher.getUsername()).get();
        } catch (Exception e) {
            teacherRepository.save(teacher);
            teacher.setPassword("");
            return teacher;
        }
        throw new ExceptionData("账号已经注册了，或更换用户名。");
    }

    @GetMapping( "/quit" )
    public boolean quit(HttpSession session) {
        session.invalidate();
        return true;
    }

}
