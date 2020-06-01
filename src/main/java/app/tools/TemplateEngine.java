package app.tools;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Log4j2
public class TemplateEngine {
  private final Configuration config;

  public TemplateEngine(String fullPath) {
    this.config = new Configuration(Configuration.VERSION_2_3_28) {{
      try {
        setDirectoryForTemplateLoading(new File(fullPath));

        setDefaultEncoding(String.valueOf(StandardCharsets.UTF_8));
        setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        setLogTemplateExceptions(false);
        setWrapUncheckedExceptions(true);

      } catch (IOException e) {
        log.warn(String.format("IOException happened in method setDirectoryForTemplateLoading(): %s", e.getMessage()));
      }
    }};
  }

  public static TemplateEngine folder(String path) {
    return new TemplateEngine(path);
  }

  public void render(String template, HashMap<String, Object> data, HttpServletResponse res) {
    res.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));

    try (PrintWriter pw = res.getWriter()) {
      //  res.getOutputStream().close();
      config.getTemplate(template).process(data, pw);
    } catch (TemplateException | IOException e) {
      log.warn(String.format("Freemarker template rendering failed: %s", e.getMessage()));
    }
  }
}

