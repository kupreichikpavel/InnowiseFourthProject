package com.example.innowisefourthproject.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class EncodingFilter implements Filter {
    private static final String UTF_8 = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        request.setCharacterEncoding(UTF_8);
        response.setCharacterEncoding(UTF_8);
        response.setContentType("text/html;charset=UTF-8");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}