package com.springsecurity.hyeonbank.filter;

import jakarta.servlet.*;

import java.io.IOException;
import java.util.logging.Logger;

public class AuthoritiesLoggingAtFilter implements Filter {
    private Logger LOG = Logger.getLogger(AuthoritiesLoggingAtFilter.class.getName());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOG.info("인증 검증이 진행 중입니다.");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
