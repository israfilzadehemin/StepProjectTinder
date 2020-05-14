package app.servlets;

import app.dao.LikeDao;
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
import java.util.Optional;

public class UserServlet extends HttpServlet {
  private final TemplateEngine engine;

  public UserServlet(TemplateEngine engine) throws SQLException {
    this.engine = engine;
  }

  UserDao userDao = new UserDao();
  LikeDao likeDao = new LikeDao();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    try {
      HashMap<String, Object> data = new HashMap<>();
      User currentUser = userDao.getUserFromCookie(req);
      Optional<User> showingUser = likeDao.getRandomUnlikedUser(currentUser);

      if (showingUser.equals(Optional.empty())) resp.sendRedirect("/liked");
      else {
        Cookie cookies = new Cookie("liked", String.format("%s", showingUser.get().getMail()));
        cookies.setMaxAge(60);
        resp.addCookie(cookies);
        userDao.addOnline(currentUser);

        data.put("user", showingUser.get());
        engine.render("like-page.ftl", data, resp);
      }

    } catch (SQLException | IOException sqlException) {
      throw new RuntimeException("Unexpected error happened :(");
    }
  }

  @SneakyThrows
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    HashMap<String, Object> data = new HashMap<>();
    String btn = req.getParameter("button");

    User currentUser = userDao.getUserFromCookie(req);
    Cookie[] cookies = req.getCookies();
    boolean isLiked = Arrays.stream(cookies).anyMatch(c -> c.getName().equals("liked"));

    if (isLiked) {
      String liked = Arrays.stream(cookies)
              .filter(c -> c.getName().equals("liked"))
              .findFirst()
              .orElseThrow(RuntimeException::new)
              .getValue();

      User likedUser = userDao.getAllUsers().stream()
              .filter(u -> u.getMail().equals(liked))
              .findFirst()
              .orElseThrow(RuntimeException::new);

      if (btn.equals("like")) likeDao.addLike(currentUser, likedUser);
      Optional<User> showingUser = likeDao.getRandomUnlikedUser(currentUser);

      if (showingUser.equals(Optional.empty())) resp.sendRedirect("/liked");
      else {
        Arrays.stream(cookies).forEach(c -> c.setMaxAge(0));
        Cookie newCookie = new Cookie("liked", String.format("%s", showingUser.get().getMail()));
        newCookie.setMaxAge(60);
        resp.addCookie(newCookie);

        data.put("user", showingUser.get());
        engine.render("like-page.ftl", data, resp);
      }
    }

  }

}

