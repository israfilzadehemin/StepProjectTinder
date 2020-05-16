package app.servlets;

import app.dao.UserDao;
import lombok.SneakyThrows;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class LogoutServlet extends HttpServlet {

  @SneakyThrows
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    UserDao userDao = new UserDao();
    userDao.updateLastSeen(userDao.getUserFromCookie(req));

    Cookie[] cookies = req.getCookies();
    Arrays.stream(cookies)
            .forEach(c -> {
              c.setMaxAge(0);
              resp.addCookie(c);
            });

    resp.sendRedirect("/login");
  }

}
