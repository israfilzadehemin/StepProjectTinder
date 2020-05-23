package app.servlets;

import app.tools.CookieFilter;
import app.dao.UserDao;
import app.tools.TemplateEngine;
import lombok.SneakyThrows;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class LoginServlet extends HttpServlet {
  private final TemplateEngine engine;

  public LoginServlet(TemplateEngine engine) {
    this.engine = engine;
  }

  UserDao userDao = new UserDao();

  @SneakyThrows
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    HashMap<String, Object> data = new HashMap<>();
    data.put("error", "noerror");

    //User is not logged yet
    CookieFilter cookieFilter = new CookieFilter();
    if (!cookieFilter.isLogged(req))
      engine.render("login.ftl", data, resp);

    //User logged
    else resp.sendRedirect("/users");
  }

  @SneakyThrows
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    //User inputs
    String mail = req.getParameter("email");
    String password = req.getParameter("password");

    //Input value checking
    if (userDao.checkUser(mail, password)) {
      Cookie loginCk = new Cookie("login", String.format("%s", mail));
      loginCk.setMaxAge(60 * 60 * 24);
      resp.addCookie(loginCk);

      resp.sendRedirect("/users");
    } else {
      HashMap<String, Object> data = new HashMap<>();
      data.put("error", "wrongUser");
      engine.render("login.ftl", data, resp);
    }
  }

}
