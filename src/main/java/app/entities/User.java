package app.entities;

import java.util.ArrayList;
import java.util.List;

public class User {
  private int id;
  private String username;
  private String fullName;
  private String mail;
  private String password;
  private String profilePic;
  private List<Integer> likesId = new ArrayList<>();//can be deleted
  private List<Message> messages = new ArrayList<>();
  private String lastLogin;



  public User(int id, String username, String fullName, String mail, String password, String profilePic, String lastLogin) {
    this.id = id;
    this.username = username;
    this.fullName = fullName;
    this.mail = mail;
    this.password = password;
    this.profilePic = profilePic;
    this.lastLogin = lastLogin;
  }

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getFullName() {
    return fullName;
  }

  public String getMail() {
    return mail;
  }

  public String getPassword() {
    return password;
  }

  public List<Integer> getLikesId() {
    return likesId;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public String getProfilePic() {
    return profilePic;
  }

  public String getLastLogin() {
    return lastLogin;
  }

  @Override
  public String toString() {
    return String.format("User{id=%d, username='%s', fullName='%s', mail='%s', password='%s', profilePic='%s', likesId=%s, messages=%s, lastLogin='%s'}", id, username, fullName, mail, password, profilePic, likesId, messages, lastLogin);
  }
}
