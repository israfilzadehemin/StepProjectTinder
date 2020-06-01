package app.dao;

import app.entities.User;
import app.tools.ConnectionTool;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;

public class LikeDao {

  ConnectionTool connectionTool = new ConnectionTool();

  public List<User> getVisitedUsers(User user, String action) {
    return connectionTool.getVisitedUsers(user, action);
  }

  public void addAction(User from, User to, String action) {
    connectionTool.addAction(from, to, action);
  }

  public void deleteAction(User from, User to) {
    connectionTool.deleteAction(from, to);
  }

  public Optional<User> getRandomUnvisitedUser(User user) {
    return connectionTool.getRandomUnvisitedUser(user);
  }

}
