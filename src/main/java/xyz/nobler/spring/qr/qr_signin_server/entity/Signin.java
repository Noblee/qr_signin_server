package xyz.nobler.spring.qr.qr_signin_server.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
@Entity
@IdClass(SigninMultiKey.class)
public class Signin implements Serializable {
    @Id
    private String studentid;
    @Id
    private Integer courseid;
    @Id
    private Integer signinid;
    private Integer status;

    public Signin() {
    }

    public Signin(String studentid, Integer courseid, Integer signinid, Integer status) {
        this.studentid = studentid;
        this.courseid = courseid;
        this.signinid = signinid;
        this.status = status;
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
