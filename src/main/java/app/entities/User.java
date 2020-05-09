package app.entities;

import java.util.ArrayList;
import java.util.List;

public class User {
  private int id;
  private String username;
  private String mail;
  private String password;
  private String profilePic;
  private List<Integer> likesId = new ArrayList<>();
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

  public void setId(int id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setLikesId(List<Integer> likesId) {
    this.likesId = likesId;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public String getProfilePic() {
    return profilePic;
  }

  public void setProfilePic(String profilePic) {
    this.profilePic = profilePic;
  }

  @Override
  public String toString() {
    return String.format("User{id=%d, username='%s', mail='%s', password='%s', likesId=%s, messages=%s}", id, username, mail, password, likesId, messages);
  }
}
