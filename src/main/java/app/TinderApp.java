package app;

import app.servlets.*;
import app.tools.CookieFilter;
import app.tools.TemplateEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.EnumSet;

public class TinderApp {
  public static void main(String[] args) throws Exception {

    //Configuration
    Server server = new Server(9090);
    ServletContextHandler handler = new ServletContextHandler();
    TemplateEngine engine = TemplateEngine.folder("content");

    //Servlets
    handler.addServlet(new ServletHolder(new LoginServlet(engine)), "/login/*");
    handler.addServlet(new ServletHolder(new LogoutServlet()), "/logout/*");
    handler.addServlet(new ServletHolder(new UserServlet(engine)), "/users/*");
    handler.addServlet(new ServletHolder(new LikedServlet(engine)), "/liked/*");
    handler.addServlet(new ServletHolder(new MessagesServlet(engine)), "/messages/*");
    handler.addServlet(new ServletHolder(new LinkServlet("css")), "/css/*");
    handler.addServlet(new ServletHolder(new LinkServlet("img")), "/img/*");


    //Filters
    handler.addFilter(CookieFilter.class, "/messages/*", EnumSet.of(DispatcherType.REQUEST));
    handler.addFilter(CookieFilter.class, "/users/*", EnumSet.of(DispatcherType.REQUEST));
    handler.addFilter(CookieFilter.class, "/liked/*", EnumSet.of(DispatcherType.REQUEST));
    handler.addFilter(CookieFilter.class, "/logout/*", EnumSet.of(DispatcherType.REQUEST));

    server.setHandler(handler);
    server.start();
    server.join();

  }
}
