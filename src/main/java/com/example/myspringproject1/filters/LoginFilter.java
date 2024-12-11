package com.example.myspringproject1.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        HttpSession session = httpRequest.getSession(false);

        System.out.println("Request URI: " + path);

        if (path.startsWith("/login") || path.startsWith("/logout")
                || path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/images")) {
            chain.doFilter(request, response);
            return;
        }

        if (session == null || session.getAttribute("loggedInUser") == null) {
            System.out.println("Unauthorized access attempt. Redirecting to /login");
            httpResponse.sendRedirect("/login");
            return;
        }

        System.out.println("Logged-in user: " + session.getAttribute("loggedInUser"));

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
