package xyz.nobler.spring.qr.qr_signin_server.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
@Entity
public class Teacher implements Serializable {
    @Id
    private String username;
    private String password;
    private String name;
    private String phonenum;
    private String address;

    public Teacher() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
