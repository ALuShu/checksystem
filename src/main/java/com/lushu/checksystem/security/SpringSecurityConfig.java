package com.lushu.checksystem.security;

import com.lushu.checksystem.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private MyAuthenticationSuccessHandler successHandler;
    private MyAuthenticationFailureHandler failureHandler;
    private UserServiceImpl userService;

    public SpringSecurityConfig(MyAuthenticationSuccessHandler successHandler, MyAuthenticationFailureHandler failureHandler, UserServiceImpl userService) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
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
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                /**
                 * 1. 多URL的坑: 规则是按顺序的，如果把"/**"写一开始，那么后面的"/a/**"将不会生效
                 * 2. "?"代表单字符，"*"代表0到多个字符，"**"代表0到多个目录
                 * 3. 关于Role和Authority: 如果要hasRole判断，在loadUserByUsername方法中放入List<GrantedAuthority>
                 *    中就需要"ROLE_xx"; 如果要hasAuthority判断，则不用"ROLE_"前缀； 因为这个写一点血的教训，曾因为数据
                 *    库role表的字段没加"ROLE_"前缀导致一直用不了hasRole方法，反而是permission表的字段没有限制
                 */
                .antMatchers("/student/**").hasAnyRole("STUDENT","ADMIN")
                .antMatchers("/teacher/**").hasAnyRole("TEACHER","ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/public/login")
                .loginProcessingUrl("/public/loginCheck")
                .failureHandler(failureHandler)
                .successHandler(successHandler)
                .permitAll()
            .and()
                .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/public/login")
                .permitAll()
            .and()
                //开放给前端的页面<iframe>引用
                .headers().frameOptions().sameOrigin()
            .and()
                .rememberMe()/* 有点问题，无法记住用户，待维护*/
                .tokenValiditySeconds(60*60*24)
            .and()
                .csrf().disable()
                .sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/public/login");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //坑，这里是把layui放在src/main/resources/static下面，而且直接"/static/**"行不通，除非你静态文件就放在static下面
        web.ignoring().antMatchers("/css/**","/js/**","/layui/**","/image/**","/webjars/**","/pdf/**");
    }
}
