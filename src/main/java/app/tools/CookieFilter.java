package app.tools;


import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Log4j2
public class CookieFilter implements Filter {

  private boolean isHttp(ServletRequest req) {
    return req instanceof HttpServletRequest;
  }

  public boolean isLogged(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) return false;
    return (int) Arrays.stream(cookies).filter(c -> c.getName().equals("login")).count() != 0;
  }

  @Override
  public void init(FilterConfig filterConfig) {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
    try {
      if (isHttp(servletRequest) && isLogged((HttpServletRequest) servletRequest)) {
        filterChain.doFilter(servletRequest, servletResponse);
      } else ((HttpServletResponse) servletResponse).sendRedirect("/login");
    } catch (ServletException e) {
      log.warn(String.format("Servlet Exception happened in method doFilter(): %s", e.getMessage()));
    } catch (IOException e) {
      log.warn(String.format("Redirecting to login page failed: %s", e.getMessage()));
    }
  }

  @Override
  public void destroy() {

  }
}
