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

  public List<User> getLikedUsers(User user) throws SQLException {
    return connectionTool.getLikedUsers(user);
  }

  public void addLike(User from, User to) throws SQLException {
    connectionTool.addLike(from, to);
  }

  public void deleteLike(User from, User to) throws SQLException {
    connectionTool.deleteLike(from, to);
  }

  public Optional<User> getRandomUnlikedUser(User user) throws SQLException {
    return connectionTool.getRandomUnlikedUser(user);
  }


}
