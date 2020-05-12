package app.servlets;

import app.dao.UserDao;
import app.entities.User;
import app.tools.ConnectionTool;
import app.tools.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

public class LikedServlet extends HttpServlet {
  private final TemplateEngine engine;

  public LikedServlet(TemplateEngine engine) throws SQLException {
    this.engine = engine;
  }

  UserDao userDao = new UserDao();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    try {
      HashMap<String, Object> data = new HashMap<>();
      User currentUser = userDao.getUserFromCookie(req);

      userDao.addOnline(currentUser);
      data.put("liked", userDao.getLikedUsers(currentUser));

      engine.render("people-list.ftl", data, resp);
    } catch (SQLException sqlException) {
      throw new RuntimeException("Unexpected error happened :(");
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      String btn = req.getParameter("msg");

      Cookie msgId = new Cookie("message", String.format("%s", btn));
      msgId.setMaxAge(60 * 60);
      resp.addCookie(msgId);

      userDao.addOnline(userDao.getUserFromCookie(req));

      String delete = req.getParameter("delete");
      if (delete !=null) {
        Integer otherUserId = Integer.parseInt(delete);
        User otherUser = userDao.getById(otherUserId);
        User currentUser = userDao.getUserFromCookie(req);


        userDao.deleteLike(currentUser, otherUser);
        resp.sendRedirect("/liked");
      };

      resp.sendRedirect("/messages");

    } catch (SQLException sqlException) {
      throw new RuntimeException("Unexpected error happened :(");
    }
  }
}
