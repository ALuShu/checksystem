package com.lushu.checksystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/30
 * @throws
 * @since
 */
@Slf4j
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Map<String,Object> map = new HashMap<>();
        response.setStatus(401);
        if (e instanceof BadCredentialsException) {
            map.put("status",HttpStatus.UNAUTHORIZED.value());
            map.put("exception",e.getMessage());
            map.put("msg","账号或密码不匹配");
            response.getWriter().write(objectMapper.writeValueAsString(map));
            response.setContentType("text/html;charset=UTF-8");

        }else if (e instanceof UsernameNotFoundException){
            map.put("status",HttpStatus.UNAUTHORIZED.value());
            map.put("exception",e.getMessage());
            map.put("msg","没有此用户");
            response.getWriter().write(objectMapper.writeValueAsString(map));
            response.setContentType("text/html;charset=UTF-8");

        }else if (e instanceof AccountStatusException){
            map.put("status",HttpStatus.FORBIDDEN.value());
            map.put("exception",e.getMessage());
            map.put("msg","账号已锁定");
            response.getWriter().write(objectMapper.writeValueAsString(map));
            response.setContentType("text/html;charset=UTF-8");

        }else {
            log.error("unset exception:"+e.toString());
            map.put("status",0);
            map.put("exception",e.getMessage());
            map.put("msg","未知错误，请联系开发者");
            response.getWriter().write(objectMapper.writeValueAsString(map));
            response.setContentType("text/html;charset=UTF-8");
        }

    }
}
