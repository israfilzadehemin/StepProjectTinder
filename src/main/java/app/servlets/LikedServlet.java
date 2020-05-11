package app.servlets;

import app.entities.User;
import app.tools.ConnectionTool;
import app.tools.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
      connTool.addOnline(currentUser);
      data.put("liked", connTool.getLikedUsers(currentUser));

    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
    engine.render("people-list.ftl", data, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String btn = req.getParameter("msg");
    Cookie msgId = new Cookie("message", String.format("%s",btn));
    msgId.setMaxAge(60*60);
    resp.addCookie(msgId);

    try {
      connTool.addOnline(connTool.getUserFromCookie(req));
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }

    resp.sendRedirect("/messages");
  }
}
