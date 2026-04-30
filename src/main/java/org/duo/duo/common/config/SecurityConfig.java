package org.duo.duo.common.config;

import lombok.RequiredArgsConstructor;
import org.duo.duo.common.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/ws/**")
            )

            .headers(headers -> headers
                .contentTypeOptions(contentType -> {})
            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/", "/login", "/signup", "/css/**", "/js/**", "/images/**", "/uploads/**", "/ws/**").permitAll()

                .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/notifications/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/riot/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/boards/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/boards/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/freeboards/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/freeboards/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/profile/*").permitAll()
                .requestMatchers("/profile/**").hasAnyRole("USER", "ADMIN")

                .requestMatchers("/chat/**").hasAnyRole("USER", "ADMIN")

                .requestMatchers("/admin/**").hasRole("ADMIN")
            )


            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/boards", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )

            .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}