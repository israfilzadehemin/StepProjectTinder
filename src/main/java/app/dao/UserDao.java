package app.dao;

import app.entities.User;
import app.tools.ConnectionTool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserDao {

  ConnectionTool connectionTool = new ConnectionTool();
  List<User> users = new ArrayList<>();


  public UserDao() throws SQLException {
    users.addAll(connectionTool.getUsers());
  }


  public List<User> getAllUsers() {
    return users;
  }

  public User getById(int id) {
    return users.stream()
            .filter(u -> u.getId() == id)
            .findFirst()
            .orElseThrow(RuntimeException::new);
  }

  public boolean checkUser(String mail, String password) {
    return users.stream()
            .anyMatch(u -> u.getMail().equals(mail) && u.getPassword().equals(password));
  }

  public boolean checkDuplicate(String username, String mail) throws SQLException {
    users.addAll(connectionTool.getUsers());
    return users.stream()
            .anyMatch(user -> user.getUsername().equals(username) || user.getMail().equals(mail));
  }

  public User getUserFromCookie(HttpServletRequest request) throws SQLException {
    Cookie[] cookies = request.getCookies();

    String mail = Arrays.stream(cookies)
            .filter(l -> l.getName().equals("login"))
            .map(Cookie::getValue)
            .findFirst()
            .orElseThrow(RuntimeException::new);

    return users.stream()
            .filter(u -> u.getMail().equals(mail))
            .findFirst()
            .orElseThrow(RuntimeException::new);
  }

  public void addUser(String username, String fullname, String mail, String password, String profilePic) throws SQLException {
    connectionTool.addUser(username, fullname, mail, password, profilePic);
  }

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

  public void addLastLogin(User user) throws SQLException {
    connectionTool.addLastLogin(user);
  }

  public void addOnline(User user) throws SQLException {
    connectionTool.addOnline(user);
  }
}
