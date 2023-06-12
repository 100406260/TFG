package com.example.tfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                    auth.requestMatchers("/login", "/public/**", "/uploadfiles123", "/uploadCourse", "/uploadLogs",
                    "/uploadQuiz").permitAll();
                   // auth.requestMatchers( "/uploadfiles123", "/uploadCourse", "/uploadLogs",
                    //"/uploadQuiz").hasRole("ADMIN");
                    auth.anyRequest().authenticated();
                }).formLogin().loginPage("/login").permitAll().and()
                .logout().permitAll();

        return http.build();
    }
    /*
     * @Bean
     * public UserDetailsService userDetailsService() {
     * UserDetails user = User.builder()
     * .username(null)
     * .password("password")
     * .roles("USER")
     * .build();
     * UserDetails teacher = User.withUsername("teacher")
     * .password("uploadfiles123").roles("TEACHER").build();
     * 
     * return new InMemoryUserDetailsManager(user, teacher);
     * }
     */

    /*
     * @Bean
     * protected SecurityFilterChain filterChain(HttpSecurity http) throws
     * Exception{
     * http.authorizeHttpRequests().
     * requestMatchers("/", "/static", "/uploadfile123", "/loginForm").permitAll()
     * .anyRequest().authenticated().and()
     * .formLogin().loginPage("/").permitAll()
     * .and().logout().permitAll();
     * /*
     * Any user can access the /login URL and any URL under /public.
     * Access to any other resource is premitted only for authenticated users.
     */
    /*
     * http
     * .authorizeRequests()
     * .antMatchers("/login", "/public/**", "/register").permitAll()
     * .anyRequest().authenticated()
     * .and()
     * .formLogin().loginPage("/login").permitAll()
     * .and()
     * .logout().permitAll();
     * return http.build();
     * }
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}