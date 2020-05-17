package app.servlets;

import app.dao.UserDao;
import app.tools.CookieFilter;
import app.tools.TemplateEngine;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class RegistrationServlet extends HttpServlet {
  TemplateEngine engine = TemplateEngine.folder("content");

  @SneakyThrows
  public RegistrationServlet() {
  }

  UserDao userDao = new UserDao();

  @SneakyThrows
  String uploadProfilePic(HttpServletRequest req, String username) {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
    String formattedNow = now.format(format);
    StringBuilder fileNameBuilder = new StringBuilder();

    Part p = req.getPart("profilePic");
    InputStream partIS = p.getInputStream();
    String fileSubName = p.getSubmittedFileName();
    String filename = String.format("%s%s%s", formattedNow, username, fileSubName);
    Files.copy(partIS, Paths.get("content/img/" + filename),
            StandardCopyOption.REPLACE_EXISTING);
    fileNameBuilder.append("img/").append(filename);

    return fileNameBuilder.toString();
  }

  @SneakyThrows
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    HashMap<String, Object> data = new HashMap<>();
    data.put("error", "noerror");

    CookieFilter cookieFilter = new CookieFilter();
    if (!cookieFilter.isLogged(req))
      engine.render("reg.ftl", data, resp);
    else resp.sendRedirect("/users");
  }

  @SneakyThrows
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    String fullname = req.getParameter("fullname");
    String username = req.getParameter("username").replaceAll(" ", "");
    String mail = req.getParameter("email");
    String password = req.getParameter("password");
    String passCon = req.getParameter("passCon");

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
      resp.sendRedirect("/login");
    }
  }

}
