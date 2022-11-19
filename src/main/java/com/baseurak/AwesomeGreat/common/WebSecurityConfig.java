package com.baseurak.AwesomeGreat.common;

import com.baseurak.AwesomeGreat.security.CustomAuthenticationProvider;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

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
                .antMatchers(HttpMethod.POST,"/user**").permitAll()
                .antMatchers(HttpMethod.GET,"/user**").permitAll()
                .antMatchers(HttpMethod.POST, "/login**").permitAll()
                //게시글
                .antMatchers(HttpMethod.GET, "/post**").permitAll()
                .antMatchers(HttpMethod.GET, "/post/**").permitAll()
                .antMatchers(HttpMethod.POST, "/post**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/post").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/post/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/post**").hasRole("USER")
                //댓글
                .antMatchers(HttpMethod.GET, "/comment/**").permitAll()
                .antMatchers(HttpMethod.POST, "/comment").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/comment/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/comment").hasRole("USER")
                .anyRequest().authenticated()
        .and()
                .csrf().disable();
        http
                .formLogin()
                .loginPage("/login")
                //.defaultSuccessUrl("http://localhost:3000/main")
                .defaultSuccessUrl("/")
                //.failureUrl("http://localhost:3000/login")
                .failureUrl("/fail")
                //.authenticationDetailsSource(formWebAuthenticationDetailsSource)
                .permitAll()
//                .successHandler((request, response, authentication) -> {
//                    RequestCache requestCache = new HttpSessionRequestCache();
//                    SavedRequest savedRequest = requestCache.getRequest(request, response);
//                    String redirectUrl = savedRequest.getRedirectUrl();
//                    response.sendRedirect(redirectUrl);
//                })
//                .authorizeRequests()
//                .antMatchers("/login", "/signup", "/home").permitAll() // 누구나 접근 허용
//                .antMatchers("/").hasRole("USER") // USER, ADMIN만 접근 가능
//                .antMatchers("/admin").hasRole("ADMIN") // ADMIN만 접근 가능
//                .anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
//                .and()
//                .formLogin()
//                .loginPage("/login") // 로그인 페이지 링크
//                .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트 주소
//                .and()
//                .logout()
//                .logoutSuccessUrl("/login") // 로그아웃 성공시 리다이렉트 주소
//                .invalidateHttpSession(true) // 세션 날리기
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userService)
                // 해당 서비스(userService)에서는 UserDetailsService를 implements해서
                // loadUserByUsername() 구현해야함 (서비스 참고)
                //.passwordEncoder(passwordEncoder());
        auth.authenticationProvider(authenticationProvider());
    }
}