package kopo.poly.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info(this.getClass().getName() + ".PasswordEncoder Start!");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info(this.getClass().getName() + " 필터 체인 시작");

        http.csrf(AbstractHttpConfigurer::disable)  // POST 방식 전송을 위해 csrf 막기
                .authorizeHttpRequests(authz -> authz // 페이지 접속 권한 설정
                        .requestMatchers("/notice/v1/**").hasAuthority("ROLE_USER") // USER 권한
                        .requestMatchers("/user/v1/**").hasAuthority("ROLE_USER") // USER 권한
                        .requestMatchers("/html/user/**").hasAuthority("ROLE_USER") // USER 권한
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN") // ADMIN 권한

                        .anyRequest().permitAll() // 그 외 나머지 URL 요청은 인증 받지 않아도 접근 가능
                )
                .formLogin(login -> login // 로그인 페이지 설정
                        .loginPage("/html/ss/login.html")
                        .loginProcessingUrl("/login/v1/loginProc")
                        .usernameParameter("userId") // 로그인 ID로 사용할 HTML의 input 객체의 name 값
                        .passwordParameter("password") // 로그인 패스워드로 사용할 HTML의 input 객체의 name 값
                        .successForwardUrl("/login/v1/loginSuccess") // Web MVC, Controller 사용할 때 적용 / 로그인 성공 URL
                        .failureForwardUrl("/login/v1/loginFail") // Web MVC, Controller 사용할 때 적용 / 로그인 실패 URL
                )
                .logout(logout -> logout
                        .logoutUrl("/user/v1/logout") // 로그아웃 요청 URL
                        .clearAuthentication(true) // 시큐리티 저장된 인증 정보 초기화
                        .invalidateHttpSession(true) // 로그인 후, 컨트롤러에서 생성한 세션 삭제
                        .logoutSuccessUrl("/html/index.html") // 로그아웃 성공 처리 URL
                );

        return http.build();
    }
}
