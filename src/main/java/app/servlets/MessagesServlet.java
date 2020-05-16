package app.servlets;

import app.dao.MessageDao;
import app.dao.UserDao;
import app.entities.Message;
import app.entities.User;
import app.tools.TemplateEngine;
import lombok.SneakyThrows;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class MessagesServlet extends HttpServlet {
  private final TemplateEngine engine;

  public MessagesServlet(TemplateEngine engine) throws SQLException {
    this.engine = engine;
  }

  UserDao userDao = new UserDao();
  MessageDao messageDao = new MessageDao();

  @SneakyThrows
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    Optional<Cookie> message = Arrays.stream(req.getCookies())
            .filter(m -> m.getName().equals("message"))
            .findFirst();

    if (message.equals(Optional.empty())) resp.sendRedirect("/liked");
    else {
      int otherUserId = Integer.parseInt(message.get().getValue());
      User otherUser = userDao.getById(otherUserId);
      User currentUser = userDao.getUserFromCookie(req);

      handleMessages(currentUser, otherUser, resp);
    }
  }

  @SneakyThrows
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp){
    String text = req.getParameter("text");

    Optional<Cookie> message = Arrays.stream(req.getCookies())
            .filter(m -> m.getName().equals("message"))
            .findFirst();

    if (message.equals(Optional.empty())) resp.sendRedirect("/liked");
    else {
      int otherUserId = Integer.parseInt(message.get().getValue());
      User otherUser = userDao.getById(otherUserId);
      User currentUser = userDao.getUserFromCookie(req);
      userDao.updateLastSeen(currentUser);

      String btn = req.getParameter("exit");
      if (Objects.equals(btn, "exit")) resp.sendRedirect("/liked");
      else {
        messageDao.addMessage(currentUser, otherUser, text);
        handleMessages(currentUser, otherUser, resp);
      }
    }
  }

  void handleMessages(User currentUser, User otherUser, HttpServletResponse resp) throws SQLException {
    HashMap<String, Object> data = new HashMap<>();

    List<Message> sent = messageDao.getMessages(currentUser, otherUser);
    List<Message> received = messageDao.getMessages(otherUser, currentUser);
    List<Message> allMessages = new ArrayList<>();

    allMessages.addAll(sent);
    allMessages.addAll(received);

    Comparator<Message> compareById = Comparator.comparing(Message::getId);
    allMessages.sort(compareById);

    data.put("messages", allMessages);
    data.put("current", currentUser);
    data.put("other", otherUser);
    engine.render("chat2.ftl", data, resp);
  }

}
