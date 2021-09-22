package com.example.restapi.security;

import com.example.restapi.config.CustomAuthenticationEntryPoint;
import com.example.restapi.domain.CustomUser;
import com.example.restapi.exception.InvalidTokenException;
import com.example.restapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final TokenProvider provider;
    private final TokenValidator validator;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public TokenAuthenticationFilter(TokenProvider provider, TokenValidator validator,
                                     UserDetailsService userDetailsService, UserService userService, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.provider = provider;
        this.validator = validator;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && validator.validate(jwt) && !validator.isTokenForResetPassword(jwt)) {
                try{
                    saveUserToContextHolder(request, jwt);
                } catch (Exception e){
                    sendTokenError(request, response);
                }
            }
            if(allowRequestWithoutToken(request)){
                filterChain.doFilter(request, response);
                return;
            } else if (!StringUtils.hasText(jwt) || !validator.validate(jwt) || validator.isTokenForResetPassword(jwt)){
                sendTokenError(request, response);
                return;
            }
        } catch (Exception exception) {
            logger.error("Could not set user authentication in security context", exception);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public boolean allowRequestWithoutToken(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.contains("/auth/");
    }

    private void saveUserToContextHolder(HttpServletRequest request, String jwt){
        Long userId = provider.getUserIdFromToken(jwt);
        CustomUser user = userService.findUserById(userId);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void sendTokenError(HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        String message = "invalid.token";
        customAuthenticationEntryPoint.commence(request,response,new InvalidTokenException(message));
    }

}
