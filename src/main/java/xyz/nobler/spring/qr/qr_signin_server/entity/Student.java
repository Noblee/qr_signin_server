package xyz.nobler.spring.qr.qr_signin_server.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@IdClass( StudentMultiKey.class )
public class Student implements Serializable {
    @Id
    private String studentid;
    @Id
    private Integer courseid;
    @NotNull
    private String name;
    private String openid;

    public Student() {
    }

    public Student(String studentid, Integer courseid, String name, String openid) {
        this.studentid = studentid;
        this.courseid = courseid;
        this.name = name;
        this.openid = openid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}


