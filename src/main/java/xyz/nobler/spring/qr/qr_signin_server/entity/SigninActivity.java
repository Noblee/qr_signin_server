package xyz.nobler.spring.qr.qr_signin_server.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class SigninActivity implements Serializable {
    @Id
    @GeneratedValue
    private Integer signinid;
    private Integer courseid;
    @Temporal( value = TemporalType.TIMESTAMP )
    private Date starttime;
    private String keycode;
    private Integer duration;


    public SigninActivity() {
    }

    public SigninActivity(Integer courseid, Date starttime, String keycode, Integer duration) {
        this.courseid = courseid;
        this.starttime = starttime;
        this.keycode = keycode;
        this.duration = duration;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public String getKeycode() {
        return keycode;
    }

    public void setKeycode(String keycode) {
        this.keycode = keycode;
    }
}
