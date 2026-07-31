package com.projects.coaching_offline_support.security;

import com.projects.coaching_offline_support.auth.Services.JwtService;
import com.projects.coaching_offline_support.user.User;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final  JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        String token = null;
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token =  authHeader.substring(7);

        }

        if(token == null && request.getCookies() !=null){
            for(Cookie cookie : request.getCookies()){
                if("access_token".equals(cookie.getName())){
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if(token == null){
            filterChain.doFilter(request,response);
            return;
        }
        String tokenType = jwtService.getTokenType(token);

        if (!"access".equals(tokenType)) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid access token"
            );

            return;
        }


        UUID userId ;
        try{
            userId = jwtService.getUserIdFromToken(token);
        } catch (JwtException e) {
            log.warn("Invalid or expired jwt token {}",e.getMessage());
            response.sendError( HttpServletResponse.SC_UNAUTHORIZED,"Invalid or expired token");

            return;
        }



        if(SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());


                UsernamePasswordAuthenticationToken authenticaten =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,null,userDetails.getAuthorities());

                authenticaten.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticaten);



        }
        filterChain.doFilter(request,response);
    }
}
