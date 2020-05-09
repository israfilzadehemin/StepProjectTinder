package app.servlets;

import app.tools.TemplateEngine;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class MessagesServlet extends HttpServlet {
  private final TemplateEngine engine;

  public MessagesServlet(TemplateEngine engine) {
    this.engine = engine;
  }


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    HashMap<String, Object> data = new HashMap<>();
    engine.render("chat.ftl", data, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

  }
}
