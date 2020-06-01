package app.servlets;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Log4j2
public class LogoutServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    //Clearing all cookies
    Arrays.stream(req.getCookies())
            .forEach(c -> {
              c.setMaxAge(0);
              resp.addCookie(c);
            });

    try {
      resp.sendRedirect("/login");
    } catch (IOException e) {
      log.warn(String.format("Redirecting from logout page to login page failed: %s", e.getMessage()));
    }
  }

}
