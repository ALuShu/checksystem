package com.lushu.checksystem.config;

import com.lushu.checksystem.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/22
 * @throws
 * @since
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userService;
    public SpringSecurityConfig(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 这里是数据库匹配，但如果不是一开始在注册时就通过相同的passwordEncoder进行加密（比如在一开始就手动写了
         * root用户的密码root），登录匹配时就会报错；
         * 注册：密码在数据库里都是使用 SHA-256+随机盐+密钥 把用户输入的密码进行hash处理得到密码的hash值，且不可逆
         * 登录：由于不可逆，会把输入的密码串再用相同的加密算法得到hash值，对比数据库的hash值
         */
        auth.userDetailsService(userService)
                .passwordEncoder(new BCryptPasswordEncoder())
                .and()
            .inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("root")
                .password(new BCryptPasswordEncoder().encode("root"))
                .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/uploadFile").hasRole("ADMIN")
                .antMatchers("/sayhello").permitAll()
            .anyRequest().authenticated()
                .and()
            .formLogin().loginPage("/login").failureUrl("/loginError").permitAll()
                .and()
            .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //坑，这里是把layui放在src/main/resources/static下面
        web.ignoring().antMatchers("/css/**","/js/**","/layui/**","/excel/**","/word/**");
    }
}
