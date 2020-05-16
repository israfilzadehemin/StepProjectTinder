package app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Like {
  private final int id;
  private final int fromUser;
  private final int toUser;
  private final String action;
}
