package xyz.nobler.spring.qr.qr_signin_server.entity;

import java.io.Serializable;

public class StudentMultiKey implements Serializable {
    private String studentid;
    private Integer courseid;

    public StudentMultiKey() {
    }

    public StudentMultiKey(String studentid, Integer courseid) {
        this.studentid = studentid;
        this.courseid = courseid;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public Integer getCourseid() {
        return courseid;
    }

    public void setCourseid(Integer courseid) {
        this.courseid = courseid;
    }
}
