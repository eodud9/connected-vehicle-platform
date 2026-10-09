package connected_vehicle.global.config;

import connected_vehicle.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        auth -> auth
                                        .requestMatchers(HttpMethod.POST,"/api/users").permitAll()
                                        .requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/ownerships").hasRole("ADMIN")
                                        .anyRequest().authenticated()
                )
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }
}
