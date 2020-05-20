package app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Message {
  private int id;
  private int from;
  private int to;
  private String body;
  private String time;

}
