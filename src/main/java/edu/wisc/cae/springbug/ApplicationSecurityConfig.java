package edu.wisc.cae.springbug;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ApplicationSecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests((authorize) -> authorize
                .antMatchers("/", "/login.html", "/error", "/css/**", "/js/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(Customizer.withDefaults())
            .formLogin((formLogin) -> formLogin
                .loginProcessingUrl("/login/form")
            )
            .exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint())
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/").permitAll()
            )
            .cors(Customizer.withDefaults())
            .csrf(CsrfConfigurer::disable);
        // @formatter:on

        return http.build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        LoginUrlAuthenticationEntryPoint webAuthenticationEntryPoint =
                new LoginUrlAuthenticationEntryPoint("/login.html");
        MediaTypeRequestMatcher textHtmlMatcher =
                new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
        textHtmlMatcher.setUseEquals(true);

        DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint = new DelegatingAuthenticationEntryPoint(new LinkedHashMap<>(
                Map.of(textHtmlMatcher, webAuthenticationEntryPoint)));
        delegatingAuthenticationEntryPoint.setDefaultEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        return delegatingAuthenticationEntryPoint;
    }


    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("demo")
                .password(passwordEncoder.encode("password"))
                .roles("somerole")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

}
