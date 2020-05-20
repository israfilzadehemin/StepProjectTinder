package app.dao;

import app.entities.Message;
import app.entities.User;
import app.tools.ConnectionTool;
import lombok.SneakyThrows;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MessageDao {

  ConnectionTool connectionTool = new ConnectionTool();

  @SneakyThrows
  public List<Message> getMessages(User from, User to) {
    return connectionTool.getMessages(from, to);
  }

  @SneakyThrows
  public void addMessage(User from, User to, String text) {
    connectionTool.addMessage(from, to, text);
  }

  public void removeMessageAccess(HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();

    Optional<Cookie> message = Arrays.stream(cookies)
            .filter(l -> l.getName().equals("message"))
            .findFirst();

    if (!message.equals(Optional.empty())) {
      message.get().setMaxAge(0);
      response.addCookie(message.get());
    }
  }
}
