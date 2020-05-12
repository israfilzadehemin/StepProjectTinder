package app.entities;

public class Message {
  private int id;
  private int from;
  private int to;
  private String body;
  private String time;

  public Message(int id, int from, int to, String body,String time) {
    this.id = id;
    this.from = from;
    this.to = to;
    this.body = body;
    this.time = time;
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

  public String getTime() {
    return time;
  }
}
