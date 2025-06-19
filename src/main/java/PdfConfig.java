import java.nio.file.Path;

public class PdfConfig {
  public PdfSettings pdf;

  public static class PdfSettings {
    public int count;
    public double sizeMB;
    public String outputDir;

    public Path getOutputPath() {
      return Path.of(outputDir);
    }
  }
}