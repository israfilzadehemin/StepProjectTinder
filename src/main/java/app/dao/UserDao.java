package app.dao;

import app.entities.User;
import app.tools.ConnectionTool;
import lombok.SneakyThrows;

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


  @SneakyThrows
  public UserDao() {
    users.addAll(connectionTool.getUsers());
  }


  public List<User> getAllUsers() {
    return connectionTool.getUsers();
  }

  public Optional<User> getUserById(int id) {
    return users.stream()
            .filter(u -> u.getId() == id)
            .findFirst();
  }

  public boolean checkUser(String mail, String password) {
    return connectionTool.getUsers().stream()
            .anyMatch(u -> u.getMail().equals(mail) && u.getPassword().equals(password));
  }

  public boolean checkDuplicate(String username, String mail) {
    return users.stream()
            .anyMatch(user -> user.getUsername().equals(username) || user.getMail().equals(mail));
  }

  public Optional<User> getUserFromCookie(HttpServletRequest request, String cookieName) {
    Cookie[] cookies = request.getCookies();

    Optional<String> mail = Arrays.stream(cookies)
            .filter(l -> l.getName().equals(cookieName))
            .map(Cookie::getValue)
            .findFirst();

    return getAllUsers().stream()
            .filter(u -> u.getMail().equals(mail.get()))
            .findFirst();
  }

  public void addUser(String username, String fullname, String mail, String password, String profilePic) {
    connectionTool.addUser(username, fullname, mail, password, profilePic);
  }

  public void updateLastSeen(User user) throws SQLException {
    connectionTool.updateLastSeen(user);
  }

}
