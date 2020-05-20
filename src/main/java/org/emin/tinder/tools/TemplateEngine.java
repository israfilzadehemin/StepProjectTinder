package org.emin.tinder.tools;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class TemplateEngine {
  private final Configuration config;


  @SneakyThrows
  public TemplateEngine(String fullPath) {
    this.config = new Configuration(Configuration.VERSION_2_3_28) {{
      setDirectoryForTemplateLoading(new File(fullPath));
      setDefaultEncoding(String.valueOf(StandardCharsets.UTF_8));
      setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
      setLogTemplateExceptions(false);
      setWrapUncheckedExceptions(true);
    }};
  }

  @SneakyThrows
  public static TemplateEngine folder(String path) {
    return new TemplateEngine(path);
  }

  public void render(String template, HashMap<String, Object> data, HttpServletResponse res) {
    res.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));

    try (PrintWriter pw = res.getWriter()) {
    //  res.getOutputStream().close();
      config.getTemplate(template).process(data, pw);
    } catch (TemplateException | IOException e) {
      System.out.println(e.getMessage());
      throw new RuntimeException("Freemarker error: ", e);
    }
  }

}
