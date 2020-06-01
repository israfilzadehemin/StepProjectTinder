package app.servlets;

import app.entities.User;
import app.dao.LikeDao;
import app.dao.MessageDao;
import app.dao.UserDao;
import app.tools.EncodingTool;
import app.tools.TemplateEngine;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Log4j2
public class LikedServlet extends HttpServlet {
  private final TemplateEngine engine;

  public LikedServlet(TemplateEngine engine) {
    this.engine = engine;
  }

  UserDao userDao = new UserDao();
  MessageDao messageDao = new MessageDao();
  LikeDao likeDao = new LikeDao();
  EncodingTool encodingTool = new EncodingTool();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    messageDao.removeMessageAccess(req, resp);

    Optional<User> me = userDao.getUserFromCookie(req, "login");
    List<User> all = likeDao.getVisitedUsers(me.get(), "all");

    HashMap<String, Object> data = new HashMap<>();
    data.put("liked", all);

    userDao.updateLastSeen(me.get());
    engine.render("people-list.ftl", data, resp);

  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    Optional<User> me = userDao.getUserFromCookie(req, "login");

    try {
      //Message button clicked
      String messageBtn = req.getParameter("msg");
      if (messageBtn != null) {
        Cookie msgUsername = new Cookie("message", encodingTool.encrypt(messageBtn));
        msgUsername.setMaxAge(60 * 60);
        resp.addCookie(msgUsername);

        resp.sendRedirect("/messages");
      }

      //View liked/disliked/all clicked
      String action = req.getParameter("action");
      if (action != null) {
        HashMap<String, Object> data = new HashMap<>();
        List<User> users = likeDao.getVisitedUsers(me.get(), action);
        data.put("liked", users);

        userDao.updateLastSeen(me.get());
        engine.render("people-list.ftl", data, resp);
      }

      //Delete button clicked
      String userId = req.getParameter("delete");
      if (userId != null) {
        User otherUser = userDao.getAllUsers().stream()
                .filter(u -> u.getId() == Integer.parseInt(userId))
                .findFirst().get();

        likeDao.deleteAction(me.get(), otherUser);
        resp.sendRedirect("/liked");
      }

    } catch (IOException e) {
      log.warn(String.format("Redirecting from liked page failed: %s", e.getMessage()));
    }
  }
}
