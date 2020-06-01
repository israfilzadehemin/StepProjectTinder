package app.servlets;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class MainServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    //Redirecting any wrong URL to login page
    try {
      resp.sendRedirect("/login");
    } catch (IOException e) {
      log.warn(String.format("Redirecting to login page failed: %s", e.getMessage()));
    }
  }
}
