package app.entities;

public class Message {
  private int id;
  private int from;
  private int to;
  private String body;

  public Message(int id, int from, int to, String body) {
    this.id = id;
    this.from = from;
    this.to = to;
    this.body = body;
  }

  public int getId() {
    return id;
  }

  public int getFrom() {
    return from;
  }

  public int getTo() {
    return to;
  }

  public String getBody() {
    return body;
  }
}
