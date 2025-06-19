import java.io.IOException;
import java.nio.file.Path;

public class Main {

  public static void main(String[] args) {
    try {
      int numberOfPdfs = 2;
      int pagesPerPdf = 100;
      Path outputDirectory = Path.of("build/generated-pdfs");

      PdfGenerator.generateMultiplePdfs(numberOfPdfs, pagesPerPdf, outputDirectory);

      System.out.println("PDFs generated successfully!");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
