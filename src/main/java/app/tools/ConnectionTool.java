package app.tools;

import app.entities.Like;
import app.entities.Message;
import app.entities.User;
import lombok.SneakyThrows;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class ConnectionTool {
  private final static String URL = "jdbc:postgresql://localhost:5432/Tinder";
  private final static String USER = "postgres";
  private final static String PASS = "xiaominote9";


  @SneakyThrows
  public List<User> getUsers() {
    List<User> users = new ArrayList<>();

    Connection conn = DriverManager.getConnection(URL, USER, PASS);
    PreparedStatement ps = conn.prepareStatement("select * from users order by id asc");
    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
      int id = rs.getInt("id");
      String username = rs.getString("username");
      String mail = rs.getString("mail");
      String password = rs.getString("password");
      String profilePic = rs.getString("profile_pic");
      String lastLogin = rs.getString("last_seen");
      String fullName = rs.getString("fullname");

      users.add(new User(id, username, fullName, mail, password, profilePic, lastLogin));
    }

    conn.close();
    return users;
  }

  @SneakyThrows
  public void addUser(String username, String fullname, String mail, String password, String profilePic) {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    PreparedStatement stmtMessage =
            conn.prepareStatement("insert into users" +
                    "(username, mail, password, profile_pic, fullname, last_seen) " +
                    "values (?, ?, ?, ?, ?, ?)");

    stmtMessage.setString(1, username);
    stmtMessage.setString(2, mail);
    stmtMessage.setString(3, password);
    stmtMessage.setString(4, profilePic);
    stmtMessage.setString(5, fullname);
    stmtMessage.setString(6, "No login");
    stmtMessage.execute();

  }

  @SneakyThrows
  public List<User> getVisitedUsers(User user, String action) {
    List<User> allUsers = new ArrayList<>(getUsers());
    List<User> visitedUsers = new ArrayList<>();

    Connection conn = DriverManager.getConnection(URL, USER, PASS);
    String sqlLike;
    PreparedStatement stmtLike;


    if (action.equals("all")) {
      sqlLike = "select * from likes where sender_id=?";
      stmtLike = conn.prepareStatement(sqlLike);
      stmtLike.setInt(1, user.getId());

    } else {
      sqlLike = "select * from likes where sender_id=? and action=?";
      stmtLike = conn.prepareStatement(sqlLike);
      stmtLike.setInt(1, user.getId());
      stmtLike.setString(2, action);

    }

    ResultSet rs = stmtLike.executeQuery();

    while (rs.next()) {
      int to = rs.getInt("receiver_id");
      allUsers.stream().
              filter(u -> u.getId() == to)
              .forEach(visitedUsers::add);
    }
    conn.close();
    return visitedUsers;
  }

  @SneakyThrows
  public List<Like> getAllActions() {
    List<Like> allActions = new ArrayList<>();
    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    PreparedStatement ps = conn.prepareStatement("select * from likes");
    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
      int id = rs.getInt("id");
      int from = rs.getInt("sender_id");
      int to = rs.getInt("receiver_id");
      String action = rs.getString("action");

      allActions.add(new Like(id, from, to, action));
    }
    conn.close();
    return allActions;
  }

  @SneakyThrows
  public Optional<User> getRandomUnvisitedUser(User user) {
    List<User> allUsers = new ArrayList<>(getUsers());
    List<User> visitedUsers = new ArrayList<>();

    Connection connection = DriverManager.getConnection(URL, USER, PASS);

    PreparedStatement ps = connection.prepareStatement("select * from likes where sender_id=?");
    ps.setInt(1, user.getId());
    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
      int to = rs.getInt("receiver_id");
      allUsers.stream()
              .filter(u -> u.getId() == to)
              .forEach(visitedUsers::add);
    }

    allUsers.remove(allUsers.stream().filter(u -> u.getId() == user.getId()).findFirst().get());
    allUsers.removeAll(visitedUsers);

    List<User> unvisitedUsers = new ArrayList<>(allUsers);

    if (unvisitedUsers.size() == 0) return Optional.empty();
    else {
      int randomIndex = new Random().nextInt(unvisitedUsers.size());
      return Optional.of(unvisitedUsers.get(randomIndex));
    }

  }

  @SneakyThrows
  public boolean addAction(User sender, User receiver, String action) {
    List<Like> allActions = getAllActions();
    boolean isDuplicate = allActions.stream()
            .anyMatch(l -> l.getFromUser() == sender.getId() && l.getToUser() == receiver.getId());

    if (isDuplicate) return false;

    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    PreparedStatement ps = conn.prepareStatement("insert into likes " +
            "(sender_id, receiver_id, \"action\" ) VALUES (?, ?, ?)");

    ps.setInt(1, sender.getId());
    ps.setInt(2, receiver.getId());
    ps.setString(3, action);

    ps.execute();
    conn.close();
    return true;
  }

  @SneakyThrows
  public void deleteAction(User sender, User receiver) {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    PreparedStatement ps =
            conn.prepareStatement("delete from likes where sender_id=? and receiver_id=?");
    ps.setInt(1, sender.getId());
    ps.setInt(2, receiver.getId());

    ps.execute();
    conn.close();

  }

  @SneakyThrows
  public void updateLastSeen(User user) {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    String formattedNow = now.format(format);

    PreparedStatement stmtUser =
            conn.prepareStatement("update users set last_seen = ? where id=?");

    stmtUser.setString(1, formattedNow);
    stmtUser.setInt(2, user.getId());
    stmtUser.execute();
    conn.close();
  }

  @SneakyThrows
  public List<Message> getMessages(User sender, User receiver) {
    List<Message> messages = new ArrayList<>();

    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    PreparedStatement ps =
            conn.prepareStatement("select * from messages where sender_id = ? and receiver_id = ?");
    ps.setInt(1, sender.getId());
    ps.setInt(2, receiver.getId());

    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
      int id = rs.getInt("id");
      int from = rs.getInt("sender_id");
      int to = rs.getInt("receiver_id");
      String body = rs.getString("body");
      String time = rs.getString("time");

      messages.add(new Message(id, from, to, body, time));
    }
    return messages;
  }

  @SneakyThrows
  public void addMessage(User sender, User receiver, String body) {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    PreparedStatement ps = conn.prepareStatement("insert into messages " +
            "(sender_id, receiver_id, body, \"time\") values (?,?,?,?)");

    ps.setInt(1, sender.getId());
    ps.setInt(2, receiver.getId());

    String text = body;
    if (text.matches("^\\s*$")) text = "empty message";
    ps.setString(3, text);

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    String formattedNow = now.format(format);

    ps.setString(4, formattedNow);
    ps.execute();

    conn.close();
  }

}
