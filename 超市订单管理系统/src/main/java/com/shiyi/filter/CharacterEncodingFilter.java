package com.shiyi.filter;

import javax.servlet.*;
import java.io.IOException;


public class CharacterEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        filterChain.doFilter(req,resp);
    }

    @Override
    public void destroy() {

    }
}
