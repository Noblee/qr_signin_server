package xyz.nobler.spring.qr.qr_signin_server.entity;

import java.io.Serializable;

public class SigninMultiKey implements Serializable {
    String studentid;
    Integer courseid;
    Integer signinid;

    public SigninMultiKey() {
    }

    public SigninMultiKey(String studentid, Integer courseid, Integer signinid) {
        this.studentid = studentid;
        this.courseid = courseid;
        this.signinid = signinid;
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

    public Integer getSigninid() {
        return signinid;
    }

    public void setSigninid(Integer signinid) {
        this.signinid = signinid;
    }
}
