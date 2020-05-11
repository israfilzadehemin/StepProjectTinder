package app.servlets;

import app.dao.UserDao;
import app.tools.ConnectionTool;
import app.tools.CookieFilter;
import app.tools.TemplateEngine;

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

  public RegistrationServlet() throws IOException {
  }

  ConnectionTool connTool = new ConnectionTool();
  UserDao userDao = new UserDao();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HashMap<String, Object> data = new HashMap<>();
    data.put("error", "noerror");

    CookieFilter cookieFilter = new CookieFilter();
    if (!cookieFilter.isLogged(req, resp))
      engine.render("registration.ftl", data, resp);
    else resp.sendRedirect("/users");
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String fullname = req.getParameter("fullname");
    String username = req.getParameter("username").replaceAll(" ", "");
    String mail = req.getParameter("mail");
    String password = req.getParameter("password");
    String passCon = req.getParameter("passCon");
    String profilePic = req.getParameter("profilePic");

    try {
      userDao.getAllUsers().addAll(connTool.getUsers());
      if (userDao.checkDuplicate(username, mail)) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("error", "duplicate");
        engine.render("registration.ftl", data, resp);
      } else if (!password.equals(passCon)) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("error", "noMatch");
        engine.render("registration.ftl", data, resp);

      } else {
        HashMap<String, Object> data = new HashMap<>();
        data.put("error", "sucessful");
        String picName = uploadFile(req, username);
        connTool.addUser(username, fullname, mail, password, picName);

        resp.sendRedirect("/login");
      }
    } catch (SQLException | ServletException sqlException) {
      sqlException.printStackTrace();
    }


  }

  String uploadFile(HttpServletRequest req, String username) throws IOException, ServletException {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
    String formatDateTime = now.format(format);
    StringBuilder fileNameBuilder = new StringBuilder();

    Part p = req.getPart("profilePic");
    InputStream partIS = p.getInputStream();
    String fileSubName = p.getSubmittedFileName();
    String filename = String.format("%s%s%s", formatDateTime, username, fileSubName);
    Files.copy(partIS, Paths.get("content/img/" + filename),
            StandardCopyOption.REPLACE_EXISTING);
    fileNameBuilder.append("img/").append(filename);

    return fileNameBuilder.toString();
  }
}
