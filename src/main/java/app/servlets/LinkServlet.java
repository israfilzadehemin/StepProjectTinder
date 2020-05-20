package app.servlets;

import lombok.SneakyThrows;
import org.eclipse.jetty.servlet.Source;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LinkServlet extends HttpServlet {
  private final String subpath;

  public LinkServlet(String subpath) {
    this.subpath = subpath;
  }

  @SneakyThrows
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    String filename = req.getPathInfo();
    String fileLoc = "content";
    Path path = Paths.get(fileLoc, subpath, filename);

    try (OutputStream os = resp.getOutputStream()) {
      Files.copy(path, os);
    }
  }


}
