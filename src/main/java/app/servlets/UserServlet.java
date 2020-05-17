package app.servlets;

import app.dao.LikeDao;
import app.dao.MessageDao;
import app.dao.UserDao;
import app.entities.User;
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
import java.util.Optional;

public class UserServlet extends HttpServlet {
  private final TemplateEngine engine;

  public UserServlet(TemplateEngine engine) {
    this.engine = engine;
  }

  UserDao userDao = new UserDao();
  MessageDao messageDao = new MessageDao();
  LikeDao likeDao = new LikeDao();

  @SneakyThrows
  private void showUser(HttpServletResponse resp, Optional<User> me) {
    Optional<User> showingUser = likeDao.getRandomUnvisitedUser(me.get());

    if (showingUser.equals(Optional.empty())) resp.sendRedirect("/liked");
    else {

      Cookie cookie = new Cookie("clicked", String.format("%s", showingUser.get().getMail()));
      cookie.setMaxAge(60 * 60);
      resp.addCookie(cookie);

      HashMap<String, Object> data = new HashMap<>();
      data.put("user", showingUser.get());

      userDao.updateLastSeen(me.get());
      engine.render("like-page.ftl", data, resp);
    }
  }

  @SneakyThrows
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    messageDao.removeMessageAccess(req, resp);
    Optional<User> me = userDao.getUserFromCookie(req, "login");
    showUser(resp, me);

  }

  @SneakyThrows
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    String btn = req.getParameter("button");

    Optional<Cookie> clicked = Arrays.stream(req.getCookies())
            .filter(c -> c.getName().equals("clicked"))
            .findFirst();

    if (clicked.equals(Optional.empty())) resp.sendRedirect("/users");
    else {
      Optional<User> me = userDao.getUserFromCookie(req, "login");
      Optional<User> clickedUser = userDao.getUserFromCookie(req, "clicked");

      likeDao.addAction(me.get(), clickedUser.get(), btn);
      showUser(resp, me);
    }
  }


}

