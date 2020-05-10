package app.dao;

import app.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
  List<User> users = new ArrayList<>();

  public List<User> getAllUsers() {
    return users;
  }

  public User getById(int id) {
    return users.get(id-1);
  }

  public void addUser(User user) {
    users.add(user);
  }

  public boolean checkUser(String mail, String password) {
    return users.stream().anyMatch(u -> u.getMail().equals(mail) && u.getPassword().equals(password));
  }

}
