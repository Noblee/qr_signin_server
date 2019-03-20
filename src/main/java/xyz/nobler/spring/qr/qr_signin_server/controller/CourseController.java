package xyz.nobler.spring.qr.qr_signin_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.nobler.spring.qr.qr_signin_server.Repository.CourseRepository;
import xyz.nobler.spring.qr.qr_signin_server.Repository.StudentRepository;
import xyz.nobler.spring.qr.qr_signin_server.entity.Course;
import xyz.nobler.spring.qr.qr_signin_server.entity.Student;
import xyz.nobler.spring.qr.qr_signin_server.entity.StudentMultiKey;
import xyz.nobler.spring.qr.qr_signin_server.entity.Teacher;
import xyz.nobler.spring.qr.qr_signin_server.exception.ExceptionData;

import javax.persistence.Id;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CourseController {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }


    @GetMapping( "/admin/course/addcourse" )
    public Course addCourse(HttpSession session, Course course) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        course.setUsername(teacher.getUsername());
        try {
            course = courseRepository.save(course);
        } catch (Exception e) {
            throw new ExceptionData("课程的各类信息不能为空。");
        }
        return course;
    }

    @PostMapping( value = "/admin/course/addstudents", consumes = "application/json; charset=utf-8" )
    public boolean addStudents(HttpSession session, Integer courseid, @RequestBody List<Student> students) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        Course course = courseRepository.getOne(courseid);
        if (!course.getUsername().equals(teacher.getUsername())) {
            throw new ExceptionData("不能向其他老师的课程添加学生。");
        }
        for (Student s : students) {
            s.setCourseid(courseid);
            studentRepository.save(s);
        }

        return true;
    }


    @GetMapping( value = "/admin/course/getcourses" )
    public CoursesDTO getCourses(HttpSession session, Integer page, Integer limit) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        List<Course> courses = courseRepository.findCoursesByUsername(teacher.getUsername());
        List<Course> subcourses = courses.subList((page - 1) * limit, Math.min(page * limit, courses.size()));
        return new CoursesDTO(subcourses, courses.size());
    }


    @PostMapping( "/admin/course/getstudents" )
    public StudentsDTO getStudents(HttpSession session, Integer courseid, Integer page, Integer limit, String studentid, String newid, String newname) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        Course course = courseRepository.getOne(courseid);
        if (!course.getUsername().equals(teacher.getUsername())) {
            throw new ExceptionData("不能获取其他老师的学生。");
        }
        if (newid != null && newname != null) {
            studentRepository.save(new Student(newid, courseid, newname, null));
        }
        List<Student> students = studentRepository.findStudentsByCourseid(courseid);
        if (studentid != null) {
            List<Student> result = new ArrayList<>();
            for (Student s : students) {
                if (s.getStudentid().startsWith(studentid)) {
                    result.add(s);
                }
            }
            List<Student> subcourses = result.subList((page - 1) * limit, Math.min(page * limit, result.size()));
            return new StudentsDTO(subcourses, studentRepository.countStudentsByCourseid(courseid), courseid);
        } else {
            List<Student> subcourses = students.subList((page - 1) * limit, Math.min(page * limit, students.size()));
            return new StudentsDTO(subcourses, studentRepository.countStudentsByCourseid(courseid), courseid);
        }

    }


    @GetMapping( "/admin/course/deletecourse" )
    public boolean deleteCourse(HttpSession session, Integer courseid) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        Course course = courseRepository.getOne(courseid);
        if (!course.getUsername().equals(teacher.getUsername())) {
            throw new ExceptionData("不能删除其他老师的课程。");
        }
        try {
            courseRepository.deleteById(courseid);
        } catch (Exception e) {
            throw new ExceptionData("删除所提供的courseid不合法。");
        }
        return true;
    }

    @GetMapping( "/admin/course/deletestudent" )
    public boolean deleteCourse(HttpSession session, Integer courseid, String studentid) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        Course course = courseRepository.getOne(courseid);
        if (!course.getUsername().equals(teacher.getUsername())) {
            throw new ExceptionData("不能删除其他老师课程的学生。");
        }
        try {
            studentRepository.deleteById(new StudentMultiKey(studentid, courseid));
        } catch (Exception e) {
            throw new ExceptionData("删除所提供的信息不合法。");
        }
        return true;
    }


}

class StudentDTO {
    private String studentid;
    private String name;

    public StudentDTO() {
    }

    public StudentDTO(Student student) {
        this.studentid = student.getStudentid();
        this.name = student.getName();
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class StudentsDTO {
    private List<StudentDTO> studentDTOs;
    private Integer total;
    private Integer courseid;

    public StudentsDTO(List<Student> students, Integer total, Integer courseid) {
        List<StudentDTO> studentDTOS = new ArrayList<>();
        for (Student student : students) {
            studentDTOS.add(new StudentDTO(student));
        }
        studentDTOs = studentDTOS;
        this.total = total;
        this.courseid = courseid;
    }


    public StudentsDTO() {
    }

    public List<StudentDTO> getStudentDTOs() {
        return studentDTOs;
    }

    public Integer getCourseid() {
        return courseid;
    }

    public void setCourseid(Integer courseid) {
        this.courseid = courseid;
    }

    public void setStudentDTOs(List<StudentDTO> studentDTOs) {
        this.studentDTOs = studentDTOs;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}

class CoursesDTO {
    private List<Course> courses;
    private Integer total;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public CoursesDTO(List<Course> courses, Integer total) {
        this.courses = courses;
        this.total = total;
    }
}