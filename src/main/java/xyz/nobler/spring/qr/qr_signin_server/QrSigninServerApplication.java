package xyz.nobler.spring.qr.qr_signin_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class QrSigninServerApplication {
    public static final String appid = "wxcc6e3ac0912b7744";
    public static final String secret = "cf829271a90dfefaeba02d9b637b20d9";
    public static String access_token = "";

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(QrSigninServerApplication.class, args);
    }

}

