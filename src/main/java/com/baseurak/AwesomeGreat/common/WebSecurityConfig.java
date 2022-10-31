package com.baseurak.AwesomeGreat.common;

import com.baseurak.AwesomeGreat.security.CustomAuthenticationProvider;
import com.baseurak.AwesomeGreat.user.CustomOAuth2UserService;
import com.baseurak.AwesomeGreat.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * 스프링 시큐리티 설정입니다.
 * @author Ru, Uju
 */
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) { // 4
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                //회원
                .antMatchers(HttpMethod.GET,"/user**").permitAll()
                .antMatchers(HttpMethod.POST,"/user**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/user").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/login**").permitAll()
                //게시글
                .antMatchers(HttpMethod.GET, "/post**").permitAll()
                .antMatchers(HttpMethod.GET, "/post/**").permitAll()
                .antMatchers(HttpMethod.POST, "/post**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/post/**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/post**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/random").permitAll()
                .antMatchers(HttpMethod.GET, "/search").permitAll()
                //댓글
                .antMatchers(HttpMethod.GET, "/comment/**").permitAll()
                .antMatchers(HttpMethod.POST, "/comment**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/comment/**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/comment**").hasAnyAuthority("USER", "ADMIN")
                //경험치
                .antMatchers("/experience/**").permitAll()
                .anyRequest().authenticated()
        .and()
                .csrf().disable();
        http
                .cors().configurationSource(corsConfigurationSource());
        http
                .formLogin()
                .loginPage("/login")
                .permitAll();
        http
                .logout()
                .deleteCookies("JSESSIONID")
                .permitAll();

        http
                .oauth2Login()
                .defaultSuccessUrl("http://localhost:8080")
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
//        http
//                .rememberMe()
//                .tokenValiditySeconds(604800);
        http
                .sessionManagement().maximumSessions(10);

    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://www.awesomegreat.kro.kr"));
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}