package app.servlets;

import app.dao.LikeDao;
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
import java.util.HashMap;
import java.util.List;

public class LikedServlet extends HttpServlet {
  private final TemplateEngine engine;

  public LikedServlet(TemplateEngine engine) throws SQLException {
    this.engine = engine;
  }

  UserDao userDao = new UserDao();
  LikeDao likeDao = new LikeDao();

  @SneakyThrows
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    HashMap<String, Object> data = new HashMap<>();
    User currentUser = userDao.getUserFromCookie(req);

    userDao.updateLastSeen(currentUser);
    List<User> all = likeDao.getVisitededUsers(currentUser, "all");

    data.put("liked", all);

    engine.render("people-list.ftl", data, resp);
  }

  @SneakyThrows
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    String messageBtn = req.getParameter("msg");

    Cookie msgId = new Cookie("message", String.format("%s", messageBtn));
    msgId.setMaxAge(60 * 60);
    resp.addCookie(msgId);

    User currentUser = userDao.getUserFromCookie(req);
    userDao.updateLastSeen(userDao.getUserFromCookie(req));

    String delete = req.getParameter("delete");
    if (delete != null) {
      int otherUserId = Integer.parseInt(delete);
      User otherUser = userDao.getById(otherUserId);


      likeDao.deleteAction(currentUser, otherUser);
      resp.sendRedirect("/liked");
    }

    String action = req.getParameter("action");

    if (action!=null) {
      HashMap<String, Object> data = new HashMap<>();
      List<User> users = likeDao.getVisitededUsers(currentUser, action);
      data.put("liked", users);
      engine.render("people-list.ftl", data, resp);
    }

    resp.sendRedirect("/messages");
  }
}
