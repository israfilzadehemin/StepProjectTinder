package app.dao;

import app.entities.Message;
import app.entities.User;
import app.tools.ConnectionTool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LikeDao {

  ConnectionTool connectionTool = new ConnectionTool();
  List<Message> likes = new ArrayList<>(); // can be filled with all likes

  public List<User> getVisitededUsers(User user, String action) throws SQLException {
    return connectionTool.getVisitedUsers(user, action);
  }

  public void addAction(User from, User to, String action) throws SQLException {
    connectionTool.addAction(from, to, action);
  }

  public void deleteAction(User from, User to) throws SQLException {
    connectionTool.deleteAction(from, to);
  }

  public Optional<User> getRandomUnvisitedUser(User user) throws SQLException {
    return connectionTool.getRandomUnvisitedUser(user);
  }

  public String findAction(User fromWhom, User toWhom) throws SQLException {
    return connectionTool.findAction(fromWhom, toWhom);
  }


}
