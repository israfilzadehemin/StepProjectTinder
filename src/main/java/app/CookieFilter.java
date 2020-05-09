package app;


import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class CookieFilter implements Filter {

  private boolean isHttp(ServletRequest req) {
    return req instanceof HttpServletRequest;
  }

  public boolean isLogged(HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) return false;
    return (int) Arrays.stream(cookies).filter(c -> c.getName().equals("login")).count() != 0;
  }


  @Override
  public void init(FilterConfig filterConfig) {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    if (isHttp(servletRequest) && isLogged((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse)) {
      filterChain.doFilter(servletRequest, servletResponse);
    } else ((HttpServletResponse) servletResponse).sendRedirect("/login");

  }

  @Override
  public void destroy() {

  }
}
