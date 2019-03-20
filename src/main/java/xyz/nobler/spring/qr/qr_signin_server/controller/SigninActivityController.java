package xyz.nobler.spring.qr.qr_signin_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nobler.spring.qr.qr_signin_server.Repository.CourseRepository;
import xyz.nobler.spring.qr.qr_signin_server.Repository.SigninActivityRepository;
import xyz.nobler.spring.qr.qr_signin_server.Repository.SigninRepository;
import xyz.nobler.spring.qr.qr_signin_server.Repository.StudentRepository;
import xyz.nobler.spring.qr.qr_signin_server.entity.Course;
import xyz.nobler.spring.qr.qr_signin_server.entity.SigninActivity;
import xyz.nobler.spring.qr.qr_signin_server.entity.Student;
import xyz.nobler.spring.qr.qr_signin_server.entity.Teacher;
import xyz.nobler.spring.qr.qr_signin_server.exception.ExceptionData;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
public class SigninActivityController {
    private final SigninActivityRepository signinActivityRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final SigninRepository signinRepository;

    @Autowired
    public SigninActivityController(SigninActivityRepository signinActivityRepository, CourseRepository courseRepository, StudentRepository studentRepository, SigninRepository signinRepository) {
        this.signinActivityRepository = signinActivityRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.signinRepository = signinRepository;
    }

    @GetMapping( "/admin/signinactivity/getsigninactivities" )
    public List<SigninActivityDTO> getSigninActivities(HttpSession session, Integer courseid) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        Course course = courseRepository.getOne(courseid);
        if (!course.getUsername().equals(teacher.getUsername())) {
            throw new ExceptionData("不能查看其他老师课程的签到内容。");
        }
        List<SigninActivity> signinActivities = signinActivityRepository.findByCourseid(courseid);
        List<SigninActivityDTO> signinActivityDTOs = new ArrayList<>();
        Integer size = studentRepository.countStudentsByCourseid(courseid);
        for (SigninActivity signinActivity : signinActivities) {
            Integer signinNum = signinRepository.countSigninsByCourseidAndSigninidAndStatus
                    (courseid, signinActivity.getSigninid(), 1);
            signinActivityDTOs.add(new SigninActivityDTO(signinActivity, size, signinNum));
        }
        return signinActivityDTOs;
    }


    @GetMapping( "/admin/signinactivity/export" )
    public StudentsSigninInfoDTO export(HttpSession session, Integer courseid) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        Course course = courseRepository.getOne(courseid);
        if (!course.getUsername().equals(teacher.getUsername())) {
            throw new ExceptionData("不能导出其他老师课程的学生签到情况。");
        }
        StudentsSigninInfoDTO studentsSigninInfoDTO = new StudentsSigninInfoDTO();
        List<StudentSigninInfo> studentSigninInfos = new ArrayList<>();
        Integer totalSigninTime = signinActivityRepository.countSigninActivitiesByCourseid(courseid);
        List<Student> students = studentRepository.findStudentsByCourseid(courseid);
        for (Student student : students) {
            StudentSigninInfo studentSigninInfo = new StudentSigninInfo();
            studentSigninInfo.setStudentid(student.getStudentid());
            studentSigninInfo.setStudentname(student.getName());
            Integer signintime = signinRepository.countSigninsByCourseidAndStudentidAndStatus(courseid,
                    student.getStudentid(), 1);
            studentSigninInfo.setSignintime(signintime);
            studentSigninInfos.add(studentSigninInfo);
        }
        studentsSigninInfoDTO.setStudentSigninInfos(studentSigninInfos);
        studentsSigninInfoDTO.setTotalSigninTime(totalSigninTime);
        return studentsSigninInfoDTO;
    }

    @GetMapping( "/admin/signinactivity/deletesigninactivity" )
    public boolean deleteSigninActivity(HttpSession session, Integer signid) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        SigninActivity signinActivity = signinActivityRepository.findById(signid).get();
        Course course = courseRepository.findById(signinActivity.getCourseid()).get();
        if (!course.getUsername().equals(teacher.getUsername())) {
            throw new ExceptionData("不能删除其他老师课程的签到。");
        }
        try {
            signinActivityRepository.deleteById(signid);
        } catch (Exception e) {
            throw new ExceptionData("删除失败");
        }
        return true;
    }
}

class StudentsSigninInfoDTO {
    private List<StudentSigninInfo> studentSigninInfos;
    private Integer totalSigninTime;

    public StudentsSigninInfoDTO(List<StudentSigninInfo> studentSigninInfos, Integer totalSigninTime) {
        this.studentSigninInfos = studentSigninInfos;
        this.totalSigninTime = totalSigninTime;
    }

    public StudentsSigninInfoDTO() {
    }

    public List<StudentSigninInfo> getStudentSigninInfos() {
        return studentSigninInfos;
    }

    public void setStudentSigninInfos(List<StudentSigninInfo> studentSigninInfos) {
        this.studentSigninInfos = studentSigninInfos;
    }

    public Integer getTotalSigninTime() {
        return totalSigninTime;
    }

    public void setTotalSigninTime(Integer totalSigninTime) {
        this.totalSigninTime = totalSigninTime;
    }
}

class StudentSigninInfo {
    private String studentid;
    private String studentname;
    private Integer signintime;

    public StudentSigninInfo(String studentiId, String studentName, Integer signinTime) {
        this.studentid = studentiId;
        this.studentname = studentName;
        this.signintime = signinTime;
    }

    public StudentSigninInfo() {
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public Integer getSignintime() {
        return signintime;
    }

    public void setSignintime(Integer signintime) {
        this.signintime = signintime;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }
}

class SigninActivityDTO {
    private Integer signinid;
    private Integer courseid;
    private Date starttime;
    private Integer duration;
    private Integer studentnum;
    private Integer signinnum;

    public SigninActivityDTO(SigninActivity signinActivity, Integer studentnum, Integer signinnum) {
        this.signinid = signinActivity.getSigninid();
        this.courseid = signinActivity.getCourseid();
        this.starttime = signinActivity.getStarttime();
        this.duration = signinActivity.getDuration();
        this.studentnum = studentnum;
        this.signinnum = signinnum;
    }

    public Integer getSigninid() {
        return signinid;
    }

    public void setSigninid(Integer signinid) {
        this.signinid = signinid;
    }

    public Integer getCourseid() {
        return courseid;
    }

    public void setCourseid(Integer courseid) {
        this.courseid = courseid;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getStudentnum() {
        return studentnum;
    }

    public void setStudentnum(Integer studentnum) {
        this.studentnum = studentnum;
    }

    public Integer getSigninnum() {
        return signinnum;
    }

    public void setSigninnum(Integer signinnum) {
        this.signinnum = signinnum;
    }
}