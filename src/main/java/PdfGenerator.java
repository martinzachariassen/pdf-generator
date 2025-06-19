import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class PdfGenerator {

  public static void generateMultiplePdfsOfSize(int numberOfPdfs, double targetSizeInMB,
      Path outputDirectory) throws IOException {
    if (!Files.exists(outputDirectory)) {
      Files.createDirectories(outputDirectory);
    }

    List<PdfResult> results = new ArrayList<>();
    Instant start = Instant.now();

    for (int i = 1; i <= numberOfPdfs; i++) {
      String filename = "attachment-" + i + ".pdf";
      Path outputPath = outputDirectory.resolve(filename);
      System.out.printf("Generating PDF #%d of ~%fMB at: %s%n", i, targetSizeInMB,
          outputPath.toAbsolutePath());

      long sizeBytes = generatePdfOfApproximateSize("Attachment " + i, targetSizeInMB, outputPath);
      results.add(new PdfResult(filename, sizeBytes));
    }

    Instant end = Instant.now();
    Duration duration = Duration.between(start, end);

    long totalSizeBytes = results.stream().mapToLong(r -> r.sizeBytes).sum();
    double totalMB = totalSizeBytes / 1024.0 / 1024.0;

    System.out.println("\n✅ Generated " + results.size() + " PDFs:");
    for (PdfResult r : results) {
      System.out.printf(" - %s (%.2fMB)%n", r.filename, r.sizeBytes / 1024.0 / 1024.0);
    }
    System.out.printf("Total size: %.2fMB%n", totalMB);
    System.out.printf("Duration: %02d:%02d sec%n", duration.toMinutesPart(),
        duration.toSecondsPart());

    System.out.printf("Output directory: %s%n", outputDirectory.toAbsolutePath());
  }

  private record PdfResult(String filename, long sizeBytes) {

  }

  public static long generatePdfOfApproximateSize(String title, double targetSizeInMB,
      Path outputPath)
      throws IOException {
    final long targetSizeBytes = (long) (targetSizeInMB * 1024 * 1024);
    final String fillerLine = "This is test content. ".repeat(50);
    final int linesPerPage = 60;
    final int progressBarWidth = 40;

    try (PDDocument document = new PDDocument()) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int pageNumber = 1;
      int lastReportedPercent = -1;

      System.out.printf("Generating '%s' targeting ~%.2fMB...%n", outputPath.getFileName(),
          targetSizeInMB);
      printProgressBar(0, progressBarWidth); // Start med 0%

      while (true) {
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
          PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
          contentStream.setFont(font, 10);
          contentStream.setLeading(14f);
          contentStream.beginText();
          contentStream.newLineAtOffset(50, 750);
          contentStream.showText(title + " - Page " + pageNumber);
          contentStream.newLine();

          for (int i = 0; i < linesPerPage; i++) {
            contentStream.showText(fillerLine);
            contentStream.newLine();
          }

          contentStream.endText();
        }

        // Re-serialize to measure
        baos.reset();
        document.save(baos);
        long currentSize = baos.size();
        int currentPercent = (int) ((currentSize / (double) targetSizeBytes) * 100);

        if (currentPercent > lastReportedPercent) {
          printProgressBar(currentPercent, progressBarWidth);
          lastReportedPercent = currentPercent;
        }

        if (currentSize >= targetSizeBytes) {
          break;
        }

        pageNumber++;
      }

      try (FileOutputStream fos = new FileOutputStream(outputPath.toFile())) {
        baos.writeTo(fos);
      }

      printProgressBar(100, progressBarWidth);
      System.out.printf("%n✅ Done: '%s' (%.2fMB, %d pages)%n",
          outputPath.getFileName(), baos.size() / 1024.0 / 1024.0, pageNumber);
      System.out.println("--------------------------------------------------");
      return baos.size();
    }
  }

  private static void printProgressBar(int percent, int width) {
    int completedWidth = (percent * width) / 100;
    String bar = "#".repeat(completedWidth) + "-".repeat(width - completedWidth);
    System.out.printf("\r[%s] %3d%%", bar, percent);
  }
}