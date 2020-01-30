package com.lushu.checksystem.security;

import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/30
 * @throws
 * @since
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String role = userService.selectRoleByUsername(user.getUsername()).getName();
        if ("ADMIN".equals(role)) {
            response.sendRedirect("/upload");
        } else if ("TEACHER".equals(role)) {
            response.sendRedirect("/teacher");
        } else if ("STUDENT".equals(role)) {
            response.sendRedirect("/student");
        }
    }
}