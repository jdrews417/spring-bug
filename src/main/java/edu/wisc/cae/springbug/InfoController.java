package edu.wisc.cae.springbug;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class InfoController {

    @RequestMapping("/user-info")
    public Map<String, Object> user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> auth = new HashMap<>();
        if (authentication == null || authentication.getPrincipal() == null) return null;
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            DefaultOidcUser user = (DefaultOidcUser) token.getPrincipal();
            auth.put("OIDC Source", token.getAuthorizedClientRegistrationId());
            auth.put("AuthMethod", "OIDC");
            auth.put("name", user.getName());
        }
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if (token.getPrincipal() != null) {

                // In this method of authentication, the username is the caelogin name
                User user = (User) token.getPrincipal();
                auth.put("AuthMethod","LoginPassword");
                auth.put("name", user.getUsername());
            }
        }
        Object[] authorities = authentication.getAuthorities().toArray();
        List<String> roles = new ArrayList<>();
        if (authorities != null)
        {
            for (Object authority : authorities)
            {
                if (authority instanceof SimpleGrantedAuthority)
                {
                    SimpleGrantedAuthority a = (SimpleGrantedAuthority) authority;
                    roles.add(a.getAuthority());
                }
            }
        }
        if (roles.size() > 0)
            auth.put("roles",roles);
        return auth;
    }
}
