package app.tools;

import app.entities.Like;
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

  public void addUser(String username, String fullname, String mail, String password, String profilePic) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);
    String sqlMessage = "insert into users (username, mail, password, profile_pic, fullname, \"lastLogin\")\n" +
            "values (?, ?, ?, ?, ?, ?)\n";
    PreparedStatement stmtMessage = conn.prepareStatement(sqlMessage);

    stmtMessage.setString(1, username);
    stmtMessage.setString(2, mail);
    stmtMessage.setString(3, password);
    stmtMessage.setString(4, profilePic);
    stmtMessage.setString(5, fullname);
    stmtMessage.setString(5, "No login");
    stmtMessage.execute();

  }

  public List<User> getVisitedUsers(User user, String action) throws SQLException {
    List<User> allUsers = new ArrayList<>(getUsers());
    List<User> visitedUsers = new ArrayList<>();

    Connection conn = DriverManager.getConnection(URL, USER, PASS);
    String sqlLike;
    PreparedStatement stmtLike;


    if (action.equals("all")) {
      sqlLike = "select * from likes where \"fromUser\"=?";
      stmtLike = conn.prepareStatement(sqlLike);
      stmtLike.setInt(1, user.getId());

    } else {
      sqlLike = "select * from likes where \"fromUser\"=? and action=?";
      stmtLike = conn.prepareStatement(sqlLike);
      stmtLike.setInt(1, user.getId());
      stmtLike.setString(2, action);

    }

    ResultSet rsetLike = stmtLike.executeQuery();

    while (rsetLike.next()) {
      int to = rsetLike.getInt("toUser");
      allUsers.stream().filter(user1 -> user1.getId() == to).forEach(visitedUsers::add);
    }
    conn.close();
    return visitedUsers;
  }

  public List<Like> getAllActions() throws SQLException {
    List<Like> allActions = new ArrayList<>();
    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    String SQL = "select * from likes";
    PreparedStatement stmtUser = conn.prepareStatement(SQL);

    ResultSet resultSet = stmtUser.executeQuery();

    while (resultSet.next()) {
      int id = resultSet.getInt("id");
      int from = resultSet.getInt("fromUser");
      int to = resultSet.getInt("toUser");
      String action = resultSet.getString("action");

      allActions.add(new Like(id, from, to, action));
    }
    conn.close();

    return allActions;
  }

  public Optional<User> getRandomUnvisitedUser(User user) throws SQLException {
    List<User> allUsers = new ArrayList<>(getUsers());
    List<User> visitedUsers = new ArrayList<>();

    Connection connection = DriverManager.getConnection(URL, USER, PASS);

    String SQL = "select * from likes where \"fromUser\"=?";
    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
    preparedStatement.setInt(1, user.getId());
    ResultSet resultSet = preparedStatement.executeQuery();

    while (resultSet.next()) {
      int to = resultSet.getInt("toUser");
      allUsers.stream().filter(u -> u.getId() == to).forEach(visitedUsers::add);
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

  public boolean addAction(User fromWhom, User toWhom, String action) throws SQLException {
    List<Like> allActions = getAllActions();
    boolean isDuplicate = allActions.stream()
            .anyMatch(l -> l.getFromUser() == fromWhom.getId() && l.getToUser() == toWhom.getId());

    if (isDuplicate) return false;


    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    String sqlUser = "insert into likes (\"fromUser\", \"toUser\", \"action\" ) VALUES (?, ?, ?)";
    PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
    stmtUser.setInt(1, fromWhom.getId());
    stmtUser.setInt(2, toWhom.getId());
    stmtUser.setString(3, action);

    stmtUser.execute();
    conn.close();
    return true;
  }

  public void deleteAction(User fromWhom, User toWhom) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);

    String sqlUser = "delete from likes where \"fromUser\"=? and \"toUser\"=?";
    PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
    stmtUser.setInt(1, fromWhom.getId());
    stmtUser.setInt(2, toWhom.getId());

    stmtUser.execute();
    conn.close();

  }

  public void updateLastSeen(User user) throws SQLException {
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
      String time = rsetMessage.getString("time");

      messages.add(new Message(id, from, to, body, time));
    }
    return messages;
  }

  public void addMessage(User sender, User receiver, String body) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USER, PASS);
    String sqlMessage = "insert into messages (\"fromUser\", \"toUser\", body, \"time\") values (?,?,?,?)";
    PreparedStatement stmtMessage = conn.prepareStatement(sqlMessage);

    stmtMessage.setInt(1, sender.getId());
    stmtMessage.setInt(2, receiver.getId());

    String text = body;
    if (text.matches("^\\s*$")) text = "empty message";
    stmtMessage.setString(3, text);

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    String formatDateTime = now.format(format);

    stmtMessage.setString(4, formatDateTime);
    stmtMessage.execute();

    conn.close();
  }

  public String findAction(User fromWhom, User toWhom) throws SQLException {
    Connection connection = DriverManager.getConnection(URL, USER, PASS);

    String SQL = "select * from likes where \"fromUser\"=? AND \"toUser\"=?";
    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
    preparedStatement.setInt(1, fromWhom.getId());
    preparedStatement.setInt(2, toWhom.getId());

    ResultSet resultSet = preparedStatement.executeQuery();

    String action="";
    while (resultSet.next()){

      action = resultSet.getString("action");
    }

    return action;
  }
}
