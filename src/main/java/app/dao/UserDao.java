package app.dao;

import app.entities.User;
import app.tools.ConnectionTool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
  List<User> users = new ArrayList<>();
  ConnectionTool connectionTool = new ConnectionTool();

  public List<User> getAllUsers() {
    return users;
  }

  public User getById(int id) {
    return users.get(id-1);
  }

  public boolean checkUser(String mail, String password) {
    return users.stream().anyMatch(u -> u.getMail().equals(mail) && u.getPassword().equals(password));
  }

  public boolean checkDuplicate(String username, String mail) throws SQLException {
    users.addAll(connectionTool.getUsers());
    users.forEach(System.out::println);
    return users.stream().anyMatch(user -> user.getUsername().equals(username) || user.getMail().equals(mail));
  }

}
