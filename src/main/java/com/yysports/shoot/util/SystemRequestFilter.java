package com.yysports.shoot.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Created By cyj
 */
public class SystemRequestFilter implements Filter {

    /**
     * 跨域处理
     *
     * @param req
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        SystemRequestContext.setHttpRequest(request);
        SystemRequestContext.setSession(request.getSession());
        try {
            chain.doFilter(request, response);
        } finally {
            SystemRequestContext.removeHttpRequest();
            SystemRequestContext.removeHttpSession();
        }
    }

    /**
     * @param filterConfig
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) {

    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {

    }

}
