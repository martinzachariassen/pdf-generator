import java.io.IOException;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class ConfigLoader {
  public static PdfConfig loadConfig() {
    LoaderOptions options = new LoaderOptions();
    Constructor constructor = new Constructor(PdfConfig.class, options);
    Yaml yaml = new Yaml(constructor);

    try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream("application.yml")) {
      if (in == null) {
        throw new RuntimeException("Could not find application.yml in classpath");
      }
      return yaml.load(in);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load config", e);
    }
  }
}