package ru.nc.musiclib.controller;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
@Component
public class ContextPathFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setAttribute("contextPath", ((HttpServletRequest) request).getContextPath());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
