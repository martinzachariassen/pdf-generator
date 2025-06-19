# 📄 PDF Generator for Testing Purposes

This is a simple Java utility built with [Apache PDFBox](https://pdfbox.apache.org/) to generate dummy PDF documents for testing purposes. It supports configuration of both the number of files and their approximate sizes, making it ideal for stress-testing systems that deal with document ingestion, transmission, or storage.

## ✨ Features

- 📁 Generate one or more PDF files at once
- 🔢 Specify approximate size (in MB) per file (e.g. `0.3`, `10`, `20`)
- 📄 Fills each page with placeholder text
- 📊 Includes a live progress bar while generating large files
- 🗂️ Output directory is configurable

## ⚙️ Configuration via `application.yml`

The behavior of the generator is defined through a YAML config file.

### 📄 `src/main/resources/application.yml`

```yaml
pdf:
  count: 3             # Number of PDFs to generate
  sizeMB: 0.3          # Approximate size in megabytes per file (supports decimal values)
  outputDir: build/generated-pdfs  # Target output directory
```

## 🚀 Running the Tool
1. Prerequisites
    - Java 11 or higher
    - Maven 3.6
2. Compile the project
```bash
mvn compile
```
3. Run the Main class
```bash
mvn exec:java -Dexec.mainClass="Main"
```


## 🧾 Example Use Cases
```bash
Generating PDF #1 of ~0.3MB at: /your/path/build/generated-pdfs/attachment-1.pdf
Generating 'attachment-1.pdf' targeting ~0.3MB...
[##########------------------------------]  25% (0MB/0.3MB)
[####################--------------------]  50% (0MB/0.3MB)
[################################--------]  75% (0MB/0.3MB)
[########################################] 100% (0MB/0.3MB)
✅ Done: 'attachment-1.pdf' (0.31MB, 5 pages)
--------------------------------------------------

✅ Generated 1 PDFs:
 - attachment-1.pdf (0.31MB)
Total size: 0.31MB
Duration: 00:02 sec
Output directory: /your/path/build/generated-pdfs
```