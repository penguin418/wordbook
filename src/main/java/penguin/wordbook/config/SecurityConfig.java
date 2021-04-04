package penguin.wordbook.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import penguin.wordbook.filter.JwtTokenFilter;
import penguin.wordbook.service.AccountService;


/**
 * 스프링 시큐리티
 * - jwt 기반의 로그인 지원
 * - wordbook create 를 로그인된 사용자의 권한으로 제한
 */
@Controller
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AccountService accountService;
    private final JwtTokenFilter jwtTokenFilter;

    /**
     * PasswordEncoder 등록
     * - Account entity 의 password 항목을 저장, 로그인 요청 시 암호화할 때 사용됨
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring 에서 기본으로 제공하는 인증객체를 등록
     *
     * @return AuthenticationManager
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * userDetailsService 가 위에서 정의한 passwordEncoder 를 사용하도록 주입
     * TODO: 인메모리 대신 redis 로 변경할 것
     *
     * @param auth {AuthenticationManagerBuilder} AuthenticationManager (인증객체)를 생성하는 빌더
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder());
    }

    /**
     * 웹 필터링 (http 필터링 이전))
     * - js, css, image 등 static resources (js, css, images 등 static 폴더 항목) 제외
     * - swagger 관련 항목 제외
     * @param web {WebSecurity}
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .mvcMatchers("/index.html")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    /**
     * http 필터링 (스프링 시큐리티 이전)
     * - wordbook 생성 권한을 제한합니다
     *
     * @param http {HttpSecurity}
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers(HttpMethod.POST, "/wordbooks").hasRole("USER")
                .anyRequest().authenticated();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
