package app.dao;

import app.entities.User;
import app.tools.ConnectionTool;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;

public class LikeDao {

  ConnectionTool connectionTool = new ConnectionTool();

  @SneakyThrows
  public List<User> getVisitedUsers(User user, String action) {
    return connectionTool.getVisitedUsers(user, action);
  }

  @SneakyThrows
  public void addAction(User from, User to, String action) {
    connectionTool.addAction(from, to, action);
  }

  @SneakyThrows
  public void deleteAction(User from, User to) {
    connectionTool.deleteAction(from, to);
  }

  @SneakyThrows
  public Optional<User> getRandomUnvisitedUser(User user) {
    return connectionTool.getRandomUnvisitedUser(user);
  }

}
