package edu.wisc.cae.springbug;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class DemoFormPostAuthFilter extends UsernamePasswordAuthenticationFilter {
    public DemoFormPostAuthFilter(AuthenticationManager authenticationManager)
    {
        this.setAuthenticationManager(authenticationManager);
        this.setFilterProcessesUrl("/login/form");
        this.setAuthenticationSuccessHandler(new RefererRedirectionAuthenticationSuccessHandler());
    }
}
