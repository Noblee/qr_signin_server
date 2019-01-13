package xyz.nobler.spring.qr.qr_signin_server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nobler.spring.qr.qr_signin_server.Repository.TeacherRepository;
import xyz.nobler.spring.qr.qr_signin_server.entity.Teacher;

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

        session.setAttribute("teacher", teacher);
        Teacher userdata;
        try {
            userdata = teacherRepository.findById(teacher.getUsername()).get();
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (!teacher.getPassword().equals(userdata.getPassword())) {
            return null;
        }
        session.setAttribute("teacher", userdata);
        userdata.setPassword(null);
        return userdata;
    }
    @PostMapping( "/signup" )
    public Teacher signup(Teacher teacher) {
        try {
            teacherRepository.findById(teacher.getUsername()).get();
        } catch (Exception e) {
            teacherRepository.save(teacher);
            return teacher;
        }
        return null;
    }

    @GetMapping( "/quit" )
    public boolean quit(HttpSession session) {
        session.invalidate();
        return true;

    }
}
