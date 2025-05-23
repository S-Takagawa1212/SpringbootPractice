package com.example.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.security.service.CustomUserDetailService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/admin/signin", // ← ログインページは認証不要
                                "/admin/signup", // ← サインアップページは認証不要
                                "/contact/**", // ←お問い合わせ自体は認証不要
                                "/css/**", "/js/**", "/images/**")
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll())

                .formLogin(form -> form
                        .usernameParameter("email") // emailでログイン認証するのでformのname属性をemailに変更
                        .loginPage("/admin/signin")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/admin/contacts")
                        .failureUrl("/admin/signin?error=true"))

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/admin/signin")
                        .permitAll())

                .userDetailsService(customUserDetailService);

        return http.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
