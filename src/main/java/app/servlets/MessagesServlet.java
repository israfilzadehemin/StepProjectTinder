package app.servlets;

import app.dao.UserDao;
import app.entities.Message;
import app.entities.User;
import app.tools.ConnectionTool;
import app.tools.TemplateEngine;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class MessagesServlet extends HttpServlet {
  private final TemplateEngine engine;

  public MessagesServlet(TemplateEngine engine) {
    this.engine = engine;
  }

  ConnectionTool connTool = new ConnectionTool();
  UserDao userDao = new UserDao();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      userDao.getAllUsers().addAll(connTool.getUsers());

      Optional<Cookie> message = Arrays.stream(req.getCookies())
              .filter(m -> m.getName().equals("message"))
              .findFirst();

      if (message.equals(Optional.empty())) resp.sendRedirect("/liked");
      else {
        int otherUserId = Integer.parseInt(message.get().getValue());
        User otherUser = userDao.getById(otherUserId);
        User currentUser = connTool.getUserFromCookie(req);

        handleMessages(currentUser, otherUser, resp);
      }

    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }

  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String text = req.getParameter("text");

    Optional<Cookie> message = Arrays.stream(req.getCookies())
            .filter(m -> m.getName().equals("message"))
            .findFirst();
    try {
      if (message.equals(Optional.empty())) resp.sendRedirect("/liked");
      else {
        int otherUserId = Integer.parseInt(message.get().getValue());
        User otherUser = userDao.getById(otherUserId);
        User currentUser = connTool.getUserFromCookie(req);
        connTool.addOnline(currentUser);

        connTool.addMessage(currentUser, otherUser, text);
        handleMessages(currentUser, otherUser, resp);

      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }

    String btn = req.getParameter("exit");
    if (Objects.equals(btn, "exit")) resp.sendRedirect("/liked");

  }

  void handleMessages(User currentUser, User otherUser, HttpServletResponse resp) throws SQLException {
    HashMap<String, Object> data = new HashMap<>();

    List<Message> sent = connTool.getMessages(currentUser, otherUser);
    List<Message> received = connTool.getMessages(otherUser, currentUser);
    List<Message> allMessages = new ArrayList<>();
    allMessages.addAll(sent);
    allMessages.addAll(received);

    Comparator<Message> compareById = Comparator.comparing(Message::getId);
    allMessages.sort(compareById);

    data.put("messages", allMessages);
    data.put("current", currentUser);
    data.put("other", otherUser);
    engine.render("chat.ftl", data, resp);
  }
}
