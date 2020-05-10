package app.entities;

import java.util.ArrayList;
import java.util.List;

public class User {
  private int id;
  private String username;
  private String mail;
  private String password;
  private String profilePic;
  private List<Integer> likesId = new ArrayList<>();//can be deleted
  private List<Message> messages = new ArrayList<>();

  public User(int id, String username, String mail, String password, String profilePic) {
    this.id = id;
    this.username = username;
    this.mail = mail;
    this.password = password;
    this.profilePic = profilePic;
  }

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
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

  @Override
  public String toString() {
    return String.format("User{id=%d, username='%s', mail='%s', password='%s', likesId=%s, messages=%s}", id, username, mail, password, likesId, messages);
  }
}
