package com.lushu.checksystem.exception;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @author ALuShu
 * @Description
 * @date 2020/2/14
 * @throws
 * @since
 */
@Configuration
public class ErrorPageHandler {

    @Bean
    public WebServerFactoryCustomizer webServerFactoryCustomizer(){
        return new WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>() {

            @Override
            public void customize(ConfigurableServletWebServerFactory factory) {
                factory.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN,"/public/perError"));
                factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/public/serError"));
            }
        };
    }
}
