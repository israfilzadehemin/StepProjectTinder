package app.dao;

import app.entities.Message;
import app.entities.User;
import app.tools.ConnectionTool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {

  ConnectionTool connectionTool = new ConnectionTool();
  List<Message> messages = new ArrayList<>(); // can be filled with all messages

  public List<Message> getMessages(User from, User to) throws SQLException {
    return connectionTool.getMessages(from, to);
  }

  public void addMessage(User from, User to, String text) throws SQLException {
    connectionTool.addMessage(from, to, text);
  }
}
