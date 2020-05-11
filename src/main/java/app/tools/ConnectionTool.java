package app.tools;

import app.entities.Message;
import app.entities.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class ConnectionTool {
  private final static String URL = "jdbc:postgresql://localhost:5432/Tinder";
  private final static String USER = "postgres";
  private final static String PASS = "xiaominote9";


  public List<User> getUsers() throws SQLException {
    List<User> users = new ArrayList<>();

    Connection conn = DriverManager.getConnection(URL, USER, PASS);
    String sqlUser = "select * from users order by id asc";
    PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
    ResultSet rsetUser = stmtUser.executeQuery();

    while (rsetUser.next()) {
      int id = rsetUser.getInt("id");
      String username = rsetUser.getString("username");
      String mail = rsetUser.getString("mail");
      String password = rsetUser.getString("password");
      String profilePic = rsetUser.getString("profile_pic");
      String lastLogin = rsetUser.getString("lastlogin");
      String fullName = rsetUser.getString("fullname");

      users.add(new User(id, username, fullName, mail, password, profilePic, lastLogin));
    }

    conn.close();
    return users;
  }

  public List<User> getLikedUsers(User user) throws SQLException {
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

  public List<Message> getMessages(User fromWhom, User toWhom) throws SQLException {
    List<Message> messages = new ArrayList<>();

    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    String sqlMessage = "select * from messages where \"fromUser\" = ? and \"toUser\" = ?";
    PreparedStatement stmtMessage = conn.prepareStatement(sqlMessage);
    stmtMessage.setInt(1, fromWhom.getId());
    stmtMessage.setInt(2, toWhom.getId());

    ResultSet rsetMessage = stmtMessage.executeQuery();

    while (rsetMessage.next()) {
      int id = rsetMessage.getInt("id");
      int from = rsetMessage.getInt("fromUser");
      int to = rsetMessage.getInt("toUser");
      String body = rsetMessage.getString("body");
      messages.add(new Message(id, from, to, body));
    }
    return messages;
  }

  public Optional<User> getUnliked(User user) throws SQLException {
    List<User> allUsers = new ArrayList<>(getUsers());
    List<User> likedUsers = getLikedUsers(user);
    List<User> liked = new ArrayList<>();

    if (likedUsers.size() > 0) {
      allUsers.forEach(user1 ->
              likedUsers.stream().
                      filter(l -> user1.getId() == l.getId() || user1.getId() == user.getId())
                      .map(id -> user1)
                      .forEach(liked::add));

    } else {
      allUsers.stream()
              .filter(user1 -> user.getId() == user1.getId())
              .forEach(liked::add);
    }

    allUsers.removeAll(liked);
    List<User> unliked = new ArrayList<>(allUsers);

    if (unliked.size() > 0) {
      int r = new Random().nextInt(unliked.size());
      return Optional.of(unliked.get(r));
    } else return Optional.empty();
  }

  public User getUserFromCookie(HttpServletRequest request) throws SQLException {
    Cookie[] cookies = request.getCookies();

    String mail = Arrays.stream(cookies)
            .filter(l -> l.getName().equals("login"))
            .map(Cookie::getValue)
            .findFirst()
            .orElseThrow(RuntimeException::new);

    return getUsers().stream()
            .filter(u -> u.getMail().equals(mail))
            .findFirst()
            .orElseThrow(RuntimeException::new);
  }

  public void addLike(User fromWhom, User toWhom) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    String sqlUser = "insert into likes (\"fromUser\", \"toUser\") VALUES (?, ?)";
    PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
    stmtUser.setInt(1, fromWhom.getId());
    stmtUser.setInt(2, toWhom.getId());

    stmtUser.execute();
    conn.close();
  }

  public void addMessage(User sender, User receiver, String body) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);
    String sqlMessage = "insert into messages (\"fromUser\", \"toUser\", body) values (?,?,?)";
    PreparedStatement stmtMessage = conn.prepareStatement(sqlMessage);

    stmtMessage.setInt(1, sender.getId());
    stmtMessage.setInt(2, receiver.getId());
    stmtMessage.setString(3, body);
    stmtMessage.execute();

    conn.close();
  }

  public void addLastLogin(User user) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    String formatDateTime = now.format(format);

    String sqlUser = "update users set lastlogin = ? where id=?";
    PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
    stmtUser.setString(1, formatDateTime);
    stmtUser.setInt(2, user.getId());

    stmtUser.execute();

    conn.close();
  }

  public void addOnline(User user) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    String sqlUser = "update users set lastlogin = ? where id=?";
    PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
    stmtUser.setString(1, "Online");
    stmtUser.setInt(2, user.getId());
    stmtUser.execute();

    conn.close();
  }

  public void addUser(String username, String fullname, String mail, String password, String profilePic) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);
    String sqlMessage = "insert into users (username, mail, password, profile_pic, fullname)\n" +
            "values (?, ?, ?, ?, ?)\n";
    PreparedStatement stmtMessage = conn.prepareStatement(sqlMessage);

    stmtMessage.setString(1, username);
    stmtMessage.setString(2, mail);
    stmtMessage.setString(3, password);
    stmtMessage.setString(4, profilePic);
    stmtMessage.setString(5, fullname);
    stmtMessage.execute();

  }
}
