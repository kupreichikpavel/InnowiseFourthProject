package com.example.innowisefourthproject.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebFilter(
        urlPatterns = {"/pages/*"},
        initParams = {
                @WebInitParam(name = "INDEX_PATH", value = "/index.jsp")
        }
)
public class SecurityFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(SecurityFilter.class);

    private static final String USER_ATTRIBUTE = "user";

    private String indexPath;

    @Override
    public void init(FilterConfig filterConfig) {
        indexPath = filterConfig.getInitParameter("INDEX_PATH");
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String requestURI = httpServletRequest.getRequestURI();

        if (requestURI.endsWith("/pages/register.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpServletRequest.getSession(false);

        boolean isAuthorized = session != null && session.getAttribute(USER_ATTRIBUTE) != null;

        if (!isAuthorized) {
            logger.info("Unauthorized access to protected page: {}", httpServletRequest.getRequestURI());
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + indexPath);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}