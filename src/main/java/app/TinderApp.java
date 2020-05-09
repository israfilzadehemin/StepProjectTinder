package app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import javax.servlet.Servlet;
import java.util.EnumSet;

public class TinderApp {
  public static void main(String[] args) throws Exception {

    //Configuration
    Server server = new Server(9090);
    ServletContextHandler handler = new ServletContextHandler();
    TemplateEngine engine = TemplateEngine.folder("content");

    //Servlets
    handler.addServlet(new ServletHolder(new LoginServlet(engine)), "/login/*");
    handler.addServlet(new ServletHolder(new UserServlet(engine)), "/users/*");
    handler.addServlet(new ServletHolder(new LikedServlet(engine)), "/liked/*");
    handler.addServlet(new ServletHolder(new MessagesServlet(engine)), "/messages/*");
    handler.addServlet(new ServletHolder(new LinkServlet("css")), "/css/*");


    //Filters
    handler.addFilter(CookieFilter.class, "/messages/*", EnumSet.of(DispatcherType.REQUEST));
    handler.addFilter(CookieFilter.class, "/users/*", EnumSet.of(DispatcherType.REQUEST));
    handler.addFilter(CookieFilter.class, "/liked/*", EnumSet.of(DispatcherType.REQUEST));


    server.setHandler(handler);
    server.start();
    server.join();

  }
}
