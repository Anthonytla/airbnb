package com.quest.etna.config;

import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component("jwtRequestFilter")
@Order(1)
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            System.out.println(authHeader);
            String path = request.getRequestURI();
            if ("/register".equals(path) || "/authenticate".equals(path)) {
                filterChain.doFilter(request, response);
                return;
            }
            if (StringUtils.hasText(authHeader)) {
                System.out.println("");
                String bearerToken = authHeader.split(" ")[1];
                System.out.println(bearerToken);
                Optional<User> user = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(bearerToken));
                System.out.println("INN");
                if (user.isPresent()) {
                    JwtUserDetails jwtUserDetails = new JwtUserDetails(user.get());
                    if (jwtTokenUtil.validateToken(bearerToken, jwtUserDetails)) {
                        Authentication auth = new UsernamePasswordAuthenticationToken(jwtUserDetails.getUsername(), jwtUserDetails.getPassword(), jwtUserDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        filterChain.doFilter(request, response);
                    }
                }
                else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized.");
                }

            }
            else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized.");
            }


        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized.");
        }
    }
}
