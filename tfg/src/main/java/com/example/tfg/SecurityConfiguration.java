package com.example.tfg;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /*
     * @Bean
     * public UserDetailsService userDetailsService() {
     * UserDetails user = User.builder()
     * .username("user")
     * .password("password")
     * .roles("USER")
     * .build();
     * UserDetails admin = User.builder()
     * .username("admin")
     * .password("password")
     * .roles("ADMIN", "USER")
     * .build();
     * return new InMemoryUserDetailsManager(user, admin);
     * }
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeHttpRequests(auth -> {
            auth.requestMatchers("/login", "/public/**").permitAll();
            auth.requestMatchers("/uploadfiles123", "/uploadStudents", "/uploadCourse", "/uploadLogs",
                    "/uploadQuiz").hasAuthority("ADMIN");
            auth.anyRequest().authenticated();
                }).formLogin().loginPage("/login").permitAll().and()
                .logout().permitAll();
            
            

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}