package xyz.nobler.spring.qr.qr_signin_server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class ExceptionData extends RuntimeException {
    public ExceptionData(String msg) {
        super(msg);
    }
}