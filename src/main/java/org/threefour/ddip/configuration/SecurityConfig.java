package org.threefour.ddip.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.threefour.ddip.member.jwt.JWTUtil;
import org.threefour.ddip.member.jwt.LoginFilter;

//@Configuration 및 @EnableWebSecurity 기본 웹 보안 구성 비활성화 후 자체 정의
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  //AuthenticationManager가 인자로 받을 Au~Confing~ 객체주입
  private final AuthenticationConfiguration authenticationConfiguration;
  private final JWTUtil jwtUtil;

  //AuthenticationManager Bean 등록
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        //csrf disable session stateless기 때문
        .csrf().disable()
        //form 로그인 방식 disable (test후 지울 예정)
       //.formLogin()
            //.loginPage("/member/login")
            /*.defaultSuccessUrl("/", true)
            .failureUrl("/")*/
            /*.successHandler(new AuthenticationSuccessHandler() {
              @Override
              public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                System.out.println("authentication: " + authentication.getName());
                response.sendRedirect("/");
              }
            })*/
            //.permitAll();
    //http
        //http basic 인증 방식 disable
        .httpBasic().disable()
        //경로별 인가 작업
        .authorizeHttpRequests()
            .antMatchers("/**"/*, "/join"*/)
            .permitAll() //로그인 하지 않아도 접근 가능
            //.requestMatchers("/admin").hasRole("ADMIN") //ADMIN 유저만 접근 가능
            /* configuring authorities
            * hasRole() 단일 역할에 대한 유효성 검사
            * hasAnyRole() 구성된 역할 중 하나라고 있으면 가능
            * access() SpEL 위의 방법으로 할 수 없는 다양한 역할 설정 가능 or and 연산자 가능
            * ROLE_ 접두사는 DB구성시에만 사용하고 실제 역할 구성할 때(Java)는 이름만
            * access()는 사용자의 국가나 현재 시간/날짜에 따라 액세스 구성 가능
            * */
            .anyRequest()
            .authenticated(); //.authenticated() 로그인한 사용자만 접근 가능
    http
        //필터 추가 LoginFilter()는 인자를 받음
        //UsernamePasswordAuthenticationFilter는 Form방식 로그인
        //BasicAuthenticationFilter는 Basic 방식의 로그인 지원
        //AuthenticationManager()와 JWTUtil 인수 전달
        .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class)
        //session 설정 JESSION이라는 쿠키를 통해 특정 이때 서버측 메모리에 저장 X
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    return http.build();
  }
}