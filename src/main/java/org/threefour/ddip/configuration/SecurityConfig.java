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
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.threefour.ddip.member.jwt.CoustomLogoutFilter;
import org.threefour.ddip.member.jwt.JWTUtil;
import org.threefour.ddip.member.jwt.LoginFilter;
import org.threefour.ddip.member.repository.RefreshRepository;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final AuthenticationConfiguration authenticationConfiguration;
  private final JWTUtil jwtUtil;
  private final RefreshRepository refreshRepository;

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
            .csrf().disable()
            .httpBasic().disable()
            .authorizeHttpRequests()
            .antMatchers("/**").permitAll()
            .antMatchers("/admin").hasRole("ADMIN")
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessHandler((request, response, authentication) -> {
              response.setStatus(HttpServletResponse.SC_OK);})
            .and()
            .addFilterBefore(new CoustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class)
            .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    return http.build();
  }
}