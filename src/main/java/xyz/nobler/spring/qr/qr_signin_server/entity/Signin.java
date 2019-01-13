package xyz.nobler.spring.qr.qr_signin_server.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
@Entity
public class Signin implements Serializable {
    @Id
    String studentid;
    @Id
    Integer courseid;
    @Id
    Integer signinid;
    Integer status;

    public Signin() {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
