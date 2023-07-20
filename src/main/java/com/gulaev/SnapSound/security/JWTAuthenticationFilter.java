package com.gulaev.SnapSound.security;

import com.gulaev.SnapSound.service.CustomUserDetailsService;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JWTTokenProvider jwtTokenProvider;
  @Autowired
  private CustomUserDetailsService customUserDetailsService;
  public static final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
   try {
     String jwt = getJWTFromRequest(request);
     if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)){
       Long userId = jwtTokenProvider.getUserIdFromToken(jwt);

       UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
           customUserDetailsService.loadUserById(userId), null, Collections.emptyList()
       );
       authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
       SecurityContextHolder.getContext().setAuthentication(authenticationToken );
     }
   } catch (Exception e) {
     log.error("Could not set user authentication "+  e.getMessage());
   }
    finally {
         filterChain.doFilter(request, response);
   }
  }

  private String getJWTFromRequest(HttpServletRequest request) {
    String bearToken = request.getHeader(SecurityConstant.HEADER_STRING);
    if (StringUtils.hasText(bearToken) && bearToken.startsWith(SecurityConstant.TOKEN_PREFIX)) {
      return bearToken.split(" ")[1];
    } else {
      return "No data request";
    }

  }

}
