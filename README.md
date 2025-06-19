# 📄 PDF Generator for Testing

A simple Java utility using [Apache PDFBox](https://pdfbox.apache.org/) to generate test PDFs with configurable number of pages and placeholder content.

Designed to support testing of document-based integrations by generating realistic dummy attachments.

## ✨ Features

- Generate multiple PDFs in one run
- Custom number of pages per file
- Fills each page with test content
- Saves output to `build/generated-pdfs/` (excluded from Git)

## 🚀 Usage

### 🔧 Build and Run with Maven

```bash
# Compile
mvn compile

# Run the main class
mvn exec:java -Dexec.mainClass="Main"
```

### 📂 Example Output
```bash
Generating 2 PDF(s) with 100 page(s) each in directory: /absolute/path/build/generated-pdfs
Creating: attachment-1.pdf
Creating: attachment-2.pdf
All PDFs generated successfully.
```