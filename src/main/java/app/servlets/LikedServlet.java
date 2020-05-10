package app.servlets;

import app.entities.User;
import app.tools.ConnectionTool;
import app.tools.TemplateEngine;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;

public class LikedServlet extends HttpServlet {
  private final TemplateEngine engine;

  public LikedServlet(TemplateEngine engine) {
    this.engine = engine;
  }

  ConnectionTool connTool = new ConnectionTool();


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    HashMap<String, Object> data = new HashMap<>();

    try {
      User currentUser = connTool.getUserFromCookie(req);
      data.put("liked", connTool.getLikedPeople(currentUser));

    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
    engine.render("people-list.ftl", data, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

  }
}
