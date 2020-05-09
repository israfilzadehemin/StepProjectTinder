package app;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class LoginServlet extends HttpServlet {
  private final TemplateEngine engine;

  public LoginServlet(TemplateEngine engine) {
    this.engine = engine;
  }


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    HashMap<String, Object> data = new HashMap<>();
    engine.render("login.ftl", data, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

  }
}
