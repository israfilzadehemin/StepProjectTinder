package app.servlets;

import app.tools.CookieFilter;
import app.dao.UserDao;
import app.tools.EncodingTool;
import app.tools.TemplateEngine;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Log4j2
public class LoginServlet extends HttpServlet {
  private final TemplateEngine engine;

  public LoginServlet(TemplateEngine engine) {
    this.engine = engine;
  }

  UserDao userDao = new UserDao();
  EncodingTool encodingTool = new EncodingTool();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    try {
      HashMap<String, Object> data = new HashMap<>();
      data.put("error", "noerror");

      //User is not logged yet
      CookieFilter cookieFilter = new CookieFilter();
      if (!cookieFilter.isLogged(req))
        engine.render("login.ftl", data, resp);

        //User logged
      else resp.sendRedirect("/users");
    } catch (IOException e) {
      log.warn(String.format("Redirecting from login page to users page failed: %s", e.getMessage()));
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    try {
      //User inputs
      String mail = req.getParameter("email");
      String password = req.getParameter("password");

      //Input value checking
      if (userDao.checkUser(mail, password)) {
        Cookie loginCk = new Cookie("login", String.format("%s", encodingTool.encrypt(mail)));
        loginCk.setMaxAge(60 * 60 * 24);
        resp.addCookie(loginCk);

        resp.sendRedirect("/users");
      } else {
        HashMap<String, Object> data = new HashMap<>();
        data.put("error", "wrongUser");
        engine.render("login.ftl", data, resp);
      }
    } catch (IOException e) {
      log.warn(String.format("Redirecting from login page to users page failed: %s", e.getMessage()));
    }
  }

}
