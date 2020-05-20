package app.servlets;

import lombok.SneakyThrows;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

  @SneakyThrows
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    resp.sendRedirect("/login");
  }
}
