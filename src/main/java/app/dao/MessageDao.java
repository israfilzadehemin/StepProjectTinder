package app.dao;

import app.entities.Message;
import app.entities.User;
import app.tools.ConnectionTool;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
