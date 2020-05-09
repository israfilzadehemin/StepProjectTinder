package app;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class UserServlet extends HttpServlet {
  private final TemplateEngine engine;

  public UserServlet(TemplateEngine engine) {
    this.engine = engine;
  }


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    HashMap<String, Object> data = new HashMap<>();
    engine.render("like-page.ftl", data, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

  }
}
