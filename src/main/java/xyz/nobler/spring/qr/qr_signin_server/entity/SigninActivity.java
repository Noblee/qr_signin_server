package xyz.nobler.spring.qr.qr_signin_server.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
@Entity
public class SigninActivity implements Serializable{
    @Id
    private Integer signinid;
    private Integer courseid;
    private String datetime;

    public SigninActivity() {
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
