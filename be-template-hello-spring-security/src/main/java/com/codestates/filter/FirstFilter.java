package com.codestates.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
public class FirstFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("----- Fist Filter 생성 -----");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("===== First Filter Start");
        chain.doFilter(request, response);
        log.info("===== First Filter End");
    }

    @Override
    public void destroy() {
        log.info("===== First Filter Destroy");
        Filter.super.destroy();
    }
}
