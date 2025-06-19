import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;

public class PdfGenerator {

  public static void generateMultiplePdfs(int numberOfPdfs, int pagesPerPdf, Path outputDirectory)
      throws IOException {
    if (!Files.exists(outputDirectory)) {
      Files.createDirectories(outputDirectory);
    }

    System.out.printf("Generating %d PDF(s) with %d page(s) each in directory: %s%n",
        numberOfPdfs, pagesPerPdf, outputDirectory.toAbsolutePath());

    for (int i = 1; i <= numberOfPdfs; i++) {
      String filename = "attachment-" + i + ".pdf";
      Path outputPath = outputDirectory.resolve(filename);
      System.out.println("Creating: " + outputPath.getFileName());
      createPdfWithMultiplePages("Attachment " + i, pagesPerPdf, outputPath);
    }

    System.out.println("All PDFs generated successfully.");
  }

  public static void createPdfWithMultiplePages(String title, int pages, Path outputPath)
      throws IOException {
    try (PDDocument document = new PDDocument()) {
      for (int i = 1; i <= pages; i++) {
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
          contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
          contentStream.beginText();
          contentStream.setLeading(16f);
          contentStream.newLineAtOffset(50, 750);

          contentStream.showText(title + " - Page " + i);
          contentStream.newLine();

          contentStream.setFont(PDType1Font.HELVETICA, 10);

          // Fill the page with placeholder text
          IntStream.range(0, 40).forEach(line -> {
            try {
              contentStream.showText(
                  "Line " + (line + 1) + ": This is test content for PDF generation.");
              contentStream.newLine();
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });

          contentStream.endText();
        }
      }
      document.save(outputPath.toFile());
    }
  }
}