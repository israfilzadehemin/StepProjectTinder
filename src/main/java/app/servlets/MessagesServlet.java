package app.servlets;

import app.dao.MessageDao;
import app.dao.UserDao;
import app.entities.Message;
import app.entities.User;
import app.tools.TemplateEngine;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Log4j2
public class MessagesServlet extends HttpServlet {
  private final TemplateEngine engine;

  public MessagesServlet(TemplateEngine engine) {
    this.engine = engine;
  }

  UserDao userDao = new UserDao();
  MessageDao messageDao = new MessageDao();

  void handleMessages(User currentUser, User otherUser, HttpServletResponse resp) {

    //Getting messages and sorting them by id
    List<Message> sent = messageDao.getMessages(currentUser, otherUser);
    List<Message> received = messageDao.getMessages(otherUser, currentUser);
    List<Message> allMessages = new ArrayList<>();

    allMessages.addAll(sent);
    allMessages.addAll(received);

    Comparator<Message> compareById = Comparator.comparing(Message::getId);
    allMessages.sort(compareById);

    //Sending messages to ftl file
    HashMap<String, Object> data = new HashMap<>();
    data.put("messages", allMessages);
    data.put("current", currentUser);
    data.put("other", otherUser);
    engine.render("chat2.ftl", data, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    //Checking cookie in order to avoid unwilling access to messages
    Optional<Cookie> message = Arrays.stream(req.getCookies())
            .filter(m -> m.getName().equals("message"))
            .findFirst();

    try {
      if (message.equals(Optional.empty())) resp.sendRedirect("/liked");
      else {
        Optional<User> me = userDao.getUserFromCookie(req, "login");
        Optional<User> otherUser = userDao.getUserFromCookie(req, "message");

        if (otherUser.equals(Optional.empty())) resp.sendRedirect("/liked");
        else {
          userDao.updateLastSeen(me.get());
          handleMessages(me.get(), otherUser.get(), resp);
        }
      }
    } catch (IOException e) {
      log.warn(String.format("Redirecting from messages page to liked page failed: %s", e.getMessage()));
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    //Getting value from message input
    String text = req.getParameter("text");

    //Checking cooke in order to avoid unwilling access to messages
    Optional<Cookie> message = Arrays.stream(req.getCookies())
            .filter(m -> m.getName().equals("message"))
            .findFirst();

    try {
      if (message.equals(Optional.empty())) resp.sendRedirect("/liked");
      else {
        Optional<User> me = userDao.getUserFromCookie(req, "login");
        Optional<User> otherUser = userDao.getUserFromCookie(req, "message");

        if (otherUser.equals(Optional.empty())) resp.sendRedirect("/liked");
        else {
          messageDao.addMessage(me.get(), otherUser.get(), text);
          userDao.updateLastSeen(me.get());
          handleMessages(me.get(), otherUser.get(), resp);
        }
      }
    } catch (IOException e) {
      log.warn(String.format("Redirecting from messages page to liked page failed: %s", e.getMessage()));
    }
  }

}
