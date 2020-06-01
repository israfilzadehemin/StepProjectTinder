package app.servlets;

import app.dao.UserDao;
import app.tools.CookieFilter;
import app.tools.TemplateEngine;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Log4j2
public class RegistrationServlet extends HttpServlet {
  TemplateEngine engine = TemplateEngine.folder("content");

  public RegistrationServlet() {
  }

  UserDao userDao = new UserDao();

  String uploadProfilePic(HttpServletRequest req, String username) {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
    String formattedNow = now.format(format);
    StringBuilder fileNameBuilder = new StringBuilder();

    try {
      Part p = req.getPart("profilePic");
      InputStream partIS = p.getInputStream();
      String fileSubName = p.getSubmittedFileName();
      String filename = String.format("%s%s%s", formattedNow, username, fileSubName);
      Files.copy(partIS, Paths.get("content/img/" + filename),
              StandardCopyOption.REPLACE_EXISTING);
      fileNameBuilder.append("img/").append(filename);

    } catch (ServletException e) {
      log.warn(String.format("Servlet exception happened in method uploadProfilePicture(): %s", e.getMessage()));
    } catch (IOException e) {
      log.warn(String.format("IOexception happened in method uploadProfilePicture(): %s", e.getMessage()));
    }
    return fileNameBuilder.toString();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    HashMap<String, Object> data = new HashMap<>();
    data.put("error", "noerror");

    //User is not logged yet
    CookieFilter cookieFilter = new CookieFilter();
    if (!cookieFilter.isLogged(req))
      engine.render("reg.ftl", data, resp);

      //User logged
    else {
      try {
        resp.sendRedirect("/users");
      } catch (IOException e) {
        log.warn(String.format("Redirecting from registration page to users page failed: %s", e.getMessage()));
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    //User inputs
    String fullname = req.getParameter("fullname");
    String username = req.getParameter("username").replaceAll(" ", "");
    String mail = req.getParameter("email");
    String password = req.getParameter("password");
    String passCon = req.getParameter("passCon");

    System.out.println(fullname);

    //Input value checking
    if (userDao.checkDuplicate(username, mail)) {
      HashMap<String, Object> data = new HashMap<>();
      data.put("error", "duplicate");
      engine.render("reg.ftl", data, resp);
    } else if (!password.equals(passCon)) {
      HashMap<String, Object> data = new HashMap<>();
      data.put("error", "noMatch");
      engine.render("reg.ftl", data, resp);

    } else {
      String picName = uploadProfilePic(req, username);
      userDao.addUser(username, fullname, mail, password, picName);
      try {
        resp.sendRedirect("/login");
      } catch (IOException e) {
        log.warn(String.format("Redirecting from registration page to login page failed: %s", e.getMessage()));
      }
    }
  }

}
