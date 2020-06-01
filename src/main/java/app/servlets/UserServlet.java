package app.servlets;

import app.entities.User;
import app.dao.LikeDao;
import app.dao.MessageDao;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

@Log4j2
public class UserServlet extends HttpServlet {
  private final TemplateEngine engine;

  public UserServlet(TemplateEngine engine) {
    this.engine = engine;
  }

  UserDao userDao = new UserDao();
  MessageDao messageDao = new MessageDao();
  LikeDao likeDao = new LikeDao();
  EncodingTool encodingTool = new EncodingTool();

  private void showUser(HttpServletResponse resp, Optional<User> me) {
    //Getting random user to make an action
    Optional<User> showingUser = likeDao.getRandomUnvisitedUser(me.get());

    try {
      //Checking whether user is found
      if (showingUser.equals(Optional.empty())) resp.sendRedirect("/liked");
      else {
        Cookie cookie = new Cookie("clicked", String.format("%s", encodingTool.encrypt(showingUser.get().getMail())));
        cookie.setMaxAge(60 * 60);
        resp.addCookie(cookie);

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", showingUser.get());

        userDao.updateLastSeen(me.get());
        engine.render("like-page.ftl", data, resp);
      }
    } catch (IOException e) {
      log.warn(String.format("Redirecting from user page to liked page failed: %s", e.getMessage()));
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    //Denying access to messages after leaving chat menu
    messageDao.removeMessageAccess(req, resp);

    //Showing user to make an action
    Optional<User> me = userDao.getUserFromCookie(req, "login");
    showUser(resp, me);

  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    //Getting action button value
    String btn = req.getParameter("button");

    Optional<Cookie> clicked = Arrays.stream(req.getCookies())
            .filter(c -> c.getName().equals("clicked"))
            .findFirst();

    try {
      //Checking whether button is clicked
      if (clicked.equals(Optional.empty())) resp.sendRedirect("/users");
      else {
        Optional<User> me = userDao.getUserFromCookie(req, "login");
        Optional<User> clickedUser = userDao.getUserFromCookie(req, "clicked");

        likeDao.addAction(me.get(), clickedUser.get(), btn);
        showUser(resp, me);
      }
    } catch (IOException e) {
      log.warn(String.format("Redirecting from user POST page to user GET failed: %s", e.getMessage()));
    }
  }
}

