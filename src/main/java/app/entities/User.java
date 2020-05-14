package app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class User {
  private int id;
  private String username;
  private String fullName;
  private String mail;
  private String password;
  private String profilePic;
  private String lastLogin;


}
