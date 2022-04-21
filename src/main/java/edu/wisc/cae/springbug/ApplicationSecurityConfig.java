package edu.wisc.cae.springbug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        http
                .cors().and()
                .csrf().disable()
                //.addFilter(new CAEBasicAuthFilter(authenticationManager()))
                .addFilter(new DemoFormPostAuthFilter(authenticationManager()))
                .authorizeRequests()
                .antMatchers("/", "index", "/css/**", "/js/**","/webjars/**","/demo/**", "/error").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .and()
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .logout(l -> l
                        .logoutSuccessUrl("/").permitAll()
                )
        ;

        //http.headers(headers -> headers.referrerPolicy(referrerPolicy -> referrerPolicy.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN)));

    }


    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails annaSmithUser = User.builder()
                .username("demo")
                .password(passwordEncoder.encode("password"))
                .roles("somerole")
                .build();

        return new InMemoryUserDetailsManager(
                annaSmithUser
        );

    }

    /** this bean can be enabled to allow for detection of multiple logins of the same account
     * Then need to add to the http config above:
     * .sessionManagement(session -> session
     *            .maximumSessions(1)
     *        );
     * it also enables tracking of all concurrent sessions and can the SessionRegistry can be queried
     * for info with getAllPrincipals()
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * This Bean provides our support for the username/password authentication
     * path.
     *
     * @return Returns the {@link DaoAuthenticationProvider}
     */
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }
}
