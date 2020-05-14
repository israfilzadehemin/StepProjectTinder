package app.servlets;

import app.dao.UserDao;
import app.entities.User;
import app.tools.ConnectionTool;
import app.tools.TemplateEngine;
import lombok.SneakyThrows;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

public class LogoutServlet extends HttpServlet {

  @SneakyThrows
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    UserDao userDao = new UserDao();
    userDao.addLastLogin(userDao.getUserFromCookie(req));

    Cookie[] cookies = req.getCookies();
    Arrays.stream(cookies)
            .forEach(c -> {
              c.setMaxAge(0);
              resp.addCookie(c);
            });

    resp.sendRedirect("/login");
  }

}
