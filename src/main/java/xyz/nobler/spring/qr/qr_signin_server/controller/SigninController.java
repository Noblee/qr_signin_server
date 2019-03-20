package xyz.nobler.spring.qr.qr_signin_server.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import xyz.nobler.spring.qr.qr_signin_server.QrSigninServerApplication;
import xyz.nobler.spring.qr.qr_signin_server.Repository.CourseRepository;
import xyz.nobler.spring.qr.qr_signin_server.Repository.SigninActivityRepository;
import xyz.nobler.spring.qr.qr_signin_server.Repository.SigninRepository;
import xyz.nobler.spring.qr.qr_signin_server.Repository.StudentRepository;
import xyz.nobler.spring.qr.qr_signin_server.entity.*;
import xyz.nobler.spring.qr.qr_signin_server.exception.ExceptionData;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
public class SigninController {
    private final SigninActivityRepository signinActivityRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final SigninRepository signinRepository;
    private final RestTemplate restTemplate;
    Gson gson = new Gson();

    @Autowired
    public SigninController(SigninActivityRepository signinActivityRepository, StudentRepository studentRepository, CourseRepository courseRepository, SigninRepository signinRepository, RestTemplate restTemplate) {
        this.signinActivityRepository = signinActivityRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.signinRepository = signinRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * 创建签到
     */
    @GetMapping( value = "/admin/signin/create", produces = MediaType.IMAGE_JPEG_VALUE )
    public byte[] createSignActivity(HttpSession session, Integer courseid, Integer duration) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        Course course = courseRepository.getOne(courseid);
        if (!course.getUsername().equals(teacher.getUsername())) {
            throw new ExceptionData("不能开展其他老师课程的签到活动。");
        }
        Date date = new Date();
        String keycode = Integer.toString((int) (100000 * Math.random()));
        //新建签到
        SigninActivity signinActivity = new SigninActivity(courseid, date, keycode, duration);
        signinActivity = signinActivityRepository.save(signinActivity);

        //获取二维码
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + QrSigninServerApplication.access_token;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scene", signinActivity.getSigninid() + "&" + courseid + "&" + keycode);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(paramMap, headers);
        ResponseEntity<byte[]> entity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);
        if (Objects.requireNonNull(entity.getBody()).length < 200) {
            updateAccessToken();
        }
        url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + QrSigninServerApplication.access_token;
        entity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);
        if (Objects.requireNonNull(entity.getBody()).length < 200) {
            throw new ExceptionData("获取二维码出错。");
        }
        List<Student> students = studentRepository.findStudentsByCourseid(courseid);
        for (Student student : students) {
            signinRepository.save(new Signin(student.getStudentid(), courseid, signinActivity.getSigninid(), 0));
        }
        //返回二维码
        return entity.getBody();
    }

    /**
     * 创建签到
     */
    @GetMapping( value = "/admin/signin/create1" )
    public String createSignActivity1(HttpSession session, Integer courseid, Integer duration) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        Course course = courseRepository.getOne(courseid);
        if (!course.getUsername().equals(teacher.getUsername())) {
            throw new ExceptionData("不能开展其他老师课程的签到活动。");
        }
        Date date = new Date();
        String keycode = Integer.toString((int) (100000 * Math.random()));
        //新建签到
        SigninActivity signinActivity = new SigninActivity(courseid, date, keycode, duration);
        signinActivity = signinActivityRepository.save(signinActivity);
        List<Student> students = studentRepository.findStudentsByCourseid(courseid);
        for (Student student : students) {
            signinRepository.save(new Signin(student.getStudentid(), courseid, signinActivity.getSigninid(), 0));
        }
        return signinActivity.getSigninid() + "&" + courseid + "&" + keycode;
    }


    @GetMapping( value = "/signin" )
    public boolean signin(Integer signinid, String studentid, Integer courseid, String name, String key, String code) {
        Student student;
        StudentMultiKey studentMultiKey = new StudentMultiKey(studentid, courseid);
        try {
            student = studentRepository.findById(studentMultiKey).get();
        } catch (Exception e) {
            throw new ExceptionData("输入的学号和数据库中的不匹配。");
        }
        if (!student.getName().equals(name)) {
            throw new ExceptionData("输入的姓名和数据库中的不匹配。");
        }
        SigninActivity signinActivity = signinActivityRepository.findById(signinid).get();
        if (!signinActivity.getKeycode().equals(key)) {
            throw new ExceptionData("有效码不正确。");
        }
        Date date = new Date();
        System.out.println(date.getTime() - signinActivity.getStarttime().getTime());
        if (date.getTime() - signinActivity.getStarttime().getTime() > signinActivity.getDuration() * 60000) {
            throw new ExceptionData("签到已经结束。");
        }
        ResponseEntity<String> stringResponseEntity = restTemplate.getForEntity("https://api.weixin.qq" +
                        ".com/sns/jscode2session?appid={1}&secret={2}&js_code={3}&grant_type=authorization_code",
                String.class, QrSigninServerApplication.appid, QrSigninServerApplication.secret, code);
        OpenIdBody openIdBody = gson.fromJson(stringResponseEntity.getBody(), OpenIdBody.class);
        String openid = openIdBody.getOpenid();
        if (openid == null) {
            throw new ExceptionData("获取openid失败，请检查code值是否正确。");
        }
        List<Student> students = studentRepository.findStudentsByCourseidAndOpenid(courseid, openid);
        if (students.size() == 1) {
            Student temp = students.get(0);
            if ((!temp.getStudentid().equals(studentid)) || (!temp.getName().equals(name))) {
                throw new ExceptionData("此次输入的信息与此课程第一次签到绑定的学号和姓名不符。");
            }
        }
        if (student.getOpenid() != null && !student.getOpenid().equals(openid)) {
            throw new ExceptionData("openid不正确，不能替别人签到。");
        } else if (students.size() == 0) { //只有第一次绑定信息。
            student.setOpenid(openid);
            studentRepository.save(student);
        }
        Signin signin = new Signin(studentid, courseid, signinid, 1);
        signinRepository.save(signin);
        return true;
    }

    public void updateAccessToken() {
        ResponseEntity<String> stringResponseEntity = restTemplate.getForEntity("https://api.weixin.qq" +
                        ".com/cgi-bin/token?grant_type=client_credential&appid={1}&secret={2}",
                String.class, QrSigninServerApplication.appid, QrSigninServerApplication.secret);
        Access_tokenBody accessTokenBody = gson.fromJson(stringResponseEntity.getBody(), Access_tokenBody.class);
        QrSigninServerApplication.access_token = accessTokenBody.getAccess_token();
    }


    @PostMapping( value = "/admin/signin/getsignins" )
    public SigninInfoDTO getSignins(HttpSession session, Integer courseid, Integer signinid,
                                    Integer page, Integer limit, String studentid, Integer status) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        Course course = courseRepository.getOne(courseid);
        if (!course.getUsername().equals(teacher.getUsername())) {
            throw new ExceptionData("不能开展其他老师课程的签到信息。");
        }
        List<SigninInfo> signinInfos = new ArrayList<>();
        //获取时的该课程人数
        Integer total = studentRepository.countStudentsByCourseid(courseid);
        List<Student> students = studentRepository.findStudentsByCourseid(courseid);
        for (Student student : students) {
            if (studentid != null && (!student.getStudentid().startsWith(studentid))) {
                continue;
            }
            SigninInfo signinInfo = new SigninInfo();
            signinInfo.setStudentid(student.getStudentid());
            signinInfo.setStudentname(student.getName());
            if (signinRepository.existsById(new SigninMultiKey(student.getStudentid(), courseid, signinid))) {
                Signin signin =
                        signinRepository.findById(new SigninMultiKey(student.getStudentid(), courseid, signinid)).get();
                if (status != null && (!signin.getStatus().equals(status))) {
                    continue;
                }
                signinInfo.setStatus(signin.getStatus());

            } else {
                if (status != null && (!status.equals(2))) {
                    continue;
                }
                //当时还没有加入课程的同学
                signinInfo.setStatus(2);
            }
            signinInfos.add(signinInfo);
        }
        List<SigninInfo> subresult = signinInfos.subList((page - 1) * limit, Math.min(page * limit, signinInfos.size()));
        return new SigninInfoDTO(subresult, total);

    }

    @GetMapping( "/admin/signin/resetsignin" )
    public boolean resetSignin(HttpSession session, Integer courseid, Integer signinid, String studentid) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        Course course = courseRepository.getOne(courseid);
        if (!course.getUsername().equals(teacher.getUsername())) {
            throw new ExceptionData("不能修改其他老师课程的签到信息。");
        }
        Signin signin;
        try {
            signin = signinRepository.findById(new SigninMultiKey(studentid, courseid, signinid)).get();
        } catch (Exception e) {
            throw new ExceptionData("没有此条签到信息");
        }
        if (signin.getStatus() == 1) {
            signin.setStatus(0);
        } else {
            signin.setStatus(1);
        }
        signinRepository.save(signin);
        return true;
    }


}

class SigninInfo {
    private String studentid;
    private String studentname;
    private Integer status;

    public SigninInfo(String studentid, String studentname, Integer status) {
        this.studentid = studentid;
        this.studentname = studentname;
        this.status = status;
    }

    public SigninInfo() {
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

class SigninInfoDTO {
    private List<SigninInfo> signins;
    private Integer total;


    public SigninInfoDTO(List<SigninInfo> signins, Integer total) {
        this.signins = signins;
        this.total = total;
    }

    public SigninInfoDTO() {
    }

    public List<SigninInfo> getSignins() {
        return signins;
    }

    public void setSignins(List<SigninInfo> signins) {
        this.signins = signins;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}