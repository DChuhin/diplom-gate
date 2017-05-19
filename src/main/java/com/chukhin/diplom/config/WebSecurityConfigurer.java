package com.chukhin.diplom.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**
 * The Web Security configuration for My Website.
 */
@Configuration
@EnableOAuth2Sso
@EnableWebMvc
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()   // Allow navigating to index page,
                .anyRequest().authenticated();  // but secure all the other URLs
    }

    @Bean
    public OAuth2RestOperations restOperations(
            OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails, OAuth2ClientContext oauth2ClientContext) {
        return new OAuth2RestTemplate(oAuth2ProtectedResourceDetails, oauth2ClientContext);
    }
}
