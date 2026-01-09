package com.oopAssignment.financeTracker.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        String path = request.getRequestURI();
        String message = authException.getMessage() != null ? authException.getMessage() : "Unauthorized";

        response.getWriter().write(String.format("""
                    {
                      "success": false,
                      "message": "Unauthorized or invalid JWT",
                      "data": {
                         "status": 401,
                         "error": "Unauthorized",
                         "message": "%s",
                         "path": "%s"
                      }
                    }
                """, message, path));
    }
}
