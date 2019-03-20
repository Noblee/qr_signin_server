package xyz.nobler.spring.qr.qr_signin_server.aspect;


import org.springframework.stereotype.Component;
import xyz.nobler.spring.qr.qr_signin_server.exception.ExceptionData;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter( filterName = "Authority", urlPatterns = {"/*"} )
@Component
public class Authority implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        String path = request.getServletPath();
        System.out.println(path);
        if (session.getAttribute("teacher") != null) {
            chain.doFilter(req, resp);
        } else if (!(path.startsWith("/admin/"))) {
            chain.doFilter(req, resp);
        } else {
            throw new ExceptionData("您没有权限访问此页面，请登录。");
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }
}
