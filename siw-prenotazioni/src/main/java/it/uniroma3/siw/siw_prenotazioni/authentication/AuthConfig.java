package it.uniroma3.siw.siw_prenotazioni.authentication;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import it.uniroma3.siw.siw_prenotazioni.model.Role;

@Configuration
public class AuthConfig {
    
    private final DataSource dataSource;

    public AuthConfig (DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
        manager.setAuthoritiesByUsernameQuery("SELECT username, ruolo FROM credentials WHERE username=?");
        return manager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // 1. AUTORIZZAZIONI: chi può accedere a cosa
    http.authorizeHttpRequests(auth -> {
        auth.requestMatchers(HttpMethod.GET, "/", "/index", "/login", "/register",
                "/servizi", "/servizi/**", "/css/**", "/images/**", "/favicon.ico").permitAll();
        auth.requestMatchers(HttpMethod.POST, "/login", "/register").permitAll();
        auth.requestMatchers("/admin/**").hasAuthority(Role.ADMIN.name());
        auth.anyRequest().authenticated();
    });

    // 2. LOGIN
    http.formLogin(form -> {
        form.loginPage("/login").permitAll();
        form.defaultSuccessUrl("/success", true);
        form.failureUrl("/login?error=true");
    });

    // 3. LOGOUT
    http.logout(logout -> {
        logout.logoutUrl("/logout");
        logout.logoutSuccessUrl("/");
        logout.invalidateHttpSession(true);
        logout.deleteCookies("JSESSIONID");
        logout.clearAuthentication(true);
        logout.permitAll();
    });

    return http.build();
}
}
