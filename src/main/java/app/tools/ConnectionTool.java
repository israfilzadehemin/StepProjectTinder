package app.tools;

import app.dao.UserDao;
import app.entities.Message;
import app.entities.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionTool {
  private final static String URL = "jdbc:postgresql://localhost:5432/Tinder";
  private final static String USER = "postgres";
  private final static String PASS = "xiaominote9";

  UserDao userDao = new UserDao();

  public List<User> getUsers() throws SQLException {

    Connection conn = DriverManager.getConnection(URL, USER, PASS);
    String sqlUser = "select * from users order by id asc";
    PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
    ResultSet rsetUser = stmtUser.executeQuery();

    List<User> users = new ArrayList<>();

    while (rsetUser.next()) {
      int id = rsetUser.getInt("id");
      String username = rsetUser.getString("username");
      String mail = rsetUser.getString("mail");
      String password = rsetUser.getString("password");
      String profilePic = rsetUser.getString("profile_pic");

      users.add(new User(id, username, mail, password, profilePic));
    }

    String sqlMessage = "select * from users s inner join messages m on (s.id=m.\"fromUser\")";
    PreparedStatement stmtMessage = conn.prepareStatement(sqlMessage);
    ResultSet rsetMessage = stmtMessage.executeQuery();

    while (rsetMessage.next()) {
      int id = rsetMessage.getInt("id");
      int from = rsetMessage.getInt("fromUser");
      int to = rsetMessage.getInt("toUser");
      String body = rsetMessage.getString("body");

      userDao.getById(from).getMessages().add(new Message(id, from, to, body));
    }

    conn.close();
    return users;
  }

  public List<User> getLikedPeople(User user) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);
    List<User> allUsers = new ArrayList<>(getUsers());
    List<User> likedPeople = new ArrayList<>();

    String sqlLike = "select * from likes where \"fromUser\"=?";

    PreparedStatement stmtLike = conn.prepareStatement(sqlLike);
    stmtLike.setInt(1, user.getId());
    ResultSet rsetLike = stmtLike.executeQuery();

    while (rsetLike.next()) {
      int to = rsetLike.getInt("toUser");
      allUsers.stream().filter(user1 -> user1.getId() == to).forEach(likedPeople::add);
    }
    conn.close();
    return likedPeople;

  }

  public Optional<User> getUnivisited(User user) throws SQLException {

    List<User> allUsers = new ArrayList<>(getUsers());
    List<User> likedUsers = getLikedPeople(user);
    List<User> visited = new ArrayList<>();

    if (likedUsers.size() > 0) {
      allUsers.forEach(user1 ->
              likedUsers.stream().
                      filter(liked -> user1.getId() == liked.getId() || user1.getId() == user.getId())
                      .map(id -> user1)
                      .forEach(visited::add));

    }
    else {
      allUsers.stream().filter(user1 -> user.getId()==user1.getId())
                      .forEach(visited::add);
    }

    allUsers.removeAll(visited);
    List<User> unvisited = new ArrayList<>(allUsers);

    if (unvisited.size() > 0) {
      int r = new Random().nextInt(unvisited.size());
      return Optional.of(unvisited.get(r));
    } else return Optional.empty();
  }

  public User getUserFromCookie(HttpServletRequest request) throws SQLException {
    userDao.getAllUsers().addAll(getUsers());
    Cookie[] cookies = request.getCookies();

    String mail = Arrays.stream(cookies)
            .filter(l -> l.getName().equals("login"))
            .map(Cookie::getValue)
            .findFirst()
            .orElseThrow(RuntimeException::new);

    return userDao.getAllUsers().stream()
            .filter(u -> u.getMail().equals(mail))
            .findFirst()
            .orElseThrow(RuntimeException::new);
  }

  public void addLike(User fromWhom, User toWhom) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    int fromUser = fromWhom.getId();
    int toUser = toWhom.getId();

    String sqlUser = "insert into likes (\"fromUser\", \"toUser\") VALUES (?, ?)";
    PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
    stmtUser.setInt(1, fromUser);
    stmtUser.setInt(2, toUser);
    stmtUser.execute();
    conn.close();
  }

}
