package org.emin.tinder.servlets;

import lombok.SneakyThrows;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class LogoutServlet extends HttpServlet {

  @SneakyThrows
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

    Arrays.stream(req.getCookies())
            .forEach(c -> {
              c.setMaxAge(0);
              resp.addCookie(c);
            });

    resp.sendRedirect("/login");
  }

}
