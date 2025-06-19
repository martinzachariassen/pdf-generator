import java.nio.file.Path;

public class Main {
  public static void main(String[] args) {
    try {
      PdfConfig config = ConfigLoader.loadConfig();

      int numberOfPdfs = config.pdf.count;
      double targetSizeInMB = config.pdf.sizeMB;
      Path outputDirectory = config.pdf.getOutputPath();

      PdfGenerator.generateMultiplePdfsOfSize(numberOfPdfs, targetSizeInMB, outputDirectory);
    } catch (Exception e) {
      System.err.println("❌ Failed to generate PDFs: " + e.getMessage());
      throw new RuntimeException("PDF generation failed", e);
    }
  }
}