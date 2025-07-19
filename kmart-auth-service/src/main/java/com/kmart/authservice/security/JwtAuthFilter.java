package com.kmart.authservice.security;

import com.kmart.authservice.service.AdminDetailsService;
import com.kmart.authservice.service.CustomUserDetailsService;
import com.kmart.authservice.service.VendorDetailsService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AdminDetailsService adminDetailsService;

    @Autowired
    private VendorDetailsService vendorDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/users/login")
                || path.startsWith("/api/users/register")
                || path.startsWith("/api/admin/login")
                || path.startsWith("/api/admin/register")
                || path.startsWith("/api/vendor/login")
                || path.startsWith("/api/vendor/register");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                System.out.println("🔐 Username: " + username);
                System.out.println("🔐 Role from token: " + role);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails;

                    switch (role) {
                        case "ROLE_ADMIN":
                            userDetails = adminDetailsService.loadUserByUsername(username);
                            break;
                        case "ROLE_VENDOR":
                            userDetails = vendorDetailsService.loadUserByUsername(username);
                            break;
                        case "ROLE_USER":
                            userDetails = customUserDetailsService.loadUserByUsername(username);
                            break;
                        default:
                            throw new RuntimeException("Invalid role in token: " + role);
                    }

                    if (jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null,
                                        List.of(new SimpleGrantedAuthority(role))
                                );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

            } catch (Exception e) {
                System.out.println("❌ JWT authentication failed: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
