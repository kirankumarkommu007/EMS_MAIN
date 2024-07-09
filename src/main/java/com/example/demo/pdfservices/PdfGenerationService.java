package com.example.demo.pdfservices;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
public class PdfGenerationService {

    private final TemplateEngine templateEngine;

    public PdfGenerationService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void generatePdf(String to) throws Exception {
        // Prepare the Thymeleaf context
        Context context = new Context();
        context.setVariable("to", to);

        // Render the Thymeleaf template to String
        String renderedHtmlContent = templateEngine.process("/views/fragments/addemployeedownloadform", context);

        // Generate PDF from rendered HTML content
        generatePdfFromHtml(renderedHtmlContent);
    }

    private void generatePdfFromHtml(String html) throws Exception {
        String outputFolder = System.getProperty("user.home") + "/thymeleaf.pdf";
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }
}
