package app.entities;

public class User {
  private int id;
  private String Username;
  private String mail;
  private String password;
  private int likesId;
  private int messagesFrom;
  private int messagesTo;

  public int getId() {
    return id;
  }

  public String getUsername() {
    return Username;
  }

  public String getMail() {
    return mail;
  }

  public String getPassword() {
    return password;
  }

  public int getLikesId() {
    return likesId;
  }

  public int getMessagesFrom() {
    return messagesFrom;
  }

  public int getMessagesTo() {
    return messagesTo;
  }
}
